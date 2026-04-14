package com.enterprise.chat;

import com.enterprise.chat.controller.ChatController;
import com.enterprise.chat.core.ApplicationEventBus;
import com.enterprise.chat.ui.ChatWindow;
import com.formdev.flatlaf.FlatLightLaf;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.swing.SwingUtilities;

/**
 * Application entry point.
 *
 * <h3>Startup sequence</h3>
 * <ol>
 *   <li>Install the FlatLaf look-and-feel (must happen before any Swing component is created).</li>
 *   <li>Bootstrap the Spring DI container.</li>
 *   <li>Create the UI <b>on the Event Dispatch Thread</b> via {@code SwingUtilities.invokeLater}.</li>
 * </ol>
 */
@Configuration
@ComponentScan("com.enterprise.chat")
public class MainApp {

    public static void main(String[] args) {
        // 1. Look & feel — must precede any Swing component creation
        FlatLightLaf.setup();

        // 2. Spring dependency injection
        ApplicationContext context = new AnnotationConfigApplicationContext(MainApp.class);
        ChatController controller = context.getBean(ChatController.class);
        ApplicationEventBus eventBus = context.getBean(ApplicationEventBus.class);

        // 3. Boot the UI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> new ChatWindow(controller, eventBus));
    }
}