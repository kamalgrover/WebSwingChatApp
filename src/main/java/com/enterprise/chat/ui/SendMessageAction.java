package com.enterprise.chat.ui;

import com.enterprise.chat.controller.ChatController;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * A Swing {@link Action} that sends the current input text as a chat message.
 *
 * <h3>Why use {@code Action} instead of {@code ActionListener}?</h3>
 * <ul>
 *   <li>An {@code Action} can be shared between a {@link JButton}, a
 *       {@link JMenuItem}, and a <b>key binding</b> — all three stay in sync
 *       automatically (enabled state, name, tooltip, icon).</li>
 *   <li>Disabling the Action disables <em>every</em> control wired to it.</li>
 *   <li>It bundles name, tooltip, icon, and enabled state in a single object,
 *       following the <em>Command</em> pattern.</li>
 * </ul>
 *
 * @see ChatWindow ChatWindow for how this Action is bound to both a button click and the Enter key.
 */
public final class SendMessageAction extends AbstractAction {

    private final JTextField inputField;
    private final JTextArea chatArea;
    private final ChatController controller;

    public SendMessageAction(String name,
                             ChatController controller,
                             JTextField inputField,
                             JTextArea chatArea) {
        super(name);
        putValue(SHORT_DESCRIPTION, "Send the message (Enter)");
        this.controller = controller;
        this.inputField = inputField;
        this.chatArea = chatArea;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            chatArea.append("You: " + text + "\n");
            controller.sendMessage(text);
            inputField.setText("");
            inputField.requestFocusInWindow();
        }
    }
}


