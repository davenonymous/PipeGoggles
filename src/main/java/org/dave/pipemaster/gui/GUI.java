package org.dave.pipemaster.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.dave.pipemaster.gui.widgets.Widget;
import org.dave.pipemaster.gui.widgets.WidgetPanel;
import org.dave.pipemaster.util.Logz;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class GUI extends WidgetPanel {
    // TODO: Add auto centering constructor, i.e. without x and y

    public GUI(int x, int y, int width, int height) {
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
        this.setPadding(3);
        this.setPaddingBottom(0);
    }

    public void drawGUI(GuiScreen screen) {
        this.setX((screen.width - this.width)/2);
        this.setY((screen.height - this.height)/2);

        this.shiftAndDraw(screen);
    }

    @Override
    public void drawBeforeShift(GuiScreen screen) {
        //screen.drawDefaultBackground();

        super.drawBeforeShift(screen);
    }

    @Override
    public void draw(GuiScreen screen) {
        super.draw(screen);
    }

    public void drawTooltips(GuiScreen screen, int mouseX, int mouseY) {
        List<Widget> hoveredTooltipWidgets = getHoveredWidgets().stream().filter(Widget::hasToolTip).collect(Collectors.toList());
        if(hoveredTooltipWidgets.size() <= 0) {
            return;
        }

        Widget hoveredWidget = hoveredTooltipWidgets.get(0);

        FontRenderer font = screen.mc.fontRenderer;
        GuiUtils.drawHoveringText(hoveredWidget.getTooltip(), mouseX, mouseY, width, height, 180, font);
    }

}
