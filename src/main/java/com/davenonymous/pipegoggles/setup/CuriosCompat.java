package com.davenonymous.pipegoggles.setup;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.items.IItemHandler;

public class CuriosCompat {
	public static final EntityCapability<IItemHandler, Void> CURIOS_INVENTORY =
		EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath("curios", "item_handler"), IItemHandler.class);
}
