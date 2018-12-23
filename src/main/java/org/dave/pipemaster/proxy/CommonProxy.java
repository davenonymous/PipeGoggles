package org.dave.pipemaster.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import org.dave.pipemaster.PipeMaster;
import org.dave.pipemaster.items.goggles.PipeGogglesItem;
import org.dave.pipemaster.network.PipeMasterNetworkHandler;

@Mod.EventBusSubscriber
public class CommonProxy {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> reg = event.getRegistry();
        reg.register(new PipeGogglesItem());
    }

    public void preInit(FMLPreInitializationEvent event) {
    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(PipeMaster.instance, new GuiProxy());
    }

    public void postInit(FMLPostInitializationEvent event) {
        PipeMasterNetworkHandler.init();
    }
}
