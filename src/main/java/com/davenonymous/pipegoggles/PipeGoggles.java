package com.davenonymous.pipegoggles;

import com.davenonymous.pipegoggles.config.Config;
import com.davenonymous.pipegoggles.setup.ClassInheritances;
import com.davenonymous.pipegoggles.setup.Registration;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(PipeGoggles.MODID)
public class PipeGoggles {

	public static final String MODID = "pipegoggles";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static ModContainer CONTAINER;

	public static ResourceLocation resource(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}
	public static ClassInheritances CLASS_INHERITANCES;

	public PipeGoggles(IEventBus modEventBus, ModContainer modContainer)
	{
		CONTAINER = modContainer;
		Registration.register(modEventBus);

		modContainer.registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
		modContainer.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);

		CLASS_INHERITANCES = new ClassInheritances();
	}
}
