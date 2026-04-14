package com.enterprise.chat.controller;

import com.enterprise.chat.model.UserSettings;
import com.enterprise.chat.service.HistoryService;
import com.enterprise.chat.service.NetworkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.beans.PropertyChangeListener;

import static org.mockito.Mockito.*;

/**
 * Demonstrates how constructor injection makes the controller trivially testable.
 * No Spring context needed — just Mockito mocks.
 */
@ExtendWith(MockitoExtension.class)
class ChatControllerTest {

    @Mock private NetworkService networkService;
    @Mock private HistoryService historyService;

    private UserSettings settings;
    private ChatController controller;

    @BeforeEach
    void setUp() {
        settings = new UserSettings();
        controller = new ChatController(networkService, historyService, settings);
    }

    // ── sendMessage ──────────────────────────────────────────────

    @Test
    @DisplayName("sendMessage delegates to NetworkService")
    void sendMessage_delegatesToNetworkService() {
        controller.sendMessage("hello");
        verify(networkService).sendMessageToServer("hello");
    }

    @Test
    @DisplayName("sendMessage ignores blank input")
    void sendMessage_ignoresBlank() {
        controller.sendMessage("   ");
        verifyNoInteractions(networkService);
    }

    @Test
    @DisplayName("sendMessage ignores null input")
    void sendMessage_ignoresNull() {
        controller.sendMessage(null);
        verifyNoInteractions(networkService);
    }

    // ── loadInitialData ──────────────────────────────────────────

    @Test
    @DisplayName("loadInitialData delegates to HistoryService")
    void loadInitialData_delegatesToHistory() {
        controller.loadInitialData();
        verify(historyService).loadChatHistory();
    }

    // ── settings façade ──────────────────────────────────────────

    @Test
    @DisplayName("setDoNotDisturb updates the model and fires a PropertyChangeEvent")
    void setDoNotDisturb_updatesModel() {
        PropertyChangeListener listener = mock(PropertyChangeListener.class);
        controller.addSettingsListener(listener);

        controller.setDoNotDisturb(true);

        verify(listener).propertyChange(argThat(evt ->
                "doNotDisturb".equals(evt.getPropertyName())
                        && Boolean.TRUE.equals(evt.getNewValue())));
    }

    @Test
    @DisplayName("removeSettingsListener stops notifications")
    void removeSettingsListener_stopsNotifications() {
        PropertyChangeListener listener = mock(PropertyChangeListener.class);
        controller.addSettingsListener(listener);
        controller.removeSettingsListener(listener);

        controller.setDoNotDisturb(true);

        verifyNoInteractions(listener);
    }
}

