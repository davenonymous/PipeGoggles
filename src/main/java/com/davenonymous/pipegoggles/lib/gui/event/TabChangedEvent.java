package com.davenonymous.pipegoggles.lib.gui.event;

import com.davenonymous.pipegoggles.lib.gui.widgets.WidgetPanel;

public class TabChangedEvent extends ValueChangedEvent<WidgetPanel> {
	public TabChangedEvent(WidgetPanel oldValue, WidgetPanel newValue) {
		super(oldValue, newValue);
	}
}
