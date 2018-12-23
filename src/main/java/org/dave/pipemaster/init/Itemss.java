package org.dave.pipemaster.init;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.dave.pipemaster.PipeMaster;
import org.dave.pipemaster.items.goggles.PipeGogglesItem;

@GameRegistry.ObjectHolder(PipeMaster.MODID)
public class Itemss {
    @GameRegistry.ObjectHolder("pipe_goggles")
    public static PipeGogglesItem pipeGoggles;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        pipeGoggles.initModel();
    }
}
