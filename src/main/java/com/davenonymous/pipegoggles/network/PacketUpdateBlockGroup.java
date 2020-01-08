package com.davenonymous.pipegoggles.network;

import com.davenonymous.libnonymous.base.BasePacket;
import com.davenonymous.libnonymous.serialization.Sync;
import com.davenonymous.pipegoggles.item.PipeGogglesItem;
import com.davenonymous.pipegoggles.item.PipeGogglesItemData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketUpdateBlockGroup extends BasePacket {
    @Sync
    int slot;

    @Sync
    int groupNum;

    @Sync
    ResourceLocation groupId;

    public PacketUpdateBlockGroup(int slot, int groupNum, ResourceLocation groupId) {
        super();
        this.slot = slot;
        this.groupNum = groupNum;
        this.groupId = groupId;
    }

    public PacketUpdateBlockGroup(PacketBuffer buf) {
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
        originalData.setGroupForSlot(groupNum, groupId);

        inventory.setInventorySlotContents(this.slot, originalData.createItemStack());
        inventory.markDirty();
    }
}
