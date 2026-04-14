package com.enterprise.chat.service;

import com.enterprise.chat.core.ApplicationEventBus;
import com.enterprise.chat.event.IncomingMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.swing.SwingWorker;
import java.util.concurrent.ExecutionException;

/**
 * Simulates sending a message over the network and receiving an echo.
 *
 * <h3>Why SwingWorker instead of raw {@code new Thread(...)}?</h3>
 * <ul>
 *   <li>{@code doInBackground()} runs on a managed worker thread — no orphan threads.</li>
 *   <li>{@code done()} runs on the EDT, making it safe to post events that touch the UI.</li>
 *   <li>Exceptions in {@code doInBackground()} surface via {@code get()} in {@code done()},
 *       preventing the silent-failure anti-pattern.</li>
 * </ul>
 */
@Service
public class NetworkService {

    private static final Logger log = LoggerFactory.getLogger(NetworkService.class);

    private final ApplicationEventBus eventBus;

    public NetworkService(ApplicationEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void sendMessageToServer(String msg) {
        log.info("Sending → {}", msg);

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                // Simulate a network round-trip (runs off the EDT)
                Thread.sleep(1_000);
                return "Echo: " + msg;
            }

            @Override
            protected void done() {
                // Runs on the EDT — safe to post UI events
                try {
                    String reply = get();   // re-throws any background exception
                    eventBus.post(new IncomingMessageEvent(reply, false));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("Send interrupted", e);
                } catch (ExecutionException e) {
                    log.error("Send failed", e.getCause());
                }
            }
        }.execute();
    }
}