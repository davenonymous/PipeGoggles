package com.davenonymous.pipegoggles.items;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.lib.gui.WidgetBlockEntityContainer;
import com.davenonymous.pipegoggles.lib.gui.WidgetContainer;
import com.davenonymous.pipegoggles.setup.ModContainers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PipeGoggleContainer extends WidgetContainer {
	public static int WIDTH = 176;
	public static int HEIGHT = 180;

	public static ResourceLocation SLOTGROUP_PATTERN = PipeGoggles.resource("pattern");
	public static ResourceLocation SLOTGROUP_QUEUE = PipeGoggles.resource("queue");
	public static ResourceLocation SLOTGROUP_DONE = PipeGoggles.resource("done");
	public static ResourceLocation SLOTGROUP_RESULT = PipeGoggles.resource("result");


	public PipeGoggleContainer(int id, Inventory inv, @NotNull Player player) {
		super(ModContainers.PIPE_GOGGLE_CONTAINER.get(), id, inv);
		this.layoutPlayerInventorySlots(8, HEIGHT - 84);
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		ItemStack mainHand = pPlayer.getMainHandItem();
		if (!(mainHand.getItem() instanceof PipeGoggleItem)) {
			return false;
		}

		return super.stillValid(pPlayer);
	}
}
