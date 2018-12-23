package org.dave.pipemaster.gui.widgets;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import org.dave.pipemaster.gui.event.MouseEnterEvent;
import org.dave.pipemaster.gui.event.MouseExitEvent;
import org.dave.pipemaster.gui.event.WidgetEventResult;

public class WidgetSelectButton<T> extends WidgetWithChoiceValue<T> {
    public boolean hovered = false;

    protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");

    public WidgetSelectButton() {
        this.setHeight(20);
        this.setWidth(100);

        this.setId("WidgetSelectButton");

        this.addListener(MouseEnterEvent.class, (event, widget) -> {((WidgetSelectButton)widget).hovered = true; return WidgetEventResult.CONTINUE_PROCESSING; });
        this.addListener(MouseExitEvent.class, (event, widget) -> {((WidgetSelectButton)widget).hovered = false; return WidgetEventResult.CONTINUE_PROCESSING; });

        this.addClickListener();
    }

    @Override
    public void draw(GuiScreen screen) {
        screen.mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
        GlStateManager.color(1.0F, 1.0F, 1.0F, hovered ? 1.0F : 1.0F);

        //Logz.info("Width: %d, height: %d", width, height);

        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.translate(0.0f, 0.0f, 2.0f);
        //Logz.info("Texture: %s", screen.mc.getTextureManager().getTexture(BUTTON_TEXTURES).getGlTextureId());
        if(hovered) {
            screen.drawTexturedModalRect(0, 0, 0, 46 + 2 * 20, width / 2, height);
            screen.drawTexturedModalRect(width / 2, 0, 200 - width / 2, 46 + 2 * 20, width / 2, height);
        } else {
            screen.drawTexturedModalRect(0, 0, 0, 46 + 1 * 20, width / 2, height);
            screen.drawTexturedModalRect(width / 2, 0, 200 - width / 2, 46 + 1 * 20, width / 2, height);
        }

        drawButtonContent(screen, screen.mc.fontRenderer);
    }

    protected void drawButtonContent(GuiScreen screen, FontRenderer fontrenderer) {
        int color = 0xEEEEEE;
        screen.drawCenteredString(fontrenderer, getValue().toString(), width / 2, (height - 8) / 2, color);
    }
}
