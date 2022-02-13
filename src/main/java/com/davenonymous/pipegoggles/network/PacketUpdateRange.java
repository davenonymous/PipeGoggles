package com.davenonymous.pipegoggles.network;

import com.davenonymous.libnonymous.base.BasePacket;
import com.davenonymous.libnonymous.serialization.Sync;
import com.davenonymous.pipegoggles.item.PipeGogglesItem;
import com.davenonymous.pipegoggles.item.PipeGogglesItemData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

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

    public PacketUpdateRange(FriendlyByteBuf buf) {
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
        originalData.range = this.range;

        inventory.setItem(this.slot, originalData.createItemStack());
        inventory.setChanged();
    }
}