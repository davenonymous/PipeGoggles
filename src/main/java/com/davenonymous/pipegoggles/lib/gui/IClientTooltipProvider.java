package com.davenonymous.pipegoggles.lib.gui;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.util.List;

public interface IClientTooltipProvider {
	List<TooltipComponent> getClientTooltip();
}
