package com.davenonymous.pipegoggles.lib.gui.tooltip;

import com.davenonymous.pipegoggles.lib.gui.widgets.Widget;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public class CenteredTooltipComponent extends SeparatorTooltipComponent {
	ClientTooltipComponent component;

	public CenteredTooltipComponent(Widget owner, ClientTooltipComponent component) {
		super(owner);
		this.component = component;
	}

	@Override
	public void renderSeparator(Font font, int maxTooltipWidth, GuiGraphics guiGraphics) {
		int width = component.getWidth(font);
		int x = (maxTooltipWidth - width) / 2;

		component.renderImage(font, x, 0, guiGraphics);
	}

	@Override
	public int getHeight() {
		return component.getHeight();
	}

	@Override
	public int getWidth(Font font) {
		return component.getWidth(font) + 10;
	}
}
