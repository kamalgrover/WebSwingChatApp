package com.enterprise.chat.core;

import com.enterprise.chat.event.IncomingMessageEvent;
import com.google.common.eventbus.Subscribe;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the injectable {@link ApplicationEventBus}.
 * Demonstrates that the bus is a plain object — no Spring context required.
 */
class ApplicationEventBusTest {

    @Test
    @DisplayName("post delivers events to registered subscribers")
    void postDeliversToSubscribers() {
        ApplicationEventBus bus = new ApplicationEventBus();
        AtomicReference<IncomingMessageEvent> received = new AtomicReference<>();

        Object subscriber = new Object() {
            @Subscribe
            public void handle(IncomingMessageEvent e) {
                received.set(e);
            }
        };

        bus.register(subscriber);
        bus.post(new IncomingMessageEvent("test", false));

        assertThat(received.get()).isNotNull();
        assertThat(received.get().message()).isEqualTo("test");
        assertThat(received.get().isHistory()).isFalse();
    }

    @Test
    @DisplayName("unregister stops event delivery")
    void unregisterStopsDelivery() {
        ApplicationEventBus bus = new ApplicationEventBus();
        AtomicReference<IncomingMessageEvent> received = new AtomicReference<>();

        Object subscriber = new Object() {
            @Subscribe
            public void handle(IncomingMessageEvent e) {
                received.set(e);
            }
        };

        bus.register(subscriber);
        bus.unregister(subscriber);
        bus.post(new IncomingMessageEvent("test", false));

        assertThat(received.get()).isNull();
    }
}

