package com.davenonymous.pipegoggles.lib.gui.widgets;


import net.minecraft.resources.ResourceLocation;

public abstract class WidgetPanelWithValue<T> extends WidgetPanel implements IValueProvider<T> {
	private T value;
	private ResourceLocation id;

	public WidgetPanelWithValue(T value) {
		super();
		this.value = value;
	}

	@Override
	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public void setId(ResourceLocation location) {
		this.id = location;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public void setValue(T value) {
		this.value = value;
	}

	public boolean hasValue() {
		return this.value != null;
	}
}
