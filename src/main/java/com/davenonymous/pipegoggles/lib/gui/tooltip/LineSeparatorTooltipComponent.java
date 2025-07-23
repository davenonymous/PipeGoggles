package com.davenonymous.pipegoggles.lib.gui.tooltip;

import com.davenonymous.pipegoggles.lib.gui.widgets.Widget;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class LineSeparatorTooltipComponent extends SeparatorTooltipComponent {
	int padding = 2;
	int lineWidth = 1;
	int lineColor = 0xFFFFFFFF;

	public LineSeparatorTooltipComponent(Widget owner) {
		super(owner);
	}

	public LineSeparatorTooltipComponent setColor(int lineColor) {
		this.lineColor = lineColor;
		return this;
	}

	public LineSeparatorTooltipComponent setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
		return this;
	}

	public LineSeparatorTooltipComponent setPadding(int padding) {
		this.padding = padding;
		return this;
	}

	@Override
	public void renderSeparator(Font font, int maxTooltipWidth, GuiGraphics guiGraphics) {
		guiGraphics.fill(padding, padding, maxTooltipWidth - padding, padding + lineWidth, lineColor);
	}

	@Override
	public int getHeight() {
		return 2*padding + lineWidth + 2;
	}
}
