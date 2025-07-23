package com.davenonymous.pipegoggles.lib.gui.widgets;


import com.davenonymous.pipegoggles.lib.gui.event.MouseClickEvent;
import com.davenonymous.pipegoggles.lib.gui.event.MouseReleasedEvent;
import com.davenonymous.pipegoggles.lib.gui.event.WidgetEventResult;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;


public class WidgetGhostSlot extends WidgetItemStack {
	Function<ItemStack, Boolean> isValidItemStackFunc = (stack -> true);

	public WidgetGhostSlot(ItemStack stack, boolean drawSlot) {
		super(stack, drawSlot);

		this.addListener(
			MouseClickEvent.class, (event, widget) -> {
				if (!widget.enabled) {
					return WidgetEventResult.CONTINUE_PROCESSING;
				}


				ItemStack playerStack = getGUI().getContainer().getCarried().copy();
				playerStack.setCount(1);
				this.setValue(playerStack);
				return WidgetEventResult.CONTINUE_PROCESSING;
			}
		);

		this.addListener(
			MouseReleasedEvent.class, ((event, widget) -> {
				return WidgetEventResult.CONTINUE_PROCESSING;
			})
		);
	}

	public void setValueForced(ItemStack stack) {
		super.setValue(stack);
	}

	@Override
	public void setValue(ItemStack stack) {
		if(!isValidItemStack(stack)) {
			return;
		}

		super.setValue(stack);
	}

	public WidgetGhostSlot setIsValidItemStack(Function<ItemStack, Boolean> isValidItemStack) {
		this.isValidItemStackFunc = (stack -> stack.isEmpty() || isValidItemStack.apply(stack));
		return this;
	}

	public WidgetGhostSlot(ItemStack stack) {
		this(stack, true);
	}

	protected boolean isValidItemStack(ItemStack stack) {
		return isValidItemStackFunc == null ? true : isValidItemStackFunc.apply(stack);
	}
}
