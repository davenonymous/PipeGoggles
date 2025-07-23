package com.davenonymous.pipegoggles.lib.gui.event;

public class VisibilityChangedEvent extends ValueChangedEvent<Boolean> {
	public VisibilityChangedEvent(Boolean oldValue, Boolean newValue) {
		super(oldValue, newValue);
	}
}
