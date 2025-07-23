package com.davenonymous.pipegoggles.render;

import com.davenonymous.pipegoggles.config.Client;
import com.davenonymous.pipegoggles.data.cache.BoxOptimizer;
import com.davenonymous.pipegoggles.lib.gui.GUIHelper;
import com.davenonymous.pipegoggles.lib.gui.event.MouseClickEvent;
import com.davenonymous.pipegoggles.lib.gui.event.WidgetEventResult;
import com.davenonymous.pipegoggles.lib.gui.tooltip.StringTooltipComponent;
import com.davenonymous.pipegoggles.lib.gui.tooltip.WrappedStringTooltipComponent;
import com.davenonymous.pipegoggles.lib.gui.widgets.WidgetGhostSlot;
import com.davenonymous.pipegoggles.networking.RemoveStackForColorPacket;
import com.davenonymous.pipegoggles.networking.SetEnabledForColorPacket;
import com.davenonymous.pipegoggles.networking.SetStackForColorPacket;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.PacketDistributor;

public class WidgetColoredGhostSlot extends WidgetGhostSlot {
	private int colorA;
	private DyeColor dyeColor;

	public WidgetColoredGhostSlot(DyeColor color) {
		super(ItemStack.EMPTY, true);
		this.setSize(18, 18);
		this.colorA = GUIHelper.brighten(color.getTextColor(), 1.1f);
		this.dyeColor = color;

		this.removeEventListeners(MouseClickEvent.class);
		this.addListener(
			MouseClickEvent.class, (event, widget) -> {
				if (event.button == 1) {
					this.setValue(ItemStack.EMPTY);
					PacketDistributor.sendToServer(new RemoveStackForColorPacket(color));
					this.updateTooltips();
					return WidgetEventResult.HANDLED;
				}

				if (event.button != 0) {
					return WidgetEventResult.CONTINUE_PROCESSING; // Only handle left mouse button clicks
				}

				ItemStack playerStack = getGUI().getContainer().getCarried();
				if(playerStack.isEmpty() || ItemStack.isSameItemSameComponents(playerStack, this.getValue())) {
					if(this.grayOut()) {
						this.setGrayOut(false);
						PacketDistributor.sendToServer(new SetEnabledForColorPacket(color, true));
					} else {
						this.setGrayOut(true);
						PacketDistributor.sendToServer(new SetEnabledForColorPacket(color, false));
					}
				} else {
					ItemStack newStack = playerStack.copy();
					newStack.setCount(1); // Only allow one item in the slot
					if(this.isValidItemStack(newStack)) {
						this.setValue(newStack);
						this.setGrayOut(false);
						PacketDistributor.sendToServer(new SetStackForColorPacket(color, newStack));
						PacketDistributor.sendToServer(new SetEnabledForColorPacket(color, true));
					}
				}
				this.updateTooltips();
				return WidgetEventResult.HANDLED;
			}
		);

		this.updateTooltips();
	}

	public void updateTooltips() {
		if(dyeColor == null) {
			return;
		}

		ItemStack stack = this.getValue();
		String dyeColorName = I18n.get("item.minecraft.firework_star." + dyeColor.getName());
		this.setTooltipElements(new StringTooltipComponent(dyeColorName, dyeColor == DyeColor.BLACK ? DyeColor.WHITE.getTextColor() : dyeColor.getTextColor()));
		if(!stack.isEmpty()) {
			String modName = stack.getItem().getCreatorModId(stack);
			var modFile = ModList.get().getModFileById(modName);
			if(modFile == null) {
				this.addTooltipElement(StringTooltipComponent.gray(I18n.get(stack.getDescriptionId())));
			} else {
				if(!modFile.getMods().isEmpty()) {
					this.addTooltipElement(StringTooltipComponent.white(modFile.getMods().getFirst().getDisplayName()));
				}
			}
		}

		if(getValue() != null && !getValue().isEmpty()) {
			if(!grayOut()) {
				this.addTooltipElement(StringTooltipComponent.orange(I18n.get("pipegoggles.message.click_to_disable")));
			} else {
				this.addTooltipElement(StringTooltipComponent.green(I18n.get("pipegoggles.message.click_to_enable")));
			}
			this.addTooltipElement(StringTooltipComponent.chat(I18n.get("pipegoggles.message.right_click_to_remove"), ChatFormatting.RED));
		} else if(Client.showHelpTooltips) {
			this.addTooltipElement(WrappedStringTooltipComponent.gray(I18n.get("pipegoggles.help.insert_items")));
		}

	}

	public void setValue(ItemStack stack) {
		super.setValue(stack);
		updateTooltips();
	}

	@Override
	protected void drawSlot(GuiGraphics pGuiGraphics, Screen screen) {
		GUIHelper.setShaderColor(colorA);
		super.drawSlot(pGuiGraphics, screen);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
