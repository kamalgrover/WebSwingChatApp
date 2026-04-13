package com.enterprise.chat.ui;

import com.enterprise.chat.controller.ChatController;
import com.enterprise.chat.core.EventBusManager;
import com.enterprise.chat.event.IncomingMessageEvent;
import com.google.common.eventbus.Subscribe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;

public class ChatWindow extends JFrame {
    private final ChatController controller;
    private final JTextArea chatArea;
    private final JTextField inputField;
    private boolean playSound = true;
    private PropertyChangeListener settingsListener;

    public ChatWindow(ChatController controller) {
        this.controller = controller;
        
        setTitle("Enterprise Chat - Webswing");
        setSize(500, 600);
        setLayout(new BorderLayout());

        // --- Build UI Components ---
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setMargin(new Insets(10, 10, 10, 10));
        
        inputField = new JTextField();
        JButton sendButton = new JButton("Send");
        JCheckBox dndCheckBox = new JCheckBox("Do Not Disturb");

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        bottomPanel.add(dndCheckBox, BorderLayout.SOUTH);

        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- Event Bus Registration ---
        EventBusManager.register(this);

        // --- Listeners ---
        sendButton.addActionListener(e -> processLocalMessage());
        
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) processLocalMessage();
            }
        });

        dndCheckBox.addItemListener(e -> 
            controller.getSettingsModel().setDoNotDisturb(dndCheckBox.isSelected())
        );

        settingsListener = evt -> {
            if ("doNotDisturb".equals(evt.getPropertyName())) {
                playSound = !(Boolean) evt.getNewValue();
                chatArea.append(">> System: Sounds " + (playSound ? "Enabled" : "Muted") + "\n");
            }
        };
        controller.getSettingsModel().addPropertyChangeListener(settingsListener);

        setupLifecycleManagement();
        
        setLocationRelativeTo(null);
        setVisible(true);

        // Trigger background load
        controller.loadInitialData();
    }

    private void processLocalMessage() {
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            chatArea.append("You: " + text + "\n");
            controller.handleSendMessageRequest(text);
            inputField.setText("");
        }
    }

    // 🚌 Guava Event Bus Subscriber (Forces UI updates to EDT)
    @Subscribe
    public void onIncomingMessage(IncomingMessageEvent event) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(event.getMessage() + "\n");
            if (!event.isHistory() && playSound) {
                Toolkit.getDefaultToolkit().beep(); // Native ding sound
            }
        });
    }

    // 🛑 Prevent Memory Leaks
    private void setupLifecycleManagement() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                EventBusManager.unregister(ChatWindow.this);
                controller.getSettingsModel().removePropertyChangeListener(settingsListener);
                System.exit(0); // Kill Spring context for this standalone demo
            }
        });
    }
}