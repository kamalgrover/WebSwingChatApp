package com.enterprise.chat.event;

public class IncomingMessageEvent {
    private final String message;
    private final boolean isHistory;

    public IncomingMessageEvent(String message, boolean isHistory) {
        this.message = message;
        this.isHistory = isHistory;
    }

    public String getMessage() { return message; }
    public boolean isHistory() { return isHistory; }
}