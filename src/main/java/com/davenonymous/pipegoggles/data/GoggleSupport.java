package com.davenonymous.pipegoggles.data;

import com.davenonymous.pipegoggles.data.cache.GoggleSupportCache;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModList;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public record GoggleSupport(String modId, EnumBoxOptimizationStrategy strategy, BlockList blocks, ItemList items, boolean useBlockItems) {

	public static final MapCodec<GoggleSupport> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
		return instance.group(
			Codec.STRING.fieldOf("mod").forGetter(GoggleSupport::modId),
			EnumBoxOptimizationStrategy.CODEC.optionalFieldOf("strategy", EnumBoxOptimizationStrategy.REMOVE_DUPLICATE_LINES).forGetter(GoggleSupport::strategy),
			BlockList.CODEC.fieldOf("blocks").xmap(BlockList::new, BlockList::asList).forGetter(GoggleSupport::blocks),
			ItemList.CODEC.optionalFieldOf("items", List.of()).xmap(ItemList::new, ItemList::asList).forGetter(GoggleSupport::items),
			Codec.BOOL.optionalFieldOf("useBlockItems", true).forGetter(GoggleSupport::useBlockItems)
		).apply(instance, GoggleSupport::new);
	});

	public Set<Item> getAllItems(Registry<Item> itemRegistry) {
		return items.getAllItems(itemRegistry);
	}

	public String getModName() {
		var modFile = ModList.get().getModFileById(modId());
		if(modFile == null) {
			return "?";
		}
		if(modFile.getMods().isEmpty()) {
			return modFile.getFile().getFileName();
		}

		return modFile.getMods().getFirst().getDisplayName();
	}

	public boolean supportsBlock(BlockState state) {
		return blocks().supports(state);
	}

	public boolean isTriggerItem(ItemStack stack) {
		if(stack.getItem() instanceof BlockItem blockItem) {
			var block = blockItem.getBlock();
			if(blocks().supports(block.defaultBlockState())) {
				return true;
			}
		}

		return GoggleSupportCache.ITEMS_BY_SUPPORT.get(this).contains(stack.getItem());
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof GoggleSupport support)) {
			return false;
		}
		return
			useBlockItems() == support.useBlockItems() &&
				Objects.equals(modId(), support.modId()) &&
				Objects.equals(items(), support.items()) &&
				Objects.equals(blocks(), support.blocks()) &&
				strategy() == support.strategy();
	}

	@Override
	public int hashCode() {
		return Objects.hash(modId(), strategy(), blocks(), items(), useBlockItems());
	}
}
