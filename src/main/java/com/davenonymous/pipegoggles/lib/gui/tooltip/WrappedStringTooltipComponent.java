package com.davenonymous.pipegoggles.lib.gui.tooltip;

import com.davenonymous.pipegoggles.lib.gui.ColorHelper;
import com.davenonymous.pipegoggles.lib.gui.GUIHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public final class WrappedStringTooltipComponent implements SerializableTooltipComponent<WrappedStringTooltipComponent> {

	public static final StreamCodec<FriendlyByteBuf, WrappedStringTooltipComponent> CODEC = StreamCodec.composite(
		ByteBufCodecs.STRING_UTF8, WrappedStringTooltipComponent::message,
		ByteBufCodecs.INT, WrappedStringTooltipComponent::color,
		ByteBufCodecs.INT, WrappedStringTooltipComponent::maxWidth,
		WrappedStringTooltipComponent::new
	);
	public String message;
	public int color;
	public int maxWidth;

	public WrappedStringTooltipComponent(String message, int color, int maxWidth) {
		this.message = message;
		this.color = color;
		this.maxWidth = maxWidth;
	}

	private static int defaultMaxWidth() {
		return 240; //Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2;
	}

	public static WrappedStringTooltipComponent white(String message) {
		return white(message, defaultMaxWidth());
	}

	public static WrappedStringTooltipComponent white(String message, int maxWidth) {
		return new WrappedStringTooltipComponent(message, ChatFormatting.WHITE.getColor(), maxWidth);
	}

	public static WrappedStringTooltipComponent gray(String message) {
		return gray(message, defaultMaxWidth());
	}

	public static WrappedStringTooltipComponent gray(String message, int maxWidth) {
		return new WrappedStringTooltipComponent(message, ChatFormatting.GRAY.getColor(), maxWidth);
	}

	public static WrappedStringTooltipComponent yellow(String message) {
		return yellow(message, defaultMaxWidth());
	}

	public static WrappedStringTooltipComponent yellow(String message, int maxWidth) {
		return new WrappedStringTooltipComponent(message, ChatFormatting.YELLOW.getColor(), maxWidth);
	}

	public static WrappedStringTooltipComponent green(String message) {
		return new WrappedStringTooltipComponent(message, ColorHelper.COLOR_GREEN, defaultMaxWidth());
	}

	public static WrappedStringTooltipComponent orange(String message) {
		return new WrappedStringTooltipComponent(message, ColorHelper.COLOR_ORANGE, defaultMaxWidth());
	}

	public static WrappedStringTooltipComponent cyan(String message) {
		return new WrappedStringTooltipComponent(message, ColorHelper.COLOR_CYAN, defaultMaxWidth());
	}

	public static WrappedStringTooltipComponent red(String message) {
		return new WrappedStringTooltipComponent(message, ChatFormatting.RED.getColor(), defaultMaxWidth());
	}

	@Override
	public int getHeight() {
		return GUIHelper.wordWrapHeight(Minecraft.getInstance().font, FormattedText.of(message), maxWidth);
	}

	@Override
	public int getWidth(Font font) {
		return GUIHelper.longestWrappedLine(font, FormattedText.of(message), maxWidth);
	}

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
		GUIHelper.drawWordWrap(guiGraphics, font, FormattedText.of(message), x, y, maxWidth, color);
	}

	@Override
	public StreamCodec<FriendlyByteBuf, WrappedStringTooltipComponent> getCodec() {
		return CODEC;
	}

	public String message() {
		return message;
	}

	public int color() {
		return color;
	}

	public int maxWidth() {
		return maxWidth;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		if(obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		var that = (WrappedStringTooltipComponent) obj;
		return Objects.equals(this.message, that.message) &&
			this.color == that.color &&
			this.maxWidth == that.maxWidth;
	}

	@Override
	public int hashCode() {
		return Objects.hash(message, color, maxWidth);
	}

	@Override
	public String toString() {
		return "WrappedStringTooltipComponent[" +
			"message=" + message + ", " +
			"color=" + color + ", " +
			"maxWidth=" + maxWidth + ']';
	}

}
