module top.chiloven.mcsmp4j {
    requires java.net.http;
    requires tools.jackson.core;
    requires tools.jackson.databind;
    requires static org.jspecify;

    exports top.chiloven.mcsmp4j;
    exports top.chiloven.mcsmp4j.protocol;
}
