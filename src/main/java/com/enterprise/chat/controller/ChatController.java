package com.enterprise.chat.controller;

import com.enterprise.chat.model.UserSettings;
import com.enterprise.chat.service.HistoryService;
import com.enterprise.chat.service.NetworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatController {
    private final NetworkService networkService;
    private final HistoryService historyService;
    private final UserSettings settings;

    @Autowired
    public ChatController(NetworkService networkService, HistoryService historyService, UserSettings settings) {
        this.networkService = networkService;
        this.historyService = historyService;
        this.settings = settings;
    }

    public void handleSendMessageRequest(String messageText) {
        if (messageText != null && !messageText.trim().isEmpty()) {
            networkService.sendMessageToServer(messageText);
        }
    }

    public void loadInitialData() {
        historyService.loadChatHistory();
    }
    
    public UserSettings getSettingsModel() {
        return settings;
    }
}