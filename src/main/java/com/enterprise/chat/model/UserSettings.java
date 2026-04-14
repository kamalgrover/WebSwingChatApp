package com.enterprise.chat.model;

import org.springframework.stereotype.Component;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Observable model for user preferences.
 *
 * <p>Uses the standard JavaBeans {@link PropertyChangeSupport} mechanism so
 * that any interested party (UI, controller, …) can listen for changes
 * without tight coupling.</p>
 */
@Component
public class UserSettings {

    private boolean doNotDisturb = false;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public boolean isDoNotDisturb() {
        return doNotDisturb;
    }

    public void setDoNotDisturb(boolean dnd) {
        boolean old = this.doNotDisturb;
        this.doNotDisturb = dnd;
        pcs.firePropertyChange("doNotDisturb", old, dnd);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}