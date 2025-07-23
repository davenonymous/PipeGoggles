package com.davenonymous.pipegoggles.lib.gui;

import net.minecraft.resources.ResourceLocation;

public record GUISpriteInfo(ResourceLocation sprite, int width, int height) {
	public GUISpriteInfo(ResourceLocation sprite) {
		this(sprite, 16); // Default size if not specified
	}

	public GUISpriteInfo(ResourceLocation sprite, int size) {
		this(sprite, size, size); // Square size if only one dimension is specified
	}
}
