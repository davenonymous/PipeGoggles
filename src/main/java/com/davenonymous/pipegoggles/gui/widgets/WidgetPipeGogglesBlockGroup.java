package com.davenonymous.pipegoggles.gui.widgets;

import com.davenonymous.libnonymous.gui.framework.event.MouseClickEvent;
import com.davenonymous.libnonymous.gui.framework.event.ValueChangedEvent;
import com.davenonymous.libnonymous.gui.framework.event.WidgetEventResult;
import com.davenonymous.libnonymous.gui.framework.widgets.WidgetColorDisplay;
import com.davenonymous.libnonymous.gui.framework.widgets.WidgetPanel;
import com.davenonymous.libnonymous.gui.framework.widgets.WidgetSelectButton;
import com.davenonymous.libnonymous.gui.framework.widgets.WidgetWithChoiceValue;
import com.davenonymous.pipegoggles.data.BlockGroup;
import com.davenonymous.pipegoggles.data.BlockGroupHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WidgetPipeGogglesBlockGroup extends WidgetPanel {
    public WidgetPipeGogglesBlockGroup(Color color, BlockGroup initialGroup) {
        this.setWidth(160);
        this.setHeight(20);

        WidgetColorDisplay colorDisplay = new WidgetColorDisplay(color);
        colorDisplay.setX(2);
        colorDisplay.setY(6);
        colorDisplay.setWidth(11);
        colorDisplay.setHeight(11);

        this.add(colorDisplay);


        WidgetSelectButton<BlockGroup> groupSelectButton = new WidgetSelectButton<BlockGroup>() {
            @Override
            protected void drawButtonContent(PoseStack pPoseStack, Screen screen) {
                BlockGroup blockGroup = this.getValue();
                if(blockGroup.getId().equals(BlockGroupHelper.emptyBlockGroup.getId())) {
                    GuiComponent.drawCenteredString(pPoseStack, screen.getMinecraft().font, "-", width / 2, (height - 8) / 2, 0xEEEEEE);
                } else {
                    Minecraft.getInstance().getItemRenderer().renderGuiItem(getValue().getItemIcon().getItems()[0], getActualX()+2, getActualY()+2);
                    GuiComponent.drawString(pPoseStack, screen.getMinecraft().font, I18n.get(getValue().getTranslationKey()), 20, 6, 0xEEEEEE);
                }
            }


            @Override
            public void addClickListener() {
                // Auto-Select proper group when clicked with specific item, e.g. if clicked with Refined Storage cable, auto-select the refinedstorage group.
                this.addListener(MouseClickEvent.class, (event, widget) -> {
                    if(!event.carriedStack.isEmpty()) {

                        BlockGroup toSelect = BlockGroupHelper.getBlockGroupForItem(Minecraft.getInstance().level, event.carriedStack);
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
        choices.addAll(BlockGroupHelper.getBlockGroups(Minecraft.getInstance().level.getRecipeManager()).toList());
        groupSelectButton.addChoice(choices);

        groupSelectButton.setWidth(135);
        groupSelectButton.setX(17);
        groupSelectButton.setY(2);
        groupSelectButton.setValue(initialGroup);

        this.addChildListener(ValueChangedEvent.class, groupSelectButton);
        this.add(groupSelectButton);
    }
}