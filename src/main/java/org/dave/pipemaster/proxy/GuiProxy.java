package org.dave.pipemaster.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import org.dave.pipemaster.items.goggles.PipeGogglesData;
import org.dave.pipemaster.items.goggles.gui.PipeGogglesContainer;
import org.dave.pipemaster.items.goggles.gui.PipeGogglesGuiContainer;

import javax.annotation.Nullable;

public class GuiProxy implements IGuiHandler {
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == GuiIDS.PIPE_GOGGLES.ordinal()) {
            return new PipeGogglesContainer(player.inventory, getGogglesData(player, x));
        }

        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == GuiIDS.PIPE_GOGGLES.ordinal()) {
            return new PipeGogglesGuiContainer(new PipeGogglesContainer(player.inventory, getGogglesData(player, x)), getGogglesData(player, x), player.inventory.currentItem);
        }

        return null;
    }

    private int getSlotBeingActivated(int hand) {
        if(hand == EnumHand.MAIN_HAND.ordinal()) {
            return EntityEquipmentSlot.MAINHAND.getSlotIndex();
        }

        return EntityEquipmentSlot.OFFHAND.getSlotIndex();
    }

    private PipeGogglesData getGogglesData(EntityPlayer player, int hand) {
        ItemStack heldStack = player.getHeldItem(EnumHand.values()[hand]);
        return new PipeGogglesData(heldStack);
    }

    public enum GuiIDS {
        PIPE_GOGGLES
    }
}
