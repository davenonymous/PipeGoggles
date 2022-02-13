package com.davenonymous.pipegoggles.network;

import com.davenonymous.libnonymous.base.BasePacket;
import com.davenonymous.libnonymous.serialization.Sync;
import com.davenonymous.pipegoggles.item.PipeGogglesItem;
import com.davenonymous.pipegoggles.item.PipeGogglesItemData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;


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

    public PacketUpdateBlockGroup(FriendlyByteBuf buf) {
        super(buf);
    }

    @Override
    public void doWork(Supplier<NetworkEvent.Context> ctx) {
        ServerPlayer serverPlayer = ctx.get().getSender();
        Inventory inventory = serverPlayer.getInventory();

        ItemStack originalStack = inventory.getItem(this.slot);
        if(originalStack.isEmpty() || !(originalStack.getItem() instanceof PipeGogglesItem)) {
            return;
        }

        PipeGogglesItemData originalData = new PipeGogglesItemData(originalStack);
        originalData.setGroupForSlot(groupNum, groupId);

        inventory.setItem(this.slot, originalData.createItemStack());
        inventory.setChanged();
    }
}