package com.davenonymous.pipegoggles.setup;


import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.items.PipeGoggleContainer;
import com.davenonymous.pipegoggles.items.PipeGoggleScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@EventBusSubscriber(modid = PipeGoggles.MODID)
public class ModContainers {
	public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(Registries.MENU, PipeGoggles.MODID);

	public static final Supplier<MenuType<PipeGoggleContainer>> PIPE_GOGGLE_CONTAINER = CONTAINERS.register(
		"pipegoggles", resourceLocation -> IMenuTypeExtension.create(
			(i, inventory, registryFriendlyByteBuf) -> new PipeGoggleContainer(i, inventory, inventory.player)
		)
	);

	@SubscribeEvent
	public static void attachScreens(RegisterMenuScreensEvent event) {
		event.register(PIPE_GOGGLE_CONTAINER.get(), PipeGoggleScreen::new);
	}
}
