package com.davenonymous.pipegoggles.data.cache;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.data.GoggleSupport;
import com.davenonymous.pipegoggles.setup.ModRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;

import java.util.*;

@EventBusSubscriber(modid = PipeGoggles.MODID)
public class GoggleSupportCache {
	public static final Map<Block, GoggleSupport> SUPPORT_BY_BLOCK = new HashMap<>();
	public static final Map<Item, GoggleSupport> SUPPORT_BY_ITEM_TRIGGERS = new HashMap<>();
	public static final Map<GoggleSupport, List<Block>> BLOCKS_BY_SUPPORT = new HashMap<>();
	public static final Map<GoggleSupport, List<Item>> ITEMS_BY_SUPPORT = new HashMap<>();
	public static final Map<GoggleSupport, List<Class<?>>> BLOCK_CLASSES_BY_SUPPORT = new HashMap<>();

	@SubscribeEvent
	public static void dataMapsUpdated(DataMapsUpdatedEvent event) {
		event.ifRegistry(Registries.BLOCK, blockRegistry -> {
			var optRegistry = event.getRegistries().registry(ModRegistries.GOGGLE_SUPPORT_REGISTRY_KEY);
			if(optRegistry.isEmpty()) {
				PipeGoggles.LOGGER.warn("No GoggleSupport registry found. Goggles will not work with any blocks.");
				return;
			}

			SUPPORT_BY_BLOCK.clear();
			SUPPORT_BY_ITEM_TRIGGERS.clear();
			BLOCKS_BY_SUPPORT.clear();
			ITEMS_BY_SUPPORT.clear();
			BLOCK_CLASSES_BY_SUPPORT.clear();

			for(var entry : optRegistry.get().entrySet()) {
				ResourceKey<GoggleSupport> key = entry.getKey();
				String group = key.location().getPath();
				GoggleSupport support = entry.getValue();

				int blockCount = 0;
				int triggerItemCount = 0;

				for (Block block : support.blocks().getAllBlocks(blockRegistry)) {
					SUPPORT_BY_BLOCK.put(block, support);
					BLOCKS_BY_SUPPORT.computeIfAbsent(support, k -> new ArrayList<>()).add(block);
					blockCount++;

					if(support.useBlockItems()) {
						// If the support uses block items, we also register the block item as a trigger
						Item blockItem = block.asItem();
						if (blockItem != null) {
							SUPPORT_BY_ITEM_TRIGGERS.put(blockItem, support);
							ITEMS_BY_SUPPORT.computeIfAbsent(support, k -> new ArrayList<>()).add(blockItem);
							triggerItemCount++;
						}
					}
				}

				for (Item item : support.getAllItems(event.getRegistries().registry(Registries.ITEM).get())) {
					SUPPORT_BY_ITEM_TRIGGERS.put(item, support);
					ITEMS_BY_SUPPORT.computeIfAbsent(support, k -> new ArrayList<>()).add(item);
					triggerItemCount++;
				}

				PipeGoggles.LOGGER.info("Group '{}' has {} block{} and {} item{} with goggle support", group, blockCount, blockCount == 1 ? "" : "s", triggerItemCount, triggerItemCount == 1 ? "" : "s");
			}
		});
	}

	public static Optional<GoggleSupport> getSupportFor(ItemStack stack) {
		if(!SUPPORT_BY_ITEM_TRIGGERS.containsKey(stack.getItem())) {
			return Optional.empty(); // No support for this item
		}

		var support = SUPPORT_BY_ITEM_TRIGGERS.get(stack.getItem());
		return Optional.of(support);
	}

	public static boolean isSupportedMod(ItemStack stack) {
		return getSupportFor(stack).isPresent();
	}
}
