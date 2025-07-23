package com.davenonymous.pipegoggles.setup;

import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ModUtils {
	public static Optional<String> getModId(ItemStack stack) {
		if (stack.isEmpty()) {
			return Optional.empty();
		}

		var item = stack.getItem();
		var registryKey = item.builtInRegistryHolder().key();

		var modId = registryKey.location().getNamespace();
		if (modId.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(modId);
	}
}
