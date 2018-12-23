package org.dave.pipemaster.gui.widgets;


import org.dave.pipemaster.gui.event.ValueChangedEvent;

public class WidgetWithValue<T> extends Widget {
    T value;

    public T getValue() {
        return this.value;
    }

    public void valueChanged(T oldValue, T newValue) {
        this.value = newValue;
        this.fireEvent(new ValueChangedEvent<T>(oldValue, this.value));
    }
}
