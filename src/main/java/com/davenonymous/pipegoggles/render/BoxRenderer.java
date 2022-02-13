package com.davenonymous.pipegoggles.render;

import com.davenonymous.libnonymous.render.CustomRenderTypes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.Collection;

public class BoxRenderer {
    private static void renderBlockOutline(PoseStack poseStack, Collection<BoxOptimizer.Line> lines, Color color, Matrix4f projectionMatrix) {
        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer builder = buffer.getBuffer(CustomRenderTypes.OVERLAY_LINES);

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);

        RenderSystem.disableCull();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableTexture();

        var r = color.getRed();
        var g = color.getGreen();
        var b = color.getBlue();
        var alpha = color.getAlpha();

        var matrix = poseStack.last().pose();
        for(BoxOptimizer.Line line : lines) {
            var sx = line.start.x();
            var sy = line.start.y();
            var sz = line.start.z();
            var dx = line.end.x();
            var dy = line.end.y();
            var dz = line.end.z();

            builder.vertex(matrix, (float)sx, (float)sy, (float)sz).color(r, g, b, alpha).endVertex();
            builder.vertex(matrix, (float)dx, (float)dy, (float)dz).color(r, g, b, alpha).endVertex();
        }

        buffer.endBatch(CustomRenderTypes.OVERLAY_LINES);

        RenderSystem.lineWidth(1.0f);
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.enableTexture();
    }

    private static int bufferIndex(int pX, int pY) {
        return pY * 4 + pX;
    }

    public static void renderLines(PoseStack pose, Player player, float partialTicks, Color color, Collection<BoxOptimizer.Line> lines, Matrix4f projectionMatrix) {
        pose.pushPose();

        Vec3 projection = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        var xOffset = -projection.x;
        var yOffset = -projection.y;
        var zOffset = -projection.z;
        pose.translate(xOffset, yOffset, zOffset);

        renderBlockOutline(pose, lines, color, projectionMatrix);

        pose.popPose();
    }
}