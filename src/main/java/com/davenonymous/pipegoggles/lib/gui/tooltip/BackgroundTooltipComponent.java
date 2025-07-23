package com.davenonymous.pipegoggles.lib.gui.tooltip;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class BackgroundTooltipComponent implements TooltipComponent, ClientTooltipComponent {
	ClientTooltipComponent component;
	ResourceLocation backgroundImage;
	int paddingTop = 6;
	int paddingRight = 6;
	int paddingBottom = 4;
	int paddingLeft = 6;


	public BackgroundTooltipComponent(ClientTooltipComponent component, ResourceLocation backgroundImage) {
		this.component = component;
		this.backgroundImage = backgroundImage;
	}

	public BackgroundTooltipComponent setPadding(int padding) {
		this.paddingTop = padding;
		this.paddingRight = padding;
		this.paddingBottom = padding;
		this.paddingLeft = padding;
		return this;
	}

	public BackgroundTooltipComponent setPadding(int paddingTop, int paddingRight, int paddingBottom, int paddingLeft) {
		this.paddingTop = paddingTop;
		this.paddingRight = paddingRight;
		this.paddingBottom = paddingBottom;
		this.paddingLeft = paddingLeft;
		return this;
	}

	public BackgroundTooltipComponent setPaddingHorizontal(int padding) {
		this.paddingLeft = padding;
		this.paddingRight = padding;
		return this;
	}

	public BackgroundTooltipComponent setPaddingVertical(int padding) {
		this.paddingTop = padding;
		this.paddingBottom = padding;
		return this;
	}

	@Override
	public int getHeight() {
		return this.component.getHeight() + paddingTop + paddingBottom;
	}

	@Override
	public int getWidth(Font font) {
		return this.component.getWidth(font) + paddingLeft + paddingRight;
	}

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
		//GUIHelper.drawEmbossedWindow(guiGraphics, backgroundImage, this.getWidth(font), this.getHeight(), x, y);
		guiGraphics.blitSprite(backgroundImage, x, y, this.getWidth(font), this.getHeight());
		this.component.renderImage(font, x+paddingLeft, y+paddingTop, guiGraphics);
	}
}
