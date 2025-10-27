package com.davenonymous.pipegoggles.render;


import com.davenonymous.pipegoggles.config.Client;
import com.davenonymous.pipegoggles.data.EnumGoggleMode;
import com.davenonymous.pipegoggles.data.cache.BoxLineCache;
import com.davenonymous.pipegoggles.data.cache.BoxOptimizer;
import com.davenonymous.pipegoggles.datacomponents.PipeGoggleDataComponent;
import com.davenonymous.pipegoggles.items.PipeGoggleItem;
import com.davenonymous.pipegoggles.setup.ModDataComponents;
import com.davenonymous.pipegoggles.setup.ModItems;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.Collection;

public class BoxRenderer {
    private static void renderBlockOutline(PoseStack poseStack, Collection<BoxLineCache.Line> lines, int color, int lineWidth) {

		var bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
		RenderSystem.enableBlend();
		var consumer = bufferSource.getBuffer(OverlayLineRenderType.forThickness(lineWidth));
		var pose = poseStack.last();
        for(BoxLineCache.Line line : lines) {
            var sx = line.start().x();
            var sy = line.start().y();
            var sz = line.start().z();
            var dx = line.end().x();
            var dy = line.end().y();
            var dz = line.end().z();

			float r = ((color >> 16) & 0xFF) / 255.0F;
			float g = ((color >> 8) & 0xFF) / 255.0F;
			float b = (color & 0xFF) / 255.0F;
			float a = ((color >> 24) & 0xFF) / 255.0F;

			renderLine(pose, consumer, sx, sy, sz, dx, dy, dz, r, g, b, a, r, g, b);
        }
		RenderSystem.disableBlend();
		bufferSource.endBatch();
    }

	private static void renderLine(PoseStack.Pose pose, VertexConsumer consumer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float red,
		float green, float blue, float alpha, float red2, float green2, float blue2) {
		float f = (float)minX;
		float f1 = (float)minY;
		float f2 = (float)minZ;
		float f3 = (float)maxX;
		float f4 = (float)maxY;
		float f5 = (float)maxZ;

		consumer.addVertex(pose, f, f1, f2).setColor(red2, green2, blue, alpha).setNormal(pose, 0.0F, 0.0F, 1.0F);
		consumer.addVertex(pose, f, f1, f5).setColor(red2, green2, blue, alpha).setNormal(pose, 0.0F, 0.0F, 1.0F);
		consumer.addVertex(pose, f3, f1, f2).setColor(red, green, blue, alpha).setNormal(pose, 0.0F, 1.0F, 0.0F);
		consumer.addVertex(pose, f3, f4, f2).setColor(red, green, blue, alpha).setNormal(pose, 0.0F, 1.0F, 0.0F);

		consumer.addVertex(pose, f3, f4, f2).setColor(red, green, blue, alpha).setNormal(pose, -1.0F, 0.0F, 0.0F);
		consumer.addVertex(pose, f, f4, f2).setColor(red, green, blue, alpha).setNormal(pose, -1.0F, 0.0F, 0.0F);
		consumer.addVertex(pose, f, f4, f2).setColor(red, green, blue, alpha).setNormal(pose, 0.0F, 0.0F, 1.0F);
		consumer.addVertex(pose, f, f4, f5).setColor(red, green, blue, alpha).setNormal(pose, 0.0F, 0.0F, 1.0F);
	}

    private static void renderLines(PoseStack poseStack, DyeColor color, Collection<BoxLineCache.Line> lines, int alpha, int lineWidth) {
		Vec3 projection = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
		var xOffset = -projection.x;
		var yOffset = -projection.y;
		var zOffset = -projection.z;
		poseStack.pushPose();
		poseStack.translate(xOffset, yOffset, zOffset);
		renderBlockOutline(poseStack, lines, color.getTextColor() | alpha << 24, lineWidth);
		poseStack.popPose();

    }

	private static void renderBox(PoseStack poseStack, DyeColor color, int alpha, int lineWidth) {
		var optimizer = BoxOptimizer.getBoxLineCache(color);
		BoxRenderer.renderLines(poseStack, color, optimizer.lines, alpha, lineWidth);
	}

	private static void renderOverlay(Player player, ItemStack goggleStack, PoseStack poseStack) {
		PipeGoggleDataComponent data = goggleStack.getOrDefault(ModDataComponents.PIPEGOGGLES_COMPONENT, new PipeGoggleDataComponent());

		for(DyeColor color : data.colors().keySet()) {
			if(!data.isColorEnabled(color)) {
				continue; // Color is disabled, skip
			}

			if(!data.colors().containsKey(color) || data.colors().get(color).isEmpty()) {
				return; // No stack for this color, skip
			}

			BoxOptimizer.updateBoxLineCaches(player, data);
			BoxRenderer.renderBox(poseStack, color, data.alpha(), data.lineWidth());
		}
	}

	private static void renderOverlayForSingleColor(Player player, ItemStack goggleStack, PoseStack poseStack, DyeColor color) {
		PipeGoggleDataComponent data = goggleStack.getOrDefault(ModDataComponents.PIPEGOGGLES_COMPONENT, new PipeGoggleDataComponent());

		if(!data.colors().containsKey(color) || data.colors().get(color).isEmpty()) {
			return; // No stack for this color, skip
		}

		BoxOptimizer.updateBoxLineCaches(player, data);
		BoxRenderer.renderBox(poseStack, color, data.alpha(), data.lineWidth());
	}

	public static void onRenderLast(RenderLevelStageEvent event) {
		Player player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}

		var optGoggleStack = PipeGoggleItem.getGoggleStack(player);
		if (optGoggleStack.isEmpty()) {
			return;
		}

		var goggleStack = optGoggleStack.get();
		var goggleData = PipeGoggleItem.data(goggleStack);
		if (goggleData.isDisabled()) {
			return;
		}

		if(!goggleData.enoughEnergy()) {
			return;
		}

		ItemStack heldItem = player.getMainHandItem();
		if (goggleData.goggleMode() == EnumGoggleMode.ON || heldItem.is(ModItems.PIPE_GOGGLE_ITEM.get()) || player.getOffhandItem().is(ModItems.PIPE_GOGGLE_ITEM.get())) {
			BoxRenderer.renderOverlay(player, goggleStack, event.getPoseStack());
			return;
		}

		if (Client.autoModeRequiresCrouching && !player.isShiftKeyDown()) {
			return; // Auto mode only works when crouching
		}

		if(heldItem.isEmpty()) {
			heldItem = player.getOffhandItem();
		}

		if(heldItem.isEmpty()) {
			return;
		}

		var optColor = goggleData.getColorForStack(heldItem);
		if (optColor.isEmpty()) {
			return;
		}

		if(!goggleData.isColorEnabled(optColor.get())) {
			return;
		}

		BoxRenderer.renderOverlayForSingleColor(player, goggleStack, event.getPoseStack(), optColor.get());
	}
}
