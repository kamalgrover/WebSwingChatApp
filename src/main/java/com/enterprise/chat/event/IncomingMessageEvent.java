package com.enterprise.chat.event;

/**
 * Immutable event published on the application event bus
 * whenever a message arrives (from the network or from history replay).
 *
 * <p>Uses a Java 17 {@code record} — the compiler generates the constructor,
 * accessors ({@code message()}, {@code isHistory()}), {@code equals},
 * {@code hashCode}, and {@code toString} automatically.</p>
 *
 * @param message   the chat message text
 * @param isHistory {@code true} when this is a replayed history message
 */
public record IncomingMessageEvent(String message, boolean isHistory) { }
