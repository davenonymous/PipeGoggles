package com.davenonymous.pipegoggles.lib.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.Size2i;

import java.util.HashMap;
import java.util.Map;

public class WidgetIconSelect<T> extends WidgetWithChoiceValue<T> {
	Map<T, IconData> iconMap;
	float textureWidth = 16.0f;
	float textureHeight = 16.0f;
	Size2i offset = new Size2i(0, 0);

	public WidgetIconSelect() {
		this.setHeight(16);
		this.setWidth(16);

		this.iconMap = new HashMap<>();
		this.addClickListener();
	}

	public WidgetIconSelect<T> setTextureSize(float width, float height) {
		this.textureWidth = width;
		this.textureHeight = height;
		return this;
	}

	public WidgetIconSelect<T> setOffset(Size2i offset) {
		this.offset = offset;
		return this;
	}

	public void mapChoiceToSprite(T choice, IconData sprite) {
		iconMap.put(choice, sprite);
	}

	public void addChoiceWithSprite(T choice, IconData sprite) {
		this.addChoice(choice);
		this.mapChoiceToSprite(choice, sprite);
	}

	@Override
	public void draw(GuiGraphics pGuiGraphics, Screen screen) {
		var sprite = iconMap.get(this.getValue());
		if(sprite == null) {
			return;
		}

		if(visible && areAllParentsVisible()) {
			RenderSystem.enableBlend();
			pGuiGraphics.pose().pushPose();
			pGuiGraphics.pose().scale(scale, scale, 1);
			pGuiGraphics.blitInscribed(sprite.icon(), offset.width, offset.height, this.width, this.height, (int) this.textureWidth, (int) this.textureHeight, true, true);
			pGuiGraphics.pose().popPose();
			RenderSystem.disableBlend();
		}
	}

	public record IconData(ResourceLocation icon, int width, int height) {
	}
}
