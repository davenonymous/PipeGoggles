package com.davenonymous.pipegoggles.datagen;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.datagen.client.DatagenItemModels;
import com.davenonymous.pipegoggles.datagen.client.DatagenTranslations;
import com.davenonymous.pipegoggles.datagen.server.DatagenBlockTags;
import com.davenonymous.pipegoggles.datagen.server.DatagenItemTags;
import com.davenonymous.pipegoggles.datagen.server.DatagenRecipes;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = PipeGoggles.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	private static void generateServerData(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		var blockTagsProvider = new DatagenBlockTags(generator, existingFileHelper);
		generator.addProvider(blockTagsProvider);
		generator.addProvider(new DatagenItemTags(generator, blockTagsProvider, existingFileHelper));
		generator.addProvider(new DatagenRecipes(generator));
	}

	private static void generateClientData(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		generator.addProvider(new DatagenItemModels(generator, existingFileHelper));
		generator.addProvider(new DatagenTranslations(generator, "en_us"));
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		var generator = event.getGenerator();
		if(event.includeServer()) {
			generateServerData(generator, event.getExistingFileHelper());
		}

		if(event.includeClient()) {
			generateClientData(generator, event.getExistingFileHelper());
		}
	}
}