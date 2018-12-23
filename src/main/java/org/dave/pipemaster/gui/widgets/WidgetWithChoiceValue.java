package org.dave.pipemaster.gui.widgets;

import org.dave.pipemaster.gui.event.MouseClickEvent;
import org.dave.pipemaster.gui.event.ValueChangedEvent;
import org.dave.pipemaster.gui.event.WidgetEventResult;
import org.dave.pipemaster.util.CircularPointedArrayList;

import java.util.Collection;

public class WidgetWithChoiceValue<T> extends Widget {
    CircularPointedArrayList<T> choices;

    public WidgetWithChoiceValue() {
        choices = new CircularPointedArrayList<>();
    }

    public T getValue() {
        return this.choices.getPointedElement();
    }

    public void setValue(T choice) {
        choices.setPointerTo(choice);
    }

    public void addChoice(T... newChoices) {
        for(T newChoice : newChoices) {
            this.choices.add(newChoice);
        }
    }

    public void addChoice(Collection<T> newChoices) {
        this.choices.addAll(newChoices);
    }

    public void next() {
        T oldValue = choices.getPointedElement();
        T newValue = choices.next();
        this.fireEvent(new ValueChangedEvent<T>(oldValue, newValue));
    }

    public void prev() {
        T oldValue = choices.getPointedElement();
        T newValue = choices.prev();
        this.fireEvent(new ValueChangedEvent<T>(oldValue, newValue));
    }

    public void addClickListener() {
        this.addListener(MouseClickEvent.class, (event, widget) -> {
            if(event.isLeftClick()) {
                ((WidgetWithChoiceValue<T>)widget).next();
            } else {
                ((WidgetWithChoiceValue<T>)widget).prev();
            }

            return WidgetEventResult.HANDLED;
        });
    }
}
