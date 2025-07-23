package com.davenonymous.pipegoggles.setup;

import net.neoforged.bus.api.IEventBus;

public class Registration {
	public static void register(IEventBus modbus) {
		ModItems.ITEMS.register(modbus);
		ModContainers.CONTAINERS.register(modbus);
		ModDataComponents.DATA_COMPONENTS.register(modbus);

	}
}
