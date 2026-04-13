package com.enterprise.chat;

import com.enterprise.chat.controller.ChatController;
import com.enterprise.chat.ui.ChatWindow;
import com.formdev.flatlaf.FlatLightLaf;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import javax.swing.SwingUtilities;

@Configuration
@ComponentScan("com.enterprise.chat")
public class MainApp {
    public static void main(String[] args) {
        // 1. Initialize Modern UI Theme
        FlatLightLaf.setup();

        // 2. Start Spring Dependency Injection
        ApplicationContext context = new AnnotationConfigApplicationContext(MainApp.class);
        ChatController controller = context.getBean(ChatController.class);

        // 3. Boot UI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new ChatWindow(controller));
    }
}