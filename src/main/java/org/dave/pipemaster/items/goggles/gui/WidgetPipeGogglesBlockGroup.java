package org.dave.pipemaster.items.goggles.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import org.dave.pipemaster.PipeMaster;
import org.dave.pipemaster.data.blockgroups.BlockGroup;
import org.dave.pipemaster.gui.event.MouseClickEvent;
import org.dave.pipemaster.gui.event.ValueChangedEvent;
import org.dave.pipemaster.gui.event.WidgetEventResult;
import org.dave.pipemaster.gui.widgets.WidgetColorDisplay;
import org.dave.pipemaster.gui.widgets.WidgetPanel;
import org.dave.pipemaster.gui.widgets.WidgetSelectButton;
import org.dave.pipemaster.gui.widgets.WidgetWithChoiceValue;
import org.dave.pipemaster.util.Logz;

import java.awt.*;

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
            protected void drawButtonContent(GuiScreen screen, FontRenderer fontrenderer) {
                BlockGroup blockGroup = this.getValue();
                if(blockGroup.getId().equals("disabled")) {
                    screen.drawCenteredString(fontrenderer, "-", width / 2, (height - 8) / 2, 0xEEEEEE);
                } else {
                    Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(getValue().getItemIcon(), 2, 2);
                    screen.drawString(fontrenderer, I18n.format(getValue().getTranslationKey()), 20, 6, 0xEEEEEE);
                }
                RenderHelper.disableStandardItemLighting();
            }

            @Override
            public void addClickListener() {
                // Auto-Select proper group when clicked with specific item, e.g. if clicked with Refined Storage cable, auto-select the refinedstorage group.
                this.addListener(MouseClickEvent.class, (event, widget) -> {
                    if(!event.carriedStack.isEmpty()) {

                        String modId = event.carriedStack.getItem().getRegistryName().getNamespace();
                        BlockGroup selected = PipeMaster.blockGroupRegistry.getBlockGroupByModId(modId);

                        ((WidgetWithChoiceValue)widget).setValue(selected);
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
        groupSelectButton.addChoice(PipeMaster.blockGroupRegistry.getBlockGroups());
        groupSelectButton.setWidth(135);
        groupSelectButton.setX(17);
        groupSelectButton.setY(2);
        groupSelectButton.setValue(initialGroup);

        // TODO: Add Power Usage display

        this.addChildListener(ValueChangedEvent.class, groupSelectButton);
        this.add(groupSelectButton);

    }
}
