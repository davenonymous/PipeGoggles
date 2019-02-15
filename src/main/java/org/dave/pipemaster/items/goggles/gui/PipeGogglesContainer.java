package org.dave.pipemaster.items.goggles.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.dave.pipemaster.items.goggles.PipeGogglesData;
import org.dave.pipemaster.items.goggles.PipeGogglesItem;

public class PipeGogglesContainer extends Container {
    private PipeGogglesData gogglesData;

    public PipeGogglesContainer(IInventory playerInventory, PipeGogglesData gogglesData) {
        this.gogglesData = gogglesData;

        this.addPlayerSlots(playerInventory);
    }

    private void addPlayerSlots(IInventory playerInventory) {
        int yOffset = 101;
        int xOffset = 15;

        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = col * 18 + xOffset;
                int y = row * 18 + yOffset;
                this.addSlotToContainer(new LockedGogglesSlot(playerInventory, col + row * 9 + 10, x, y));
            }
        }

        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = row * 18 + xOffset;
            int y = 58 + yOffset;
            this.addSlotToContainer(new LockedGogglesSlot(playerInventory, row, x, y));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        // TODO: Make it possible to rearrange the player inventory with shift-clicking in this gui
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    private class LockedGogglesSlot extends Slot {

        public LockedGogglesSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean canTakeStack(EntityPlayer playerIn) {
            ItemStack stack = this.getStack();
            if(stack.getItem() instanceof PipeGogglesItem) {
                return false;
            }

            return super.canTakeStack(playerIn);
        }
    }
}
