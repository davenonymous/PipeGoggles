package org.dave.pipemaster.items.goggles;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Collection;

public class BoxRenderer {
    private static void renderBlockOutline(World world, Collection<BoxOptimizer.Line> lines, Color color) {
        Tessellator tessellator = Tessellator.getInstance();

        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();

        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        lines.forEach(line -> renderLine(tessellator, line, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));

        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();

        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    private static void renderLine(Tessellator tessellator, BoxOptimizer.Line line, int r, int g, int b, int alpha) {
        GlStateManager.glLineWidth(PipeGogglesConfigOptions.lineWidth);
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        buffer.pos(line.start.x, line.start.y, line.start.z).color(r, g, b, alpha).endVertex();
        buffer.pos(line.end.x, line.end.y, line.end.z).color(r, g, b, alpha).endVertex();

        tessellator.draw();
        GlStateManager.glLineWidth(1.0F);
    }

    public static void renderLines(EntityPlayer player, float partialTicks, Color color, Collection<BoxOptimizer.Line> lines) {
        GlStateManager.pushMatrix();

        Vec3d cameraPosition = new Vec3d(
                player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks,
                player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks,
                player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks
        );

        GlStateManager.translate(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);

        renderBlockOutline(player.world, lines, color);

        GlStateManager.popMatrix();
    }
}
