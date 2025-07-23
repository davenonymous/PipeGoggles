package com.davenonymous.pipegoggles.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ItemList extends ArrayList<Either<TagKey<Item>, ResourceKey<Item>>> {

	public static final Codec<List<Either<TagKey<Item>, ResourceKey<Item>>>> CODEC = Codec.list(BlockListEntry.ENTRY_CODEC);

	public ItemList(@NotNull Collection<? extends Either<TagKey<Item>, ResourceKey<Item>>> c) {
		super(c);
	}

	public static ItemList of(String... itemIds) {
		ItemList itemList = new ItemList(new ArrayList<>());

		for(String itemId : itemIds) {
			itemList.add(itemId);
		}
		return itemList;
	}

	public ItemList add(TagKey<Item> item) {
		this.add(Either.left(item));
		return this;
	}

	public ItemList add(ResourceKey<Item> item) {
		this.add(Either.right(item));
		return this;
	}

	public ItemList add(Item item) {
		return add(item.builtInRegistryHolder().key());
	}

	public ItemList addTag(ResourceLocation tag) {
		return add(TagKey.create(Registries.ITEM, tag));
	}

	public ItemList add(ResourceLocation item) {
		return add(ResourceKey.create(Registries.ITEM, item));
	}

	public ItemList add(String itemId) {
		if(itemId.startsWith("#")) {
			// If the itemId starts with a #, it's a tag
			return addTag(ResourceLocation.parse(itemId.substring(1)));
		}

		return add(ResourceLocation.parse(itemId));
	}


	public Set<Item> getAllItems(Registry<Item> registry) {
		Set<Item> items = new java.util.HashSet<>();
		for (Either<TagKey<Item>, ResourceKey<Item>> entry : this) {
			if (entry.left().isPresent()) {
				// If it's a tag, get all items in the tag
				TagKey<Item> tag = entry.left().get();
				registry.getTagOrEmpty(tag).forEach(itemHolder -> items.add(itemHolder.value()));
			} else if (entry.right().isPresent()) {
				// If it's a resource key, add the item directly
				ResourceKey<Item> resourceKey = entry.right().get();
				registry.getOptional(resourceKey).ifPresent(items::add);
			}
		}
		return items;
	}

	public boolean supports(Item item) {
		if (isEmpty()) {
			return false; // No items defined, cannot support
		}

		for (Either<TagKey<Item>, ResourceKey<Item>> entry : this) {
			if (entry.left().isPresent()) {
				// Check if the item state is in the tag
				TagKey<Item> tag = entry.left().get();
				if(item.builtInRegistryHolder().is(tag)) {
					return true; // Item matches the tag
				}
			} else if (entry.right().isPresent()) {
				// Check if the item state matches the resource key
				if (item.builtInRegistryHolder().getKey().equals(entry.right().get())) {
					return true;
				}
			}
		}

		return false; // No matching item found
	}

	public List<Either<TagKey<Item>, ResourceKey<Item>>> asList() {
		return this;
	}

	static class BlockListEntry {
		private static final ResourceKey<Registry<Item>> registryKey = Registries.ITEM;
		public static final Codec<Either<TagKey<Item>, ResourceKey<Item>>> ENTRY_CODEC = ExtraCodecs.TAG_OR_ELEMENT_ID.xmap((l) -> l.tag() ? Either.left(TagKey.create(registryKey, l.id())) : Either.right(ResourceKey.create(registryKey, l.id())), (e) -> (ExtraCodecs.TagOrElementLocation)e.map((t) -> new ExtraCodecs.TagOrElementLocation(t.location(), true), (r) -> new ExtraCodecs.TagOrElementLocation(r.location(), false)));

		Either<TagKey<Item>, ResourceKey<Item>> value;
	}
}
