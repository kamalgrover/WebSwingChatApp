package com.enterprise.chat.service;

import com.enterprise.chat.core.ApplicationEventBus;
import com.enterprise.chat.event.IncomingMessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.swing.SwingWorker;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Loads chat history on a background thread using {@link SwingWorker}.
 *
 * <h3>Key SwingWorker concepts demonstrated</h3>
 * <ul>
 *   <li>{@code doInBackground()} — heavy I/O on a worker thread (off the EDT).</li>
 *   <li>{@code publish() / process()} — stream intermediate results to the EDT
 *       so the UI updates progressively.</li>
 *   <li>{@code done()} — <b>mandatory</b> error check via {@code get()} to surface
 *       exceptions that would otherwise be <em>silently swallowed</em>.</li>
 * </ul>
 */
@Service
public class HistoryService {

    private static final Logger log = LoggerFactory.getLogger(HistoryService.class);

    private final ApplicationEventBus eventBus;

    public HistoryService(ApplicationEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void loadChatHistory() {
        log.info("Loading chat history...");

        new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Simulate slow I/O -- runs on a background worker thread
                publish("--- Loading History ---");
                Thread.sleep(500);
                publish("Alice: Architecture looks good.");
                Thread.sleep(500);
                publish("--- History Loaded ---");
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                // Runs on the EDT — safe to touch UI / post events
                for (String chunk : chunks) {
                    eventBus.post(new IncomingMessageEvent(chunk, true));
                }
            }

            @Override
            protected void done() {
                /*
                 * IMPORTANT: Always call get() in done().
                 * If doInBackground() threw an exception, get() re-throws it
                 * wrapped in ExecutionException. Without this, errors are
                 * silently swallowed — one of the most common SwingWorker bugs.
                 */
                try {
                    get();
                    log.info("Chat history loaded successfully");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("History load interrupted", e);
                } catch (ExecutionException e) {
                    log.error("Failed to load chat history", e.getCause());
                }
            }
        }.execute();
    }
}