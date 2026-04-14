package com.enterprise.chat.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the observable {@link UserSettings} model.
 * Verifies the JavaBeans {@code PropertyChangeSupport} contract.
 */
class UserSettingsTest {

    @Test
    @DisplayName("setDoNotDisturb fires a PropertyChangeEvent with correct values")
    void firesPropertyChange() {
        UserSettings settings = new UserSettings();
        AtomicReference<PropertyChangeEvent> captured = new AtomicReference<>();
        settings.addPropertyChangeListener(captured::set);

        settings.setDoNotDisturb(true);

        assertThat(captured.get()).isNotNull();
        assertThat(captured.get().getPropertyName()).isEqualTo("doNotDisturb");
        assertThat(captured.get().getOldValue()).isEqualTo(false);
        assertThat(captured.get().getNewValue()).isEqualTo(true);
    }

    @Test
    @DisplayName("isDoNotDisturb returns the current value")
    void getterReflectsState() {
        UserSettings settings = new UserSettings();
        assertThat(settings.isDoNotDisturb()).isFalse();

        settings.setDoNotDisturb(true);
        assertThat(settings.isDoNotDisturb()).isTrue();
    }

    @Test
    @DisplayName("removePropertyChangeListener stops notifications")
    void removeListenerStopsNotifications() {
        UserSettings settings = new UserSettings();
        AtomicReference<PropertyChangeEvent> captured = new AtomicReference<>();
        PropertyChangeListener listener = captured::set;

        settings.addPropertyChangeListener(listener);
        settings.removePropertyChangeListener(listener);
        settings.setDoNotDisturb(true);

        assertThat(captured.get()).isNull();
    }
}

