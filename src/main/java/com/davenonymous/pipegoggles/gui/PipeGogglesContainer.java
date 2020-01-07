package com.davenonymous.pipegoggles.gui;

import com.davenonymous.libnonymous.gui.framework.WidgetContainer;
import com.davenonymous.pipegoggles.item.PipeGogglesItemData;
import com.davenonymous.pipegoggles.setup.ModObjects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

public class PipeGogglesContainer extends WidgetContainer {
    public static int WIDTH = 190;
    public static int HEIGHT = 183;

    public PipeGogglesItemData gogglesData;
    public int currentItemSlot;

    public PipeGogglesContainer(int id, PlayerInventory inv, PlayerEntity player) {
        super(ModObjects.PIPE_GOGGLES_CONTAINER, id, inv);

        this.gogglesData = new PipeGogglesItemData(inv.getStackInSlot(inv.currentItem));
        this.currentItemSlot = inv.currentItem;

        this.layoutPlayerInventorySlots(8, HEIGHT - 90);
        this.lockSlot(27 + inv.currentItem);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}