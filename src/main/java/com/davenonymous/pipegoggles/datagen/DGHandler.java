package com.davenonymous.pipegoggles.datagen;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.setup.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = PipeGoggles.MODID)
public class DGHandler {
	@SuppressWarnings("ConstantConditions")
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();


		var dgRegistries = new DGRegistries();
		event.createDatapackRegistryObjects(dgRegistries, dgRegistries.conditions, Set.of(PipeGoggles.MODID));

		generator.addProvider(event.includeServer(), new DGRecipes(output, lookupProvider));
		generator.addProvider(event.includeClient(), new DGTranslations(output, PipeGoggles.MODID, "en_us"));
		generator.addProvider(event.includeServer(), new DGCurios(PipeGoggles.MODID, output, existingFileHelper, lookupProvider));
	}
}
