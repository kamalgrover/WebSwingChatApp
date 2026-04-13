package com.enterprise.chat.service;

import com.enterprise.chat.core.EventBusManager;
import com.enterprise.chat.event.IncomingMessageEvent;
import org.springframework.stereotype.Service;
import javax.swing.SwingWorker;
import java.util.List;

@Service
public class HistoryService {
    public void loadChatHistory() {
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                publish("--- Loading History ---");
                Thread.sleep(500);
                publish("Alice: Architecture looks good.");
                Thread.sleep(500);
                publish("--- History Loaded ---");
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String chunk : chunks) {
                    EventBusManager.post(new IncomingMessageEvent(chunk, true));
                }
            }
        };
        worker.execute();
    }
}