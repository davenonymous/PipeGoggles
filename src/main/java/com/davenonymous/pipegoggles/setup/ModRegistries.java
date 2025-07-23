package com.davenonymous.pipegoggles.setup;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.data.GoggleSupport;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = PipeGoggles.MODID)
public class ModRegistries {
	public static final ResourceKey<Registry<GoggleSupport>> GOGGLE_SUPPORT_REGISTRY_KEY = ResourceKey.createRegistryKey(PipeGoggles.resource("mods"));

	@SubscribeEvent
	static void newRegistry(DataPackRegistryEvent.NewRegistry event) {
		event.dataPackRegistry(GOGGLE_SUPPORT_REGISTRY_KEY, GoggleSupport.CODEC.codec(), GoggleSupport.CODEC.codec());
	}
}
