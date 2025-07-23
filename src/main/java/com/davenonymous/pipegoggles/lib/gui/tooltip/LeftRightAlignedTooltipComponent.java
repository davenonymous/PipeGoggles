package com.davenonymous.pipegoggles.lib.gui.tooltip;

import com.davenonymous.pipegoggles.lib.gui.widgets.Widget;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public class LeftRightAlignedTooltipComponent extends SeparatorTooltipComponent {
	ClientTooltipComponent left;
	ClientTooltipComponent right;

	public LeftRightAlignedTooltipComponent(Widget owner, ClientTooltipComponent left, ClientTooltipComponent right) {
		super(owner);
		this.left = left;
		this.right = right;
	}

	@Override
	public void renderSeparator(Font font, int maxTooltipWidth, GuiGraphics guiGraphics) {
		left.renderImage(font, 0, 0, guiGraphics);
		int rightX = maxTooltipWidth - right.getWidth(font);
		right.renderImage(font, rightX, 0, guiGraphics);
	}

	@Override
	public int getHeight() {
		return Math.max(left.getHeight(), right.getHeight());
	}

	@Override
	public int getWidth(Font font) {
		return left.getWidth(font) + 10 +  right.getWidth(font);
	}
}
