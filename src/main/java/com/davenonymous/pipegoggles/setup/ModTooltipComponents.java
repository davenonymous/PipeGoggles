package com.davenonymous.pipegoggles.setup;

import com.davenonymous.pipegoggles.lib.gui.tooltip.*;
import com.davenonymous.pipegoggles.PipeGoggles;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

import java.util.function.Function;

@EventBusSubscriber(modid = PipeGoggles.MODID)
public class ModTooltipComponents {

	@SubscribeEvent
	public static void onTooltipRegister(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(ItemStackTooltipComponent.class, Function.identity());
		event.register(HBoxTooltipComponent.class, Function.identity());
		event.register(VBoxTooltipComponent.class, Function.identity());
		event.register(SpriteTooltipComponent.class, Function.identity());
		event.register(StringTooltipComponent.class, Function.identity());
		event.register(WrappedStringTooltipComponent.class, Function.identity());
		event.register(IngredientTooltipComponent.class, Function.identity());
		event.register(IngredientBoxTooltipComponent.class, Function.identity());
		event.register(TranslatableTooltipComponent.class, Function.identity());
		event.register(RedstoneModeTooltipComponent.class, Function.identity());
		event.register(ScrollableTooltipComponent.class, Function.identity());
		event.register(ColorDisplayTooltipComponent.class, Function.identity());
		event.register(LineSeparatorTooltipComponent.class, Function.identity());
		event.register(LabeledLineSeparatorTooltipComponent.class, Function.identity());
		event.register(TableTooltipComponent.class, Function.identity());
		event.register(LeftRightAlignedTooltipComponent.class, Function.identity());
		event.register(CenteredTooltipComponent.class, Function.identity());
		event.register(BackgroundTooltipComponent.class, Function.identity());
		event.register(NumberWithUnitTooltipComponent.class, Function.identity());
	}
}
