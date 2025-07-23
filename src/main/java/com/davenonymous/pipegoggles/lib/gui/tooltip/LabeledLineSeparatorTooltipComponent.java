package com.davenonymous.pipegoggles.lib.gui.tooltip;

import com.davenonymous.pipegoggles.lib.gui.widgets.Widget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;

public class LabeledLineSeparatorTooltipComponent extends SeparatorTooltipComponent {
	int padding = 2;
	int lineWidth = 1;
	int lineColor = 0xFFFFFFFF;
	String label;

	public LabeledLineSeparatorTooltipComponent(Widget owner, String label) {
		super(owner);
		this.label = label;
	}

	public static LabeledLineSeparatorTooltipComponent advancedInfos(Widget owner) {
		return new LabeledLineSeparatorTooltipComponent(owner, I18n.get("patternconverter.message.advanced_tooltips"));
	}

	public LabeledLineSeparatorTooltipComponent setColor(int lineColor) {
		this.lineColor = lineColor;
		return this;
	}

	public LabeledLineSeparatorTooltipComponent setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
		return this;
	}

	public LabeledLineSeparatorTooltipComponent setPadding(int padding) {
		this.padding = padding;
		return this;
	}

	@Override
	public void renderSeparator(Font font, int maxTooltipWidth, GuiGraphics guiGraphics) {
		int labelWidth = font.width(label);
		int labelX = (maxTooltipWidth - labelWidth) / 2;
		int leftLineEnd = labelX - padding;
		int rightLineStart = labelX + labelWidth + padding;

		int textHeight = font.lineHeight;
		int lineToTextOffset = Math.floorDiv(textHeight - lineWidth, 2) - 1;

		guiGraphics.fill(padding, padding + lineToTextOffset, leftLineEnd, padding + lineWidth + lineToTextOffset, lineColor);
		guiGraphics.drawString(font, label, labelX, padding, lineColor, false);
		guiGraphics.fill(rightLineStart, padding + lineToTextOffset, maxTooltipWidth - padding, padding + lineWidth + lineToTextOffset, lineColor);
	}

	@Override
	public int getHeight() {
		int textHeight = Minecraft.getInstance().font.lineHeight;
		return Math.max(textHeight, lineWidth) + (2*padding);
	}

	@Override
	public int getWidth(Font font) {
		int labelWidth = font.width(label);
		return labelWidth + (2 * padding) + (2 * 16); // Padding on both sides and line width on both sides
	}
}
