package org.dave.pipemaster.proxy;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import org.dave.pipemaster.init.Blockss;
import org.dave.pipemaster.init.Itemss;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        Blockss.initModels();
        Itemss.initModels();
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }
}
