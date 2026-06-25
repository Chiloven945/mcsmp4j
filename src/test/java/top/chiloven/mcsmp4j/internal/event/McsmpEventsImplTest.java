package top.chiloven.mcsmp4j.internal.event;

import org.junit.jupiter.api.Test;
import top.chiloven.mcsmp4j.McsmpObjectMapper;
import top.chiloven.mcsmp4j.event.PlayerJoinedEvent;
import top.chiloven.mcsmp4j.event.RawMcsmpEvent;
import top.chiloven.mcsmp4j.protocol.JsonRpcNotification;
import top.chiloven.mcsmp4j.protocol.JsonRpcNotificationListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

final class McsmpEventsImplTest {

    @Test
    void dispatchesTypedAndRawEventsAndSupportsUnsubscribe() throws Exception {
        var mapper = McsmpObjectMapper.create();
        var listenerRef = new AtomicReference<JsonRpcNotificationListener>();
        var events = new McsmpEventsImpl(
                new McsmpEventDecoder(mapper, false),
                listener -> {
                    listenerRef.set(listener);
                    return () -> listenerRef.set(null);
                }
        );
        var typed = new ArrayList<PlayerJoinedEvent>();
        var raw = new ArrayList<RawMcsmpEvent>();

        var typedSubscription = events.on(PlayerJoinedEvent.class, typed::add);
        var rawSubscription = events.onRaw(PlayerJoinedEvent.METHOD, raw::add);

        listenerRef.get().onNotification(new JsonRpcNotification(
                PlayerJoinedEvent.METHOD,
                mapper.readTree("[{\"name\":\"Alex\"}]")
        ));

        assertThat(typed).hasSize(1);
        assertThat(typed.getFirst().player().name()).isEqualTo("Alex");
        assertThat(raw).hasSize(1);

        typedSubscription.close();
        rawSubscription.close();
        listenerRef.get().onNotification(new JsonRpcNotification(
                PlayerJoinedEvent.METHOD,
                mapper.readTree("[{\"name\":\"Steve\"}]")
        ));

        assertThat(typed).hasSize(1);
        assertThat(raw).hasSize(1);
    }

}
