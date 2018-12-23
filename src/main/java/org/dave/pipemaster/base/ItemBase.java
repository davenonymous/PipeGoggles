package org.dave.pipemaster.base;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.dave.pipemaster.PipeMaster;

public class ItemBase extends Item {
    public ItemBase(String name) {
        this.setRegistryName(PipeMaster.MODID, name);
        this.setTranslationKey(PipeMaster.MODID + "." + name);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    public void renderEffectOnHeldItem(EntityPlayer player, EnumHand mainHand, float partialTicks) {

    }
}