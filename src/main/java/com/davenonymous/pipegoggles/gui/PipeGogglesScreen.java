package com.davenonymous.pipegoggles.gui;

import com.davenonymous.libnonymous.gui.framework.ColorHelper;
import com.davenonymous.libnonymous.gui.framework.GUI;
import com.davenonymous.libnonymous.gui.framework.WidgetContainerScreen;
import com.davenonymous.libnonymous.gui.framework.WidgetSlot;
import com.davenonymous.libnonymous.gui.framework.event.ValueChangedEvent;
import com.davenonymous.libnonymous.gui.framework.event.WidgetEventResult;
import com.davenonymous.libnonymous.gui.framework.widgets.WidgetSelectButton;
import com.davenonymous.pipegoggles.config.ClientConfig;
import com.davenonymous.pipegoggles.data.BlockGroup;
import com.davenonymous.pipegoggles.gui.widgets.WidgetPipeGogglesBlockGroup;
import com.davenonymous.pipegoggles.network.Networking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import java.awt.Color;

public class PipeGogglesScreen extends WidgetContainerScreen<PipeGogglesContainer> {
    public PipeGogglesScreen(PipeGogglesContainer container, Inventory inv, Component name) {
        super(container, inv, name);
    }

    @Override
    protected GUI createGUI() {
        int width = PipeGogglesContainer.WIDTH;
        int height = PipeGogglesContainer.HEIGHT;

        GUI gui = new GUI(0, 0, width, height);

        int yPadding = 3;
        for (int channelId = 0; channelId < 4; channelId++) {
            Color color = ColorHelper.hex2Rgb(ClientConfig.LINE_COLORS.get().get(channelId));
            BlockGroup selectedBg = this.menu.gogglesData.getBlockGroupForSlot(Minecraft.getInstance().level.getRecipeManager(), channelId);
            WidgetPipeGogglesBlockGroup bgWidget = new WidgetPipeGogglesBlockGroup(color, selectedBg);
            bgWidget.setX(5);
            bgWidget.setY(5 + (channelId * (bgWidget.height + yPadding)));

            int finalChannelId = channelId;
            bgWidget.addListener(ValueChangedEvent.class, (event, widget) -> {
                Networking.sendUpdateBlockGroupMessage(menu.currentItemSlot, finalChannelId, ((BlockGroup)event.newValue).getId());
                return WidgetEventResult.HANDLED;
            });
            gui.add(bgWidget);
        }

        WidgetSelectButton<Integer> rangeSelectButton = new WidgetSelectButton<>();
        rangeSelectButton.setWidth(25);
        rangeSelectButton.addChoice(ClientConfig.RANGE_CHOICES.get());
        rangeSelectButton.setX(160);
        rangeSelectButton.setY(7);
        rangeSelectButton.setValue(menu.gogglesData.range);
        rangeSelectButton.addListener(ValueChangedEvent.class, (event, widget) -> {
            Networking.sendUpdateRangeMessage(menu.currentItemSlot, (Integer) event.newValue);
            return WidgetEventResult.HANDLED;
        });

        rangeSelectButton.setTooltipLines(new TranslatableComponent("pipegoggles.gui.range"));

        gui.add(rangeSelectButton);

        // Bind all slots to the matching widgets
        for(Slot slot : this.menu.slots) {
            if(!(slot instanceof WidgetSlot)) {
                continue;
            }

            WidgetSlot widgetSlot = (WidgetSlot)slot;
            if(widgetSlot.matches(PipeGogglesContainer.SLOTGROUP_PLAYER)) {
                widgetSlot.bindToWidget(gui);
            }
        }

        return gui;
    }
}