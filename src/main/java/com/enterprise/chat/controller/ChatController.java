package com.enterprise.chat.controller;

import com.enterprise.chat.model.UserSettings;
import com.enterprise.chat.service.HistoryService;
import com.enterprise.chat.service.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.beans.PropertyChangeListener;

/**
 * Mediates between the UI and the service / model layers.
 *
 * <h3>Design decisions</h3>
 * <ul>
 *   <li><b>Constructor injection</b> — every dependency is declared up front;
 *       the class is trivial to instantiate in a test with mocks.</li>
 *   <li><b>Model encapsulation (Law of Demeter)</b> — the view never receives
 *       the {@link UserSettings} object directly. Instead, the controller
 *       exposes focused delegate methods such as {@link #setDoNotDisturb}.</li>
 * </ul>
 */
@Component
public class ChatController {

    private final NetworkService networkService;
    private final HistoryService historyService;
    private final UserSettings settings;

    @Autowired
    public ChatController(NetworkService networkService,
                          HistoryService historyService,
                          UserSettings settings) {
        this.networkService = networkService;
        this.historyService = historyService;
        this.settings = settings;
    }

    /** Validates and forwards a message to the network service. */
    public void sendMessage(String messageText) {
        if (messageText != null && !messageText.isBlank()) {
            networkService.sendMessageToServer(messageText);
        }
    }

    /** Triggers an asynchronous load of chat history. */
    public void loadInitialData() {
        historyService.loadChatHistory();
    }

    // ── Settings façade (Law of Demeter) ──────────────────────────

    public void setDoNotDisturb(boolean dnd) {
        settings.setDoNotDisturb(dnd);
    }

    public void addSettingsListener(PropertyChangeListener listener) {
        settings.addPropertyChangeListener(listener);
    }

    public void removeSettingsListener(PropertyChangeListener listener) {
        settings.removePropertyChangeListener(listener);
    }
}