package com.davenonymous.pipegoggles.gui.widgets;

import com.davenonymous.libnonymous.gui.framework.event.MouseClickEvent;
import com.davenonymous.libnonymous.gui.framework.event.ValueChangedEvent;
import com.davenonymous.libnonymous.gui.framework.event.WidgetEventResult;
import com.davenonymous.libnonymous.gui.framework.widgets.WidgetColorDisplay;
import com.davenonymous.libnonymous.gui.framework.widgets.WidgetPanel;
import com.davenonymous.libnonymous.gui.framework.widgets.WidgetSelectButton;
import com.davenonymous.libnonymous.gui.framework.widgets.WidgetWithChoiceValue;
import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.data.BlockGroup;
import com.davenonymous.pipegoggles.data.BlockGroupHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WidgetPipeGogglesBlockGroup extends WidgetPanel {
    public WidgetPipeGogglesBlockGroup(Color color, BlockGroup initialGroup) {
        this.setWidth(160);
        this.setHeight(20);

        WidgetColorDisplay colorDisplay = new WidgetColorDisplay(color, color.darker().darker().darker(), false);
        colorDisplay.setX(2);
        colorDisplay.setY(6);
        colorDisplay.setWidth(11);
        colorDisplay.setHeight(11);

        this.add(colorDisplay);

        WidgetSelectButton<BlockGroup> groupSelectButton = new WidgetSelectButton<BlockGroup>() {
            @Override
            protected void drawButtonContent(Screen screen, FontRenderer fontrenderer) {
                BlockGroup blockGroup = this.getValue();
                if(blockGroup.getId().equals(BlockGroupHelper.emptyBlockGroup.getId())) {
                    screen.drawCenteredString(fontrenderer, "-", width / 2, (height - 8) / 2, 0xEEEEEE);
                } else {
                    Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(getValue().getItemIcon().getMatchingStacks()[0], 2, 2);
                    screen.drawString(fontrenderer, I18n.format(getValue().getTranslationKey()), 20, 6, 0xEEEEEE);
                }
                RenderHelper.disableStandardItemLighting();
            }

            @Override
            public void addClickListener() {
                // Auto-Select proper group when clicked with specific item, e.g. if clicked with Refined Storage cable, auto-select the refinedstorage group.
                this.addListener(MouseClickEvent.class, (event, widget) -> {
                    if(!event.carriedStack.isEmpty()) {

                        BlockGroup toSelect = BlockGroupHelper.getBlockGroupForItem(PipeGoggles.proxy.getClientWorld(), event.carriedStack);
                        if(toSelect != null) {
                            ((WidgetWithChoiceValue)widget).setValue(toSelect);
                        }
                    } else {
                        // Otherwise handle as usual.
                        if(event.isLeftClick()) {
                            ((WidgetWithChoiceValue)widget).next();
                        } else {
                            ((WidgetWithChoiceValue)widget).prev();
                        }
                    }
                    return WidgetEventResult.HANDLED;
                });

            }
        };


        List<BlockGroup> choices = new ArrayList<>();
        choices.add(BlockGroupHelper.emptyBlockGroup);
        choices.addAll(BlockGroupHelper.getBlockGroups(Minecraft.getInstance().world.getRecipeManager()).collect(Collectors.toList()));
        groupSelectButton.addChoice(choices);

        groupSelectButton.setWidth(135);
        groupSelectButton.setX(17);
        groupSelectButton.setY(2);
        groupSelectButton.setValue(initialGroup);

        this.addChildListener(ValueChangedEvent.class, groupSelectButton);
        this.add(groupSelectButton);
    }
}
