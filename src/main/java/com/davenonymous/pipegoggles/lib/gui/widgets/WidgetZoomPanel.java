package com.davenonymous.pipegoggles.lib.gui.widgets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

public class WidgetZoomPanel extends WidgetPanelWithValue<Widget> {
	public float zoomFactor = 1.0f;

	public WidgetZoomPanel(Widget value) {
		super(value);
		this.eventListeners.clear();
		this.anyEventListener.clear();
		this.renderDebugOutlines = true;

		this.setDimensions(0, 0, value.width, value.height);

		this.add(value);
	}

	@Override
	public void draw(GuiGraphics guiGraphics, Screen screen) {
		guiGraphics.pose().pushPose();
		guiGraphics.pose().scale(zoomFactor, zoomFactor, 1.0f);
		super.draw(guiGraphics, screen);
		guiGraphics.pose().popPose();
	}

	@Override
	public Widget getHoveredWidget(int mouseX, int mouseY) {
		int scaledMouseX = Math.round((getMouseX()) / zoomFactor);
		int scaledMouseY = Math.round((getMouseY()) / zoomFactor);
		return super.getHoveredWidget(scaledMouseX, scaledMouseY);
	}
}
