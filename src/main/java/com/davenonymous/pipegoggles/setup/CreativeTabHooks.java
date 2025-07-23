package com.davenonymous.pipegoggles.setup;

import com.davenonymous.pipegoggles.PipeGoggles;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = PipeGoggles.MODID)
public class CreativeTabHooks {
	@SubscribeEvent // on the mod event bus
	public static void buildContents(BuildCreativeModeTabContentsEvent event) {
		ItemStack goggleItem = new ItemStack(ModItems.PIPE_GOGGLE_ITEM.get());
		if(event.getTabKey() != CreativeModeTabs.TOOLS_AND_UTILITIES) {
			return;
		}

		event.accept(goggleItem);
	}
}
