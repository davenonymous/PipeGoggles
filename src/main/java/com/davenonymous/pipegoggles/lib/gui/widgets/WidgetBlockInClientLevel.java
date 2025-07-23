package com.davenonymous.pipegoggles.lib.gui.widgets;

import com.davenonymous.pipegoggles.lib.gui.GUIHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class WidgetBlockInClientLevel extends WidgetWithValue<BlockPos> {
	boolean drawSlot = false;

	public WidgetBlockInClientLevel(BlockPos pos) {
		this.setSize(16, 16);
		this.setValue(pos);
	}

	public WidgetBlockInClientLevel setDrawSlot(boolean drawSlot) {
		this.drawSlot = drawSlot;
		return this;
	}

	@Override
	public void setValue(BlockPos newValue) {
		super.setValue(newValue);

		var level = Minecraft.getInstance().level;
		if(level == null || !level.isLoaded(getValue())) {
			return; // Block position is not loaded in the client level
		}

		BlockState targetState = level.getBlockState(getValue());
		if(targetState.isAir()) {
			return; // No block at this position
		}

		BlockHitResult fakeHitResult = new BlockHitResult(
			targetState.getShape(level, getValue()).bounds().getCenter(),
			Direction.DOWN,
			getValue(),
			true
		);

		ItemStack stack = targetState.getCloneItemStack(fakeHitResult, level, getValue(), Minecraft.getInstance().player);
		if(!stack.isEmpty()) {
			var tooltipFlag = Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL;
			this.setTooltipLines(stack.getTooltipLines(
				Item.TooltipContext.EMPTY,
				Minecraft.getInstance().player,
				tooltipFlag
			));
		}

	}

	private void drawSlot(GuiGraphics pGuiGraphics, Screen screen) {
		RenderSystem.setShaderTexture(0, GUIHelper.tabIcons);

		int texOffsetY = 84;
		int texOffsetX = 84;
		pGuiGraphics.blit(GUIHelper.tabIcons, -1, -1, texOffsetX, texOffsetY, 18, 18);
	}

	@Override
	public void draw(GuiGraphics pGuiGraphics, Screen screen) {
		super.draw(pGuiGraphics, screen);

		if(drawSlot) {
			pGuiGraphics.pose().pushPose();
			pGuiGraphics.pose().translate(0f, 0f, 50.0f); // Position the slot
			this.drawSlot(pGuiGraphics, screen);
			pGuiGraphics.pose().popPose();
		}

		if(this.value == null) {
			return;
		}

		if(!visible || !areAllParentsVisible()) {
			return;
		}

		var level = Minecraft.getInstance().level;
		if(level == null || !level.isLoaded(getValue())) {
			return; // Block position is not loaded in the client level
		}

		BlockState targetState = level.getBlockState(getValue());
		if(targetState.isAir()) {
			return; // No block at this position
		}

		float scaleFactor = 9.0f;
		var blockRenderer = Minecraft.getInstance().getBlockRenderer();
		pGuiGraphics.pose().pushPose();
		pGuiGraphics.pose().translate(this.width / 8f, this.height / 1.4f, 100.0f); // Render on top of everything else
		pGuiGraphics.pose().scale(-scaleFactor, scaleFactor, scaleFactor); // Scale to fit the widget size
		pGuiGraphics.pose().mulPose(Axis.XP.rotationDegrees(-30.0f));
		pGuiGraphics.pose().mulPose(Axis.YP.rotationDegrees(-45.0f));
		pGuiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(180.0f));

		blockRenderer.renderSingleBlock(
			targetState, pGuiGraphics.pose(), pGuiGraphics.bufferSource(),
			0xFF00F0, OverlayTexture.NO_OVERLAY, level.getModelData(this.value), RenderType.CUTOUT);

		pGuiGraphics.pose().popPose();
	}

}
