package top.chiloven.mcsmp4j.internal;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

final class MockMcsmpWebSocketServer implements Closeable {

    private static final String WEBSOCKET_GUID = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

    private final ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final CompletableFuture<Handshake> handshake = new CompletableFuture<>();
    private final CompletableFuture<Void> done = new CompletableFuture<>();

    private MockMcsmpWebSocketServer(Handler handler) throws IOException {
        this.serverSocket = new ServerSocket(0);
        this.executor.execute(() -> accept(handler));
    }

    static MockMcsmpWebSocketServer start(Handler handler) throws IOException {
        return new MockMcsmpWebSocketServer(handler);
    }

    static MockMcsmpWebSocketServer rejecting(int statusCode, String reason) throws IOException {
        return start(session -> session.reject(statusCode, reason));
    }

    URI uri() {
        return URI.create("ws://127.0.0.1:" + serverSocket.getLocalPort());
    }

    Handshake handshake() {
        return handshake.join();
    }

    void awaitDone() {
        done.orTimeout(5, TimeUnit.SECONDS).join();
    }

    private void accept(Handler handler) {
        try (var socket = serverSocket.accept(); var session = Session.create(socket)) {
            handshake.complete(session.handshake());
            handler.handle(session);
            done.complete(null);
        } catch (Throwable error) {
            handshake.completeExceptionally(error);
            done.completeExceptionally(error);
        }
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
        executor.shutdownNow();
    }

    @FunctionalInterface
    interface Handler {

        void handle(Session session) throws Exception;

    }

    record Handshake(
            String requestLine,
            Map<String, String> headers
    ) {

        String header(String name) {
            return headers.get(name.toLowerCase(Locale.ROOT));
        }

    }

    static final class Session implements Closeable {

        private final Socket socket;
        private final InputStream input;
        private final OutputStream output;
        private final Handshake handshake;
        private boolean accepted;

        private Session(Socket socket, Handshake handshake) throws IOException {
            this.socket = socket;
            this.input = socket.getInputStream();
            this.output = socket.getOutputStream();
            this.handshake = handshake;
        }

        static Session create(Socket socket) throws IOException {
            var handshake = readHandshake(socket.getInputStream());
            return new Session(socket, handshake);
        }

        Handshake handshake() {
            return handshake;
        }

        void accept() throws IOException {
            var key = handshake.header("sec-websocket-key");
            assertThat(key).as("Sec-WebSocket-Key").isNotBlank();
            writeAscii("HTTP/1.1 101 Switching Protocols\r\n"
                    + "Upgrade: websocket\r\n"
                    + "Connection: Upgrade\r\n"
                    + "Sec-WebSocket-Accept: " + acceptKey(key) + "\r\n"
                    + "\r\n");
            accepted = true;
        }

        void reject(int statusCode, String reason) throws IOException {
            writeAscii("HTTP/1.1 " + statusCode + " " + reason + "\r\n"
                    + "Connection: close\r\n"
                    + "Content-Length: 0\r\n"
                    + "\r\n");
        }

        String readText() throws IOException {
            ensureAccepted();
            var frame = readFrame();
            if (frame.opcode == 8) {
                return "";
            }
            assertThat(frame.opcode).as("text frame opcode").isEqualTo(1);
            return new String(frame.payload, StandardCharsets.UTF_8);
        }

        void sendText(String text) throws IOException {
            ensureAccepted();
            sendFrame(true, 1, text.getBytes(StandardCharsets.UTF_8));
        }

        void sendTextFragments(String first, String second) throws IOException {
            ensureAccepted();
            sendFrame(false, 1, first.getBytes(StandardCharsets.UTF_8));
            sendFrame(true, 0, second.getBytes(StandardCharsets.UTF_8));
        }

        void sendClose() throws IOException {
            if (accepted) {
                sendFrame(true, 8, new byte[0]);
            }
        }

        private void ensureAccepted() {
            if (!accepted) {
                throw new IllegalStateException("WebSocket handshake has not been accepted");
            }
        }

        private Frame readFrame() throws IOException {
            int first = input.read();
            if (first < 0) throw new EOFException("missing WebSocket frame");
            int second = input.read();
            if (second < 0) throw new EOFException("missing WebSocket frame length");

            int opcode = first & 0x0F;
            boolean masked = (second & 0x80) != 0;
            long length = second & 0x7F;
            if (length == 126) {
                length = ((long) readByte() << 8) | readByte();
            } else if (length == 127) {
                length = 0;
                for (int i = 0; i < 8; i++) {
                    length = (length << 8) | readByte();
                }
            }
            if (length > Integer.MAX_VALUE) {
                throw new IOException("test frame too large: " + length);
            }

            byte[] mask = masked ? input.readNBytes(4) : new byte[0];
            byte[] payload = input.readNBytes((int) length);
            if (payload.length != (int) length) {
                throw new EOFException("truncated WebSocket frame payload");
            }
            if (masked) {
                for (int i = 0; i < payload.length; i++) {
                    payload[i] = (byte) (payload[i] ^ mask[i % 4]);
                }
            }
            return new Frame(opcode, payload);
        }

        private int readByte() throws IOException {
            int value = input.read();
            if (value < 0) throw new EOFException("missing WebSocket extended length byte");
            return value & 0xFF;
        }

        private void sendFrame(boolean fin, int opcode, byte[] payload) throws IOException {
            Objects.requireNonNull(payload, "payload");
            output.write((fin ? 0x80 : 0x00) | opcode);
            if (payload.length < 126) {
                output.write(payload.length);
            } else if (payload.length <= 0xFFFF) {
                output.write(126);
                output.write((payload.length >>> 8) & 0xFF);
                output.write(payload.length & 0xFF);
            } else {
                output.write(127);
                long length = payload.length;
                for (int shift = 56; shift >= 0; shift -= 8) {
                    output.write((int) ((length >>> shift) & 0xFF));
                }
            }
            output.write(payload);
            output.flush();
        }

        private void writeAscii(String value) throws IOException {
            output.write(value.getBytes(StandardCharsets.ISO_8859_1));
            output.flush();
        }

        @Override
        public void close() throws IOException {
            socket.close();
        }

    }

    private record Frame(int opcode, byte[] payload) {

    }

    private static Handshake readHandshake(InputStream input) throws IOException {
        var buffer = new ByteArrayOutputStream();
        int previous3 = -1, previous2 = -1, previous1 = -1;
        while (true) {
            int current = input.read();
            if (current < 0) {
                throw new EOFException("HTTP upgrade request ended early");
            }
            buffer.write(current);
            if (previous3 == '\r' && previous2 == '\n' && previous1 == '\r' && current == '\n') {
                break;
            }
            previous3 = previous2;
            previous2 = previous1;
            previous1 = current;
        }

        var raw = buffer.toString(StandardCharsets.ISO_8859_1);
        var lines = raw.split("\\r\\n");
        var requestLine = lines[0];
        var headers = new LinkedHashMap<String, String>();
        for (int i = 1; i < lines.length; i++) {
            var line = lines[i];
            if (line.isEmpty()) continue;
            int colon = line.indexOf(':');
            if (colon > 0) {
                headers.put(line.substring(0, colon).toLowerCase(Locale.ROOT), line.substring(colon + 1).trim());
            }
        }
        return new Handshake(requestLine, Map.copyOf(headers));
    }

    private static String acceptKey(String key) {
        try {
            var digest = MessageDigest.getInstance("SHA-1")
                    .digest((key + WEBSOCKET_GUID).getBytes(StandardCharsets.ISO_8859_1));
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

}
