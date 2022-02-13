package com.davenonymous.pipegoggles.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Arrays;
import java.util.List;

public class CommonConfig {
	private static final Builder COMMON_BUILDER = new Builder();

	public static ForgeConfigSpec COMMON_CONFIG;


	public static void register() {

		COMMON_CONFIG = COMMON_BUILDER.build();

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
	}
}