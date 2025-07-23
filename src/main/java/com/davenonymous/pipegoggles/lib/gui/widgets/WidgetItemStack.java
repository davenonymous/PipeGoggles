package com.davenonymous.pipegoggles.lib.gui.widgets;

import com.davenonymous.pipegoggles.lib.gui.GUIHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.Collections;

public class WidgetItemStack extends WidgetWithValue<ItemStack> {
	boolean drawSlot = false;
	boolean grayOut = false;
	boolean drawTooltip = true;
	boolean drawDecorations = true;

	public WidgetItemStack(ItemStack stack) {
		this.setSize(16, 16);
		this.setValue(stack);
	}

	public WidgetItemStack(ItemStack stack, boolean drawSlot) {
		this(stack);
		this.drawSlot = drawSlot;
	}

	public WidgetItemStack setDrawDecorations(boolean drawDecorations) {
		this.drawDecorations = drawDecorations;
		return this;
	}

	public WidgetItemStack setDrawSlot(boolean drawSlot) {
		this.drawSlot = drawSlot;
		return this;
	}

	public WidgetItemStack setDrawTooltip(boolean drawTooltip) {
		this.drawTooltip = drawTooltip;
		this.setValue(this.value);
		return this;
	}

	public WidgetItemStack setGrayOut(boolean grayOut) {
		this.grayOut = grayOut;
		return this;
	}

	public boolean grayOut() {
		return grayOut;
	}

	public void setValue(ItemStack stack) {
		this.setTooltipLines(Collections.emptyList());
		if(this.drawTooltip) {
			if(!stack.isEmpty()) {
				var tooltipFlag = Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL;
				this.setTooltipLines(stack.getTooltipLines(
					Item.TooltipContext.EMPTY,
					Minecraft.getInstance().player,
					tooltipFlag
				));
			}
		}

		super.setValue(stack);
	}

	@Override
	public void draw(GuiGraphics pGuiGraphics, Screen screen) {
		super.draw(pGuiGraphics, screen);

		if(drawSlot) {
			this.drawSlot(pGuiGraphics, screen);
		}

		if(this.value == null || this.value.isEmpty()) {
			return;
		}

		if(!visible || !areAllParentsVisible()) {
			return;
		}

		if(grayOut) {
			float r = 0.0f;
			float g = 0.0f;
			float b = 0.0f;

			float[] oldColor = RenderSystem.getShaderColor();

			pGuiGraphics.setColor(r, g, b, 0.5f);
			pGuiGraphics.renderItem(this.value, 0, 0);
			if (drawDecorations) {
				pGuiGraphics.renderItemDecorations(Minecraft.getInstance().font, this.value, 0, 0);
			}

			RenderSystem.setShaderColor(oldColor[0], oldColor[1], oldColor[2], oldColor[3]);
		} else {
			pGuiGraphics.renderItem(this.value, 0, 0);

			if (drawDecorations) {
				pGuiGraphics.renderItemDecorations(Minecraft.getInstance().font, this.value, 0, 0);
			}
		}
	}

	protected void drawSlot(GuiGraphics pGuiGraphics, Screen screen) {
		RenderSystem.setShaderTexture(0, GUIHelper.tabIcons);

		int texOffsetY = 84;
		int texOffsetX = 84;
		pGuiGraphics.blit(GUIHelper.tabIcons, -1, -1, texOffsetX, texOffsetY, 18, 18);
	}

	protected void drawOverlay(GuiGraphics pGuiGraphics, Screen screen) {
		RenderSystem.setShaderTexture(0, GUIHelper.tabIcons);

		int texOffsetY = 85;
		int texOffsetX = 85;
		pGuiGraphics.blit(GUIHelper.tabIcons, 0, 0, texOffsetX, texOffsetY, 16, 16);
	}
}
