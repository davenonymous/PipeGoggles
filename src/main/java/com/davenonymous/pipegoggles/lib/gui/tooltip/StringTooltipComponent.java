package com.davenonymous.pipegoggles.lib.gui.tooltip;

import com.davenonymous.pipegoggles.lib.gui.ColorHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Objects;

public class StringTooltipComponent implements SerializableTooltipComponent<StringTooltipComponent> {

	public static final StreamCodec<FriendlyByteBuf, StringTooltipComponent> CODEC = StreamCodec.composite(
		ByteBufCodecs.STRING_UTF8, StringTooltipComponent::message,
		ByteBufCodecs.INT, StringTooltipComponent::color,
		StringTooltipComponent::new
	);
	public String message;
	public int color;

	public StringTooltipComponent(String message, int color) {
		this.message = message;
		this.color = color;
	}

	public static StringTooltipComponent white(String message) {
		return new StringTooltipComponent(message, ChatFormatting.WHITE.getColor());
	}

	public static StringTooltipComponent gray(String message) {
		return new StringTooltipComponent(message, ChatFormatting.GRAY.getColor());
	}

	public static StringTooltipComponent chat(String message, ChatFormatting color) {
		return new StringTooltipComponent(message, color.getColor());
	}

	public static StringTooltipComponent green(String message) {
		return new StringTooltipComponent(message, ColorHelper.COLOR_GREEN);
	}

	public static StringTooltipComponent orange(String message) {
		return new StringTooltipComponent(message, ColorHelper.COLOR_ORANGE);
	}

	public static StringTooltipComponent cyan(String message) {
		return new StringTooltipComponent(message, ColorHelper.COLOR_CYAN);
	}

	@Override
	public int getHeight() {
		return 10;
	}

	@Override
	public int getWidth(Font font) {
		return font.width(message);
	}

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
		guiGraphics.drawString(font, message, x, y, color);
	}

	@Override
	public StreamCodec<FriendlyByteBuf, StringTooltipComponent> getCodec() {
		return CODEC;
	}

	public String message() {
		return message;
	}

	public int color() {
		return color;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		if(obj == null || obj.getClass() != this.getClass()) {
			return false;
		}
		var that = (StringTooltipComponent) obj;
		return Objects.equals(this.message, that.message) &&
			this.color == that.color;
	}

	@Override
	public int hashCode() {
		return Objects.hash(message, color);
	}

	@Override
	public String toString() {
		return "StringTooltipComponent[" +
			"message=" + message + ", " +
			"color=" + color + ']';
	}

}
