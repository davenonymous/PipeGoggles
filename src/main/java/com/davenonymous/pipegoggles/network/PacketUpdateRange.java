package com.davenonymous.pipegoggles.network;

import com.davenonymous.libnonymous.base.BasePacket;
import com.davenonymous.libnonymous.serialization.Sync;
import com.davenonymous.pipegoggles.item.PipeGogglesItem;
import com.davenonymous.pipegoggles.item.PipeGogglesItemData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUpdateRange extends BasePacket {
    @Sync
    int slot;

    @Sync
    int range;

    public PacketUpdateRange(int slot, int range) {
        super();
        this.slot = slot;
        this.range = range;
    }

    public PacketUpdateRange(PacketBuffer buf) {
        super(buf);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> ctx) {
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
    }
}
