package com.davenonymous.pipegoggles.config;

import com.davenonymous.pipegoggles.PipeGoggles;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = PipeGoggles.MODID)
public class Config {
	public static final ModConfigSpec COMMON_SPEC;
	public static final ModConfigSpec CLIENT_SPEC;

	public static final Rules Rules;
	public static final Client Client;

	static {
		ModConfigSpec.Builder commonBuilder = new ModConfigSpec.Builder();
		Rules = new Rules(commonBuilder);
		COMMON_SPEC = commonBuilder.build();


		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
		Client = new Client(builder);
		CLIENT_SPEC = builder.build();

	}

	@SubscribeEvent
	static void onLoad(final ModConfigEvent event) {
		if(event.getConfig().getSpec() == COMMON_SPEC) {
			Rules.load();
		} else if(event.getConfig().getSpec() == CLIENT_SPEC) {
			Client.load();
		}
	}
}
