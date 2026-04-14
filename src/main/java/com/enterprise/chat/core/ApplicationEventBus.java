package com.enterprise.chat.core;

import com.google.common.eventbus.EventBus;
import org.springframework.stereotype.Component;

/**
 * Application-wide event bus for decoupled publish / subscribe communication.
 *
 * <h3>Why not a static singleton?</h3>
 * <ul>
 *   <li>A Spring-managed bean can be <b>injected</b>, so services declare
 *       their dependency explicitly in the constructor.</li>
 *   <li>In unit tests you can create a fresh instance — no shared mutable
 *       static state leaking between tests.</li>
 * </ul>
 *
 * <p>Wraps the Guava {@link EventBus}. In a larger project consider Spring's
 * {@code ApplicationEventPublisher} or a reactive library instead.</p>
 */
@Component
public class ApplicationEventBus {

    private final EventBus eventBus;

    public ApplicationEventBus() {
        this.eventBus = new EventBus();
    }

    /** Visible for testing — lets you supply a custom {@link EventBus}. */
    ApplicationEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    /** Register a subscriber whose {@code @Subscribe} methods will receive events. */
    public void register(Object subscriber) {
        eventBus.register(subscriber);
    }

    /** Unregister a subscriber — <b>must</b> be called to prevent memory leaks. */
    public void unregister(Object subscriber) {
        eventBus.unregister(subscriber);
    }

    /** Post an event to all registered subscribers. Dispatches on the calling thread. */
    public void post(Object event) {
        eventBus.post(event);
    }
}

