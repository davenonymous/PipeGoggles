package com.davenonymous.pipegoggles.setup;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.gui.PipeGogglesScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = PipeGoggles.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
	public static void init(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(Registration.PIPEGOGGLES_CONTAINER.get(), PipeGogglesScreen::new);
		});
	}

	@SubscribeEvent
	public static void onModelRegistryEvent(ModelRegistryEvent event) {
	}
}