package com.davenonymous.pipegoggles.render;

import com.davenonymous.pipegoggles.setup.Config;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Collection;

public class BoxRenderer {
    private static void renderBlockOutline(Collection<BoxOptimizer.Line> lines, Color color) {
        Tessellator tessellator = Tessellator.getInstance();

        GlStateManager.pushMatrix();

        GlStateManager.disableDepthTest();
        GlStateManager.disableTexture();

        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        for(BoxOptimizer.Line line : lines) {
            renderLine(tessellator, line, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        }

        GlStateManager.disableAlphaTest();
        GlStateManager.disableBlend();

        GlStateManager.enableTexture();
        GlStateManager.enableDepthTest();

        GlStateManager.popMatrix();
    }

    private static void renderLine(Tessellator tessellator, BoxOptimizer.Line line, int r, int g, int b, int alpha) {
        GlStateManager.lineWidth(Config.DRAW_LINE_WIDTH.get().floatValue());
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        buffer.pos(line.start.x, line.start.y, line.start.z).color(r, g, b, alpha).endVertex();
        buffer.pos(line.end.x, line.end.y, line.end.z).color(r, g, b, alpha).endVertex();

        tessellator.draw();
        GlStateManager.lineWidth(1.0F);
    }

    public static void renderLines(PlayerEntity player, float partialTicks, Color color, Collection<BoxOptimizer.Line> lines) {
        GlStateManager.pushMatrix();

        GlStateManager.translated(-TileEntityRendererDispatcher.staticPlayerX, -TileEntityRendererDispatcher.staticPlayerY, -TileEntityRendererDispatcher.staticPlayerZ);

        renderBlockOutline(lines, color);

        GlStateManager.popMatrix();
    }
}
