package com.davenonymous.pipegoggles.gui;

import com.davenonymous.libnonymous.gui.framework.WidgetContainer;
import com.davenonymous.pipegoggles.item.PipeGogglesItemData;
import com.davenonymous.pipegoggles.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;


public class PipeGogglesContainer extends WidgetContainer {
    public static int WIDTH = 190;
    public static int HEIGHT = 183;

    public PipeGogglesItemData gogglesData;
    public int currentItemSlot;

    public PipeGogglesContainer(int id, BlockPos pos, Inventory inv, Player player) {
        super(Registration.PIPEGOGGLES_CONTAINER.get(), id, inv);

        this.gogglesData = new PipeGogglesItemData(inv.getItem(inv.selected));
        this.currentItemSlot = inv.selected;

        this.layoutPlayerInventorySlots(8, HEIGHT - 90);
        this.lockSlot(27 + inv.selected);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }
}