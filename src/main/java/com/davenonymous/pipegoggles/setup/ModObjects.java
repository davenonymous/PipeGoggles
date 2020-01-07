package com.davenonymous.pipegoggles.setup;


import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.gui.PipeGogglesContainer;
import com.davenonymous.pipegoggles.item.PipeGogglesItem;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(PipeGoggles.MODID)
public class ModObjects {
    @ObjectHolder("pipe_goggles")
    public static PipeGogglesItem PIPE_GOGGLES;

    @ObjectHolder("pipe_goggles")
    public static ContainerType<PipeGogglesContainer> PIPE_GOGGLES_CONTAINER;
}
