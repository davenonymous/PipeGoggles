package com.davenonymous.pipegoggles.cache;

import com.davenonymous.pipegoggles.PipeGoggles;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChunkEvents {

	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load event) {
		var world = event.getWorld();
		var chunk = event.getChunk();
		if(!world.isClientSide()) {
			return;
		}

		//PipeGoggles.LOGGER.info("Whoop whoop");
	}
}