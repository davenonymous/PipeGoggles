package com.davenonymous.pipegoggles.render;

import com.davenonymous.pipegoggles.lib.gui.GUIHelper;
import com.davenonymous.pipegoggles.lib.gui.widgets.WidgetWithRangeValue;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;

public class WidgetTank extends WidgetWithRangeValue<Long> {
	int borderColor = 0xFF000000;
	int backgroundColor = 0xFF333333;
	int centerColor = Blocks.WATER.defaultMapColor().calculateRGBColor(MapColor.Brightness.NORMAL);
	int outerColor = Blocks.WATER.defaultMapColor().calculateRGBColor(MapColor.Brightness.LOWEST);

	public WidgetTank(Long size) {
		this.setRange(0L, size);
		this.setValue(0L);
		this.setWidth(22);
	}

	public WidgetTank setCenterColor(int centerColor) {
		this.centerColor = centerColor;
		return this;
	}

	public WidgetTank setOuterColor(int outerColor) {
		this.outerColor = outerColor;
		return this;
	}

	@Override
	public void draw(GuiGraphics pGuiGraphics, Screen screen) {
		double fillLevel = (double)(getValue() - getRangeMin()) / (double)(getRangeMax() - getRangeMin());
		fillLevel = Math.min(Math.max(fillLevel, 0.0d), 1.0d);
		int fillHeight = Math.max((int) ((Math.ceil((double) height - 1) * (1-fillLevel))), 1);
		int centerPoint = (int) Math.ceil((this.width - 1) / 2.0d);

		pGuiGraphics.fill(0, 0, width, height, borderColor);
		pGuiGraphics.fill(1, 1, width-1, height-1, backgroundColor);
		GUIHelper.fillHorizontalGradient(pGuiGraphics, 1, fillHeight, centerPoint, height - 1, outerColor, centerColor);
		GUIHelper.fillHorizontalGradient(pGuiGraphics, centerPoint, fillHeight, width-1, height - 1, centerColor, outerColor);

	}
}
