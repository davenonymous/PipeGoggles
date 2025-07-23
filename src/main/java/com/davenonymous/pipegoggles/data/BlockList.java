package com.davenonymous.pipegoggles.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class BlockList extends ArrayList<Either<TagKey<Block>, ResourceKey<Block>>> {

	public static final Codec<List<Either<TagKey<Block>, ResourceKey<Block>>>> CODEC = Codec.list(BlockListEntry.ENTRY_CODEC);

	public BlockList(@NotNull Collection<? extends Either<TagKey<Block>, ResourceKey<Block>>> c) {
		super(c);
	}

	public static BlockList of(String... blockIds) {
		BlockList blockList = new BlockList(new ArrayList<>());

		for(String blockId : blockIds) {
			blockList.add(blockId);
		}
		return blockList;
	}

	public static BlockList ofDyes(String format) {
		BlockList blockList = new BlockList(new ArrayList<>());
		for(DyeColor dyeColor : DyeColor.values()) {
			String blockId = String.format(format, dyeColor.getName());
			blockList.add(blockId);
		}
		return blockList;
	}

	public BlockList add(TagKey<Block> block) {
		this.add(Either.left(block));
		return this;
	}

	public BlockList add(ResourceKey<Block> block) {
		this.add(Either.right(block));
		return this;
	}

	public BlockList add(Block block) {
		return add(block.builtInRegistryHolder().key());
	}

	public BlockList addTag(ResourceLocation tag) {
		return add(TagKey.create(Registries.BLOCK, tag));
	}

	public BlockList add(ResourceLocation block) {
		return add(ResourceKey.create(Registries.BLOCK, block));
	}

	public BlockList add(String blockId) {
		return add(ResourceLocation.parse(blockId));
	}


	public Set<Block> getAllBlocks(Registry<Block> registry) {
		Set<Block> blocks = new java.util.HashSet<>();
		for (Either<TagKey<Block>, ResourceKey<Block>> entry : this) {
			if (entry.left().isPresent()) {
				// If it's a tag, get all blocks in the tag
				TagKey<Block> tag = entry.left().get();
				registry.getTagOrEmpty(tag).forEach(blockHolder -> blocks.add(blockHolder.value()));
			} else if (entry.right().isPresent()) {
				// If it's a resource key, add the block directly
				ResourceKey<Block> resourceKey = entry.right().get();
				registry.getOptional(resourceKey).ifPresent(blocks::add);
			}
		}
		return blocks;
	}

	public boolean supports(BlockState state) {
		if (isEmpty()) {
			return false; // No blocks defined, cannot support
		}

		for (Either<TagKey<Block>, ResourceKey<Block>> entry : this) {
			if (entry.left().isPresent()) {
				// Check if the block state is in the tag
				if (state.is(entry.left().get())) {
					return true;
				}
			} else if (entry.right().isPresent()) {
				// Check if the block state matches the resource key
				if (state.is(entry.right().get())) {
					return true;
				}
			}
		}

		return false; // No matching block found
	}

	public List<Either<TagKey<Block>, ResourceKey<Block>>> asList() {
		return this;
	}

	static class BlockListEntry {
		private static final ResourceKey<Registry<Block>> registryKey = Registries.BLOCK;
		public static final Codec<Either<TagKey<Block>, ResourceKey<Block>>> ENTRY_CODEC = ExtraCodecs.TAG_OR_ELEMENT_ID.xmap((l) -> l.tag() ? Either.left(TagKey.create(registryKey, l.id())) : Either.right(ResourceKey.create(registryKey, l.id())), (e) -> (ExtraCodecs.TagOrElementLocation)e.map((t) -> new ExtraCodecs.TagOrElementLocation(t.location(), true), (r) -> new ExtraCodecs.TagOrElementLocation(r.location(), false)));

		Either<TagKey<Block>, ResourceKey<Block>> value;
	}
}
