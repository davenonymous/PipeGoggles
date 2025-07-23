package com.davenonymous.pipegoggles.lib.gui.event;

public class ValueChangedEvent<T> implements IEvent {
	public T oldValue;
	public T newValue;

	public ValueChangedEvent(T oldValue, T newValue) {
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
}
