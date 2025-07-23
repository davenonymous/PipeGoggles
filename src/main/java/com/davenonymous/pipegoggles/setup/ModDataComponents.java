package com.davenonymous.pipegoggles.setup;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.datacomponents.PipeGoggleDataComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
	public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, PipeGoggles.MODID);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<PipeGoggleDataComponent>> PIPEGOGGLES_COMPONENT = DATA_COMPONENTS.registerComponentType(
		"pipegoggles",
		builder -> builder
			.persistent(PipeGoggleDataComponent.CODEC)
			.networkSynchronized(PipeGoggleDataComponent.STREAM_CODEC)
	);

}
