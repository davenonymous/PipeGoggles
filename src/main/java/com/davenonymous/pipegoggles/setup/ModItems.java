package com.davenonymous.pipegoggles.setup;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.items.PipeGoggleItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PipeGoggles.MODID);

	public static final DeferredItem<PipeGoggleItem> PIPE_GOGGLE_ITEM = ITEMS
		.register("pipegoggles", () -> new PipeGoggleItem(new Item.Properties()));
}
