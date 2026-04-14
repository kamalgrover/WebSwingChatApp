package com.enterprise.chat.ui;

import com.enterprise.chat.controller.ChatController;
import com.enterprise.chat.core.ApplicationEventBus;
import com.enterprise.chat.event.IncomingMessageEvent;
import com.google.common.eventbus.Subscribe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;

/**
 * Main chat window demonstrating Swing best practices:
 * <ul>
 *   <li><b>Actions</b> — {@link SendMessageAction} shared between button and key binding.</li>
 *   <li><b>Key Bindings</b> — {@code InputMap / ActionMap} instead of {@code KeyListener}.</li>
 *   <li><b>EDT safety</b> — EventBus handler marshals updates via {@code invokeLater}.</li>
 *   <li><b>Lifecycle management</b> — listeners unregistered on window close.</li>
 *   <li><b>Constructor structure</b> — broken into focused helper methods for readability.</li>
 * </ul>
 */
public class ChatWindow extends JFrame {

    // ── Dependencies ──────────────────────────────────────────────
    private final ChatController controller;
    private final ApplicationEventBus eventBus;

    // ── UI Components ─────────────────────────────────────────────
    private final JTextArea chatArea;
    private final JTextField inputField;

    // ── State ─────────────────────────────────────────────────────
    private boolean soundEnabled = true;

    // ── Listener references (stored for cleanup) ──────────────────
    private PropertyChangeListener settingsListener;

    public ChatWindow(ChatController controller, ApplicationEventBus eventBus) {
        this.controller = controller;
        this.eventBus = eventBus;

        // 1. Initialise UI components
        this.chatArea = createChatArea();
        this.inputField = new JTextField();

        // 2. Configure frame & layout
        configureFrame();
        layoutComponents();

        // 3. Create a single, shared Action for "send message"
        Action sendAction = new SendMessageAction("Send", controller, inputField, chatArea);

        // 4. Wire the Action to both the button and the Enter key
        wireSendAction(sendAction);

        // 5. Observe the settings model through the controller (Law of Demeter)
        wireSettingsObserver();

        // 6. Register for EventBus events & set up cleanup on close
        wireLifecycle();

        // 7. Show and kick off background data loading
        setLocationRelativeTo(null);
        setVisible(true);
        controller.loadInitialData();
    }

    // ── Factory / setup helpers ───────────────────────────────────

    private JTextArea createChatArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setMargin(new Insets(10, 10, 10, 10));
        return area;
    }

    private void configureFrame() {
        setTitle("Enterprise Chat — Webswing");
        setSize(500, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        add(new JScrollPane(chatArea), BorderLayout.CENTER);
    }

    // ── Wiring helpers ────────────────────────────────────────────

    /**
     * Demonstrates the <b>Action</b> and <b>Key Bindings</b> patterns.
     *
     * <p>The same {@code sendAction} is attached to the button <em>and</em> to
     * the {@code Enter} key via the input field's {@code InputMap / ActionMap}.
     * This replaces the fragile {@code KeyListener} anti-pattern.</p>
     *
     * <h4>Why Key Bindings instead of KeyListener?</h4>
     * <ul>
     *   <li>Key Bindings reuse the same {@code Action} — no logic duplication.</li>
     *   <li>They work regardless of focus traversal policies.</li>
     *   <li>They are the Swing-recommended approach (see Java Tutorial).</li>
     * </ul>
     */
    private void wireSendAction(Action sendAction) {
        JButton sendButton = new JButton(sendAction);

        // KEY BINDING — preferred over KeyListener
        String actionKey = "sendMessage";
        inputField.getInputMap(JComponent.WHEN_FOCUSED)
                  .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), actionKey);
        inputField.getActionMap().put(actionKey, sendAction);

        JCheckBox dndCheckBox = new JCheckBox("Do Not Disturb");
        dndCheckBox.addItemListener(e ->
                controller.setDoNotDisturb(dndCheckBox.isSelected()));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        bottom.add(inputField, BorderLayout.CENTER);
        bottom.add(sendButton, BorderLayout.EAST);
        bottom.add(dndCheckBox, BorderLayout.SOUTH);

        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Observes the settings model <em>through</em> the controller (Law of Demeter).
     * The listener reference is stored so it can be removed on shutdown.
     */
    private void wireSettingsObserver() {
        settingsListener = evt -> {
            if ("doNotDisturb".equals(evt.getPropertyName())) {
                soundEnabled = !(Boolean) evt.getNewValue();
                chatArea.append(">> System: Sounds " + (soundEnabled ? "Enabled" : "Muted") + "\n");
            }
        };
        controller.addSettingsListener(settingsListener);
    }

    /**
     * Registers this window on the EventBus and ensures all listeners are
     * cleaned up when the window is closed — preventing memory leaks.
     */
    private void wireLifecycle() {
        eventBus.register(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                eventBus.unregister(ChatWindow.this);
                controller.removeSettingsListener(settingsListener);
                // NOTE: System.exit(0) is used here only for this standalone demo.
                // In a container (e.g., Webswing), remove this — the container
                // manages the application lifecycle.
                System.exit(0);
            }
        });
    }

    // ── EventBus subscriber ───────────────────────────────────────

    /**
     * Receives messages from the service layer via the EventBus.
     *
     * <p><b>EDT Safety:</b> The Guava {@code EventBus} dispatches on the
     * <em>posting</em> thread, which may be a background thread. We must
     * marshal UI updates onto the Event Dispatch Thread with
     * {@link SwingUtilities#invokeLater}.</p>
     */
    @Subscribe
    public void onIncomingMessage(IncomingMessageEvent event) {
        SwingUtilities.invokeLater(() -> {
            chatArea.append(event.message() + "\n");
            if (!event.isHistory() && soundEnabled) {
                Toolkit.getDefaultToolkit().beep();
            }
        });
    }
}