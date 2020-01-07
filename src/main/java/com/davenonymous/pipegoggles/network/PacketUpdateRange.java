package com.davenonymous.pipegoggles.network;

import com.davenonymous.pipegoggles.item.PipeGogglesItem;
import com.davenonymous.pipegoggles.item.PipeGogglesItemData;
import com.davenonymous.pipegoggles.util.Logz;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUpdateRange {
    int slot;
    int range;

    public PacketUpdateRange(int slot, int range) {
        this.slot = slot;
        this.range = range;
    }

    public PacketUpdateRange(PacketBuffer buf) {
        this.slot = buf.readInt();
        this.range = buf.readInt();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(slot);
        buf.writeInt(range);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity serverPlayer = ctx.get().getSender();
            IInventory inventory = serverPlayer.inventory;

            ItemStack originalStack = inventory.getStackInSlot(this.slot);
            if(originalStack.isEmpty() || !(originalStack.getItem() instanceof PipeGogglesItem)) {
                return;
            }

            PipeGogglesItemData originalData = new PipeGogglesItemData(originalStack);
            originalData.range = this.range;

            inventory.setInventorySlotContents(this.slot, originalData.createItemStack());
            inventory.markDirty();
        });
        ctx.get().setPacketHandled(true);
    }
}
