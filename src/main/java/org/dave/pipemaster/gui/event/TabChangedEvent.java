package org.dave.pipemaster.gui.event;

import org.dave.pipemaster.gui.widgets.WidgetPanel;

public class TabChangedEvent extends ValueChangedEvent<WidgetPanel> {
    public TabChangedEvent(WidgetPanel oldValue, WidgetPanel newValue) {
        super(oldValue, newValue);
    }
}
