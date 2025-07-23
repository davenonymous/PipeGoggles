package com.davenonymous.pipegoggles.setup;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.data.GoggleEnergyStorage;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber(modid = PipeGoggles.MODID)
public class ModCapabilities {
	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerItem(Capabilities.EnergyStorage.ITEM,
			(stack, unused) -> new GoggleEnergyStorage(stack),
			ModItems.PIPE_GOGGLE_ITEM.get()
		);
	}
}
