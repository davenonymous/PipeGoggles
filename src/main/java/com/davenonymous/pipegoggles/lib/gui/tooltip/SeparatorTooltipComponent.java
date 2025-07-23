package com.davenonymous.pipegoggles.lib.gui.tooltip;

import com.davenonymous.pipegoggles.lib.gui.widgets.Widget;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public abstract class SeparatorTooltipComponent implements TooltipComponent, ClientTooltipComponent {
	Widget owner;

	public SeparatorTooltipComponent(Widget owner) {
		this.owner = owner;
	}

	public abstract void renderSeparator(Font font, int maxTooltipWidth, GuiGraphics guiGraphics);

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
		guiGraphics.pose().pushPose();
		guiGraphics.pose().translate(x, y, 0);
		renderSeparator(font, owner.getActualTooltipWidth(), guiGraphics);
		guiGraphics.pose().popPose();
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public int getWidth(Font font) {
		return 0;
	}
}
