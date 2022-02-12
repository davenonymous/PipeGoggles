package com.davenonymous.pipegoggles.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ClientConfig {
	private static final Builder CLIENT_BUILDER = new Builder();

	public static ForgeConfigSpec CLIENT_CONFIG;

	public static void register() {

		CLIENT_CONFIG = CLIENT_BUILDER.build();

		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG);
	}
}