package com.davenonymous.pipegoggles.items;


import com.davenonymous.pipegoggles.config.Client;
import com.davenonymous.pipegoggles.config.Rules;
import com.davenonymous.pipegoggles.data.EnergyCosts;
import com.davenonymous.pipegoggles.data.EnumGoggleMode;
import com.davenonymous.pipegoggles.data.cache.GoggleSupportCache;
import com.davenonymous.pipegoggles.datacomponents.PipeGoggleDataComponent;
import com.davenonymous.pipegoggles.lib.gui.ColorHelper;
import com.davenonymous.pipegoggles.lib.gui.GUI;
import com.davenonymous.pipegoggles.lib.gui.Icons;
import com.davenonymous.pipegoggles.lib.gui.WidgetContainerScreen;
import com.davenonymous.pipegoggles.lib.gui.event.GuiDataUpdatedEvent;
import com.davenonymous.pipegoggles.lib.gui.event.ValueChangedEvent;
import com.davenonymous.pipegoggles.lib.gui.event.WidgetEventResult;
import com.davenonymous.pipegoggles.lib.gui.tooltip.*;
import com.davenonymous.pipegoggles.lib.gui.widgets.WidgetIconSelect;
import com.davenonymous.pipegoggles.lib.gui.widgets.WidgetLabel;
import com.davenonymous.pipegoggles.lib.gui.widgets.WidgetSpriteSelect;
import com.davenonymous.pipegoggles.networking.SetAlphaPacket;
import com.davenonymous.pipegoggles.networking.SetLineWidthPacket;
import com.davenonymous.pipegoggles.networking.SetModePacket;
import com.davenonymous.pipegoggles.networking.SetRangePacket;
import com.davenonymous.pipegoggles.render.WidgetColoredGhostSlot;
import com.davenonymous.pipegoggles.render.WidgetSelectBar;
import com.davenonymous.pipegoggles.render.WidgetTank;
import com.davenonymous.pipegoggles.setup.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;

public class PipeGoggleScreen extends WidgetContainerScreen<PipeGoggleContainer> {
	public PipeGoggleScreen(PipeGoggleContainer container, Inventory inv, Component name) {
		super(container, inv, name);
	}

	WidgetIconSelect<Integer> alphaSelect;
	WidgetIconSelect<Integer> lineWidthSelect;
	WidgetIconSelect<EnumGoggleMode> enabledCheckBox;
	WidgetSelectBar<Integer> rangeSelect;
	WidgetTank tank;

	@Override
	protected GUI createGUI() {
		GUI gui = new GUI(0, 0, PipeGoggleContainer.WIDTH, PipeGoggleContainer.HEIGHT);
		gui.setContainer(this.menu);

		var label = new WidgetLabel(I18n.get("container.pipegoggles.pipegoggles"));
		label.setTextColor(0xFF3F3F3F);
		label.setPosition(8, 6);
		gui.add(label);

		var goggles = Minecraft.getInstance().player.getMainHandItem();
		if (!(goggles.getItem() instanceof PipeGoggleItem)) {
			// Minecraft.getInstance().popGuiLayer();
			return gui; // No goggles in hand, no GUI
		}

		PipeGoggleDataComponent dataComponent = goggles.get(ModDataComponents.PIPEGOGGLES_COMPONENT);
		if (dataComponent == null) {
			dataComponent = new PipeGoggleDataComponent();
		}

		int xOffset = 8;
		int yOffset = 18;

		lineWidthSelect = new WidgetIconSelect<>();
		lineWidthSelect.setSize(13, 12);
		lineWidthSelect.setTextureSize(13, 12);
		lineWidthSelect.addChoiceWithSprite(1, new WidgetIconSelect.IconData(Icons.lineWidth1, 13, 12));
		lineWidthSelect.addChoiceWithSprite(2, new WidgetIconSelect.IconData(Icons.lineWidth2, 13, 12));
		lineWidthSelect.addChoiceWithSprite(3, new WidgetIconSelect.IconData(Icons.lineWidth3, 13, 12));
		lineWidthSelect.setValue(dataComponent.lineWidth());
		lineWidthSelect.setPosition(gui.width - 58, 4);
		lineWidthSelect.addListener(ValueChangedEvent.class, (event, widget) -> {
			if(!(event.newValue instanceof Integer newLineWidth)) {
				return WidgetEventResult.CONTINUE_PROCESSING;
			}

			updateTooltips();
			PacketDistributor.sendToServer(new SetLineWidthPacket(newLineWidth));
			return WidgetEventResult.HANDLED;
		});
		gui.add(lineWidthSelect);

		alphaSelect = new WidgetIconSelect<>();
		alphaSelect.setSize(13, 12);
		alphaSelect.setTextureSize(13, 12);
		alphaSelect.addChoiceWithSprite((int)(0.2f * 255), new WidgetIconSelect.IconData(Icons.alpha20, 13, 12));
		alphaSelect.addChoiceWithSprite((int)(0.4f * 255), new WidgetIconSelect.IconData(Icons.alpha40, 13, 12));
		alphaSelect.addChoiceWithSprite((int)(0.6f * 255), new WidgetIconSelect.IconData(Icons.alpha60, 13, 12));
		alphaSelect.addChoiceWithSprite((int)(0.8f * 255), new WidgetIconSelect.IconData(Icons.alpha80, 13, 12));
		alphaSelect.addChoiceWithSprite(255, new WidgetIconSelect.IconData(Icons.alpha100, 13, 12));
		alphaSelect.setValue(dataComponent.alpha());
		alphaSelect.setPosition(gui.width - 44, 4);
		alphaSelect.addListener(ValueChangedEvent.class, (event, widget) -> {
			if(!(event.newValue instanceof Integer newAlpha)) {
				return WidgetEventResult.CONTINUE_PROCESSING;
			}

			updateTooltips();
			PacketDistributor.sendToServer(new SetAlphaPacket(newAlpha));
			return WidgetEventResult.HANDLED;
		});
		gui.add(alphaSelect);

		enabledCheckBox = new WidgetIconSelect<>();
		enabledCheckBox.setSize(20, 12);
		enabledCheckBox.setTextureSize(20, 12);
		enabledCheckBox.addChoiceWithSprite(
			EnumGoggleMode.OFF,
			new WidgetIconSelect.IconData(Icons.toggleOff, 20, 12)
		);
		enabledCheckBox.addChoiceWithSprite(
			EnumGoggleMode.ON,
			new WidgetIconSelect.IconData(Icons.toggleOn, 20, 12)
		);
		enabledCheckBox.addChoiceWithSprite(
			EnumGoggleMode.SMART,
			new WidgetIconSelect.IconData(Icons.toggleAuto, 20, 12)
		);

		enabledCheckBox.setValue(dataComponent.goggleMode());
		enabledCheckBox.setPosition(gui.width - 26, 4);
		enabledCheckBox.addListener(ValueChangedEvent.class, (event, widget) -> {
			if(!(event.newValue instanceof EnumGoggleMode newMode)) {
				return WidgetEventResult.CONTINUE_PROCESSING;
			}

			updateTooltips();
			PacketDistributor.sendToServer(new SetModePacket(newMode));
			return WidgetEventResult.HANDLED;
		});
		gui.add(enabledCheckBox);


		int maxCols = 8;
		int col = 0;
		for(DyeColor color : DyeColor.values()) {
			var ghostSlot = new WidgetColoredGhostSlot(color);
			ghostSlot.setIsValidItemStack(stack -> {
				if(!GoggleSupportCache.isSupportedMod(stack)) {
					return false; // Not a supported item
				}

				var goggle = Minecraft.getInstance().player.getMainHandItem();
				PipeGoggleDataComponent data = PipeGoggleItem.data(goggle);
				Optional<DyeColor> optSlotColor = data.getColorForStack(stack);
				return optSlotColor.isEmpty();
			});

			ghostSlot.setGrayOut(!dataComponent.isColorEnabled(color));
			ghostSlot.addListener(ValueChangedEvent.class, (event, widget) -> {
				var goggle = Minecraft.getInstance().player.getMainHandItem();
				PipeGoggleDataComponent data = PipeGoggleItem.data(goggle);
				if(data.getStackForColor(color).isPresent()) {
					if(data.isColorEnabled(color)) {
						ghostSlot.addTooltipElement(StringTooltipComponent.orange(I18n.get("pipegoggles.message.click_to_disable")));
					} else {
						ghostSlot.addTooltipElement(StringTooltipComponent.green(I18n.get("pipegoggles.message.click_to_enable")));
					}
					ghostSlot.addTooltipElement(StringTooltipComponent.chat(I18n.get("pipegoggles.message.right_click_to_remove"), ChatFormatting.RED));
				} else if (Client.showHelpTooltips) {
					ghostSlot.addTooltipElement(StringTooltipComponent.gray(I18n.get("pipegoggles.help.insert_items")));
				}
				return WidgetEventResult.CONTINUE_PROCESSING;
			});
			if(dataComponent.getStackForColor(color).isPresent()) {
				ghostSlot.setValueForced(dataComponent.getStackForColor(color).get());
				ghostSlot.updateTooltips();
			}
			ghostSlot.setPosition(xOffset, yOffset);
			gui.add(ghostSlot);

			col++;
			xOffset += 18;
			if(col >= maxCols) {
				col = 0;
				xOffset = 8;
				yOffset += 18;
			}
		}

		rangeSelect = new WidgetSelectBar<Integer>();
		rangeSelect.setPosition(8, yOffset + 2);
		rangeSelect.setSize(gui.width-16-18, 22);
		rangeSelect.addChoice(Rules.rangeOptions);
		rangeSelect.setPositionProvider(integer -> (integer-Rules.minRange) / (float)(Rules.maxRange - Rules.minRange));
		rangeSelect.setValue((int)dataComponent.range());
		rangeSelect.addListener(ValueChangedEvent.class, (event, widget) -> {
			if(!(event.newValue instanceof Integer newRange)) {
				return WidgetEventResult.CONTINUE_PROCESSING;
			}

			PacketDistributor.sendToServer(new SetRangePacket(newRange));
			return WidgetEventResult.HANDLED;
		});

		gui.add(rangeSelect);

		tank = new WidgetTank((long)Rules.maxEnergy);
		tank.setSize(12, 60);
		tank.setPosition(gui.width - 18, 17);
		tank.setValue(dataComponent.storedEnergy());
		tank.setVisible(Rules.requireEnergy);
		gui.add(tank);

		gui.addListener(GuiDataUpdatedEvent.class, (event, widget) -> {
			// Update the tank value when the data is updated
			var goggle = Minecraft.getInstance().player.getMainHandItem();
			PipeGoggleDataComponent data = goggle.get(ModDataComponents.PIPEGOGGLES_COMPONENT);
			tank.setValue(data.storedEnergy());

			updateTooltips();
			return WidgetEventResult.CONTINUE_PROCESSING;
		});

		this.fireDataUpdateEvent();

		return gui;
	}

	public void updateTooltips() {
		var goggle = Minecraft.getInstance().player.getMainHandItem();
		PipeGoggleDataComponent data = goggle.get(ModDataComponents.PIPEGOGGLES_COMPONENT);

		// Enabled CheckBox
		var newMode = enabledCheckBox.getValue();
		if(newMode == EnumGoggleMode.ON) {
			enabledCheckBox.setTooltipElements(WrappedStringTooltipComponent.orange(I18n.get("pipegoggles.message.always_on")));
		} else if(newMode == EnumGoggleMode.OFF) {
			enabledCheckBox.setTooltipElements(WrappedStringTooltipComponent.red(I18n.get("pipegoggles.message.always_off")));
		} else {
			enabledCheckBox.setTooltipElements(WrappedStringTooltipComponent.green(I18n.get("pipegoggles.message.auto_mode")));
			if(Client.autoModeRequiresCrouching) {
				enabledCheckBox.addTooltipElement(StringTooltipComponent.gray(I18n.get("pipegoggles.message.auto_mode_crouch")));
			}
		}

		// Range Select
		if(Client.showHelpTooltips) {
			rangeSelect.setTooltipElements(StringTooltipComponent.gray(I18n.get("pipegoggles.help.set_range")));
		}

		var alphaTooltip = new HBoxTooltipComponent(
			StringTooltipComponent.white(I18n.get("pipegoggles.message.opacity")),
			StringTooltipComponent.gray(String.format("%.0f%%", alphaSelect.getValue() / 255.0f * 100))
		);

		alphaSelect.setTooltipElements(alphaTooltip);

		var lineWidthTooltip = new HBoxTooltipComponent(
			StringTooltipComponent.white(I18n.get("pipegoggles.message.line_width")),
			StringTooltipComponent.gray(String.valueOf(lineWidthSelect.getValue()))
		);
		lineWidthSelect.setTooltipElements(lineWidthTooltip);

		// Tank
		int color = ColorHelper.COLOR_GREEN;
		if(tank.getValue() < Rules.maxEnergy * 0.2) {
			color = ChatFormatting.RED.getColor();
		} else if(tank.getValue() < Rules.maxEnergy * 0.5) {
			color = ColorHelper.COLOR_ORANGE;
		}
		tank.setTooltipElements(
			StringTooltipComponent.white(I18n.get("pipegoggles.message.stored_energy")),
			new HBoxTooltipComponent(
				new NumberWithUnitTooltipComponent(tank.getValue(), color),
				StringTooltipComponent.gray("/"),
				new NumberWithUnitTooltipComponent(Rules.maxEnergy, ColorHelper.COLOR_GREEN),
				StringTooltipComponent.gray(" FE")
			).setPadding(0)
		);

		var costs = EnergyCosts.getCost(data);
		if(data.storedEnergy() >= costs && costs > 0) {
			tank.addTooltipElement(StringTooltipComponent.orange("-" + costs + " FE/t"));
		}

		if(Client.showHelpTooltips) {
			tank.addTooltipElement(new LineSeparatorTooltipComponent(tank));
			tank.addTooltipElement(new WrappedStringTooltipComponent(I18n.get("pipegoggles.help.keep_powered"), ChatFormatting.GRAY.getColor(), 150));
		}
	}
}
