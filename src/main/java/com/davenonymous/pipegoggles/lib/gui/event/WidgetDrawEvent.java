package com.davenonymous.pipegoggles.lib.gui.event;

import net.minecraft.client.gui.GuiGraphics;

public record WidgetDrawEvent(Type type, GuiGraphics guiGraphics, float partialTicks) implements IEvent {

	public enum Type {
		PRE,
		POST
	}
}
