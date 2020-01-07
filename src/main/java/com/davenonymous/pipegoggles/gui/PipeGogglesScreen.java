package com.davenonymous.pipegoggles.gui;

import com.davenonymous.libnonymous.gui.framework.ColorHelper;
import com.davenonymous.libnonymous.gui.framework.GUI;
import com.davenonymous.libnonymous.gui.framework.WidgetContainerScreen;
import com.davenonymous.libnonymous.gui.framework.WidgetSlot;
import com.davenonymous.libnonymous.gui.framework.event.ValueChangedEvent;
import com.davenonymous.libnonymous.gui.framework.event.WidgetEventResult;
import com.davenonymous.libnonymous.gui.framework.widgets.WidgetSelectButton;
import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.data.BlockGroup;
import com.davenonymous.pipegoggles.gui.widgets.WidgetPipeGogglesBlockGroup;
import com.davenonymous.pipegoggles.network.Networking;
import com.davenonymous.pipegoggles.setup.Config;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;

public class PipeGogglesScreen extends WidgetContainerScreen<PipeGogglesContainer> {
    public PipeGogglesScreen(PipeGogglesContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    protected GUI createGUI() {
        int width = PipeGogglesContainer.WIDTH;
        int height = PipeGogglesContainer.HEIGHT;

        GUI gui = new GUI(0, 0, width, height);

        int yPadding = 3;
        for (int channelId = 0; channelId < 4; channelId++) {
            Color color = ColorHelper.hex2Rgb(Config.LINE_COLORS.get().get(channelId));
            BlockGroup selectedBg = this.container.gogglesData.getBlockGroupForSlot(PipeGoggles.proxy.getClientWorld().getRecipeManager(), channelId);
            WidgetPipeGogglesBlockGroup bgWidget = new WidgetPipeGogglesBlockGroup(color, selectedBg);
            bgWidget.setX(5);
            bgWidget.setY(5 + (channelId * (bgWidget.height + yPadding)));

            int finalChannelId = channelId;
            bgWidget.addListener(ValueChangedEvent.class, (event, widget) -> {
                Networking.sendUpdateBlockGroupMessage(container.currentItemSlot, finalChannelId, ((BlockGroup)event.newValue).getId());
                return WidgetEventResult.HANDLED;
            });
            gui.add(bgWidget);
        }

        WidgetSelectButton<Integer> rangeSelectButton = new WidgetSelectButton<>();
        rangeSelectButton.setWidth(25);
        rangeSelectButton.addChoice(Config.RANGE_CHOICES.get());
        rangeSelectButton.setX(160);
        rangeSelectButton.setY(7);
        rangeSelectButton.setValue(container.gogglesData.range);
        rangeSelectButton.addListener(ValueChangedEvent.class, (event, widget) -> {
            Networking.sendUpdateRangeMessage(container.currentItemSlot, (Integer) event.newValue);
            return WidgetEventResult.HANDLED;
        });

        rangeSelectButton.setTooltipLines(new TranslationTextComponent("pipegoggles.gui.range"));

        gui.add(rangeSelectButton);

        // Bind all slots to the matching widgets
        for(Slot slot : this.container.inventorySlots) {
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
