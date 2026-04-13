package com.enterprise.chat.model;

import org.springframework.stereotype.Component;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

@Component
public class UserSettings {
    private boolean doNotDisturb = false;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void setDoNotDisturb(boolean dnd) {
        boolean old = this.doNotDisturb;
        this.doNotDisturb = dnd;
        support.firePropertyChange("doNotDisturb", old, dnd);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    // ADD THIS MISSING METHOD
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}