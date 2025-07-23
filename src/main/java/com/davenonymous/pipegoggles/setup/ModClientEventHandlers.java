package com.davenonymous.pipegoggles.setup;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.render.BoxRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = PipeGoggles.MODID, value = Dist.CLIENT)
public class ModClientEventHandlers {
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		PipeGoggles.CONTAINER.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}


	@SubscribeEvent
	public static void onRenderLast(RenderLevelStageEvent event) {
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
			BoxRenderer.onRenderLast(event);
		}
	}
}
