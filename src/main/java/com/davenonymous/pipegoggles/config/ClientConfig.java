package com.davenonymous.pipegoggles.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Arrays;
import java.util.List;

public class ClientConfig {
	private static final Builder CLIENT_BUILDER = new Builder();

	public static ForgeConfigSpec CLIENT_CONFIG;
	public static ForgeConfigSpec.ConfigValue<List<Integer>> RANGE_CHOICES;
	public static ForgeConfigSpec.DoubleValue DRAW_LINE_WIDTH;
	public static ForgeConfigSpec.IntValue CACHE_TTL;
	public static ForgeConfigSpec.ConfigValue<List<String>> LINE_COLORS;

	public static void register() {
		DRAW_LINE_WIDTH = CLIENT_BUILDER.comment("How thick the lines should be drawn").defineInRange("lineWidth", 1.5d, 0.1d, 4.0d);
		CACHE_TTL = CLIENT_BUILDER.comment("How long to cache nearby pipes").defineInRange("cacheTTL", 20, 0, 1000);
		LINE_COLORS = CLIENT_BUILDER.comment("Color of the pipe outlines (RGBA in hex)").define("lineColors", Arrays.asList("FF000080", "00FF0080", "0000FF80", "FFFF0080"));

		RANGE_CHOICES = CLIENT_BUILDER
				.comment("List of valid range values. Please note that high values can have significant impact on FPS!")
				.define("validRanges", Arrays.asList(4, 8, 16));

		CLIENT_CONFIG = CLIENT_BUILDER.build();

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG);
	}
}