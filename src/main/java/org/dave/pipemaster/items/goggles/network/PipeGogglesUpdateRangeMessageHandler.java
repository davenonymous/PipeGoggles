package org.dave.pipemaster.items.goggles.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.dave.pipemaster.items.goggles.PipeGogglesData;
import org.dave.pipemaster.items.goggles.PipeGogglesItem;

public class PipeGogglesUpdateRangeMessageHandler implements IMessageHandler<PipeGogglesUpdateRangeMessage, PipeGogglesUpdateRangeMessage> {

    @Override
    public PipeGogglesUpdateRangeMessage onMessage(PipeGogglesUpdateRangeMessage message, MessageContext ctx) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> handle(message, ctx));
        return null;
    }

    private void handle(PipeGogglesUpdateRangeMessage message, MessageContext context) {
        EntityPlayer player = context.getServerHandler().player;
        IInventory inventory = player.inventory;

        ItemStack originalStack = inventory.getStackInSlot(message.slot);
        if(originalStack.isEmpty() || !(originalStack.getItem() instanceof PipeGogglesItem)) {
            return;
        }

        PipeGogglesData originalData = new PipeGogglesData(originalStack);
        originalData.setRange(message.range);

        inventory.setInventorySlotContents(message.slot, originalData.createItemStack());
        inventory.markDirty();
    }
}
