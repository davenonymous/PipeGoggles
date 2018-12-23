package org.dave.pipemaster.misc;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import org.dave.pipemaster.PipeMaster;
import org.dave.pipemaster.init.Itemss;

public class PipeMasterCreativeTab extends CreativeTabs {
    public PipeMasterCreativeTab() {
        super(PipeMaster.MODID);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Itemss.pipeGoggles);
    }
}
