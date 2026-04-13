package com.enterprise.chat.service;

import com.enterprise.chat.core.EventBusManager;
import com.enterprise.chat.event.IncomingMessageEvent;
import org.springframework.stereotype.Service;

@Service
public class NetworkService {
    public void sendMessageToServer(String msg) {
        System.out.println("Network: Sending -> " + msg);
        
        // Simulate a server echoing the message back after 1 second
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                EventBusManager.post(new IncomingMessageEvent("Echo: " + msg, false));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}