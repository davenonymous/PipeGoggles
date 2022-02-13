package com.davenonymous.pipegoggles.setup;


import com.davenonymous.pipegoggles.network.Networking;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ModSetup {
	public static void init(FMLCommonSetupEvent event) {
		Networking.registerMessages();
	}
}