package org.dave.pipemaster.base;


import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseEvents {
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void handleRendererForBaseObjects(RenderWorldLastEvent event) {
        if (!Minecraft.isGuiEnabled() || Minecraft.getMinecraft().player == null) {
            return;
        }

        EntityPlayer player = Minecraft.getMinecraft().player;
        ItemStack mainHand = player.getHeldItem(EnumHand.MAIN_HAND);
        ItemStack offHand = player.getHeldItem(EnumHand.OFF_HAND);

        if(!mainHand.isEmpty()) {
            if(mainHand.getItem() instanceof ItemBase) {
                ItemBase mainBase = (ItemBase)mainHand.getItem();
                mainBase.renderEffectOnHeldItem(player, EnumHand.MAIN_HAND, event.getPartialTicks());
            } // TODO: elseif ItemBlockBase
        }

        if(!offHand.isEmpty()) {
            if(offHand.getItem() instanceof ItemBase) {
                ItemBase mainBase = (ItemBase)offHand.getItem();
                mainBase.renderEffectOnHeldItem(player, EnumHand.OFF_HAND, event.getPartialTicks());
            } // TODO: elseif ItemBlockBase
        }
    }
}
