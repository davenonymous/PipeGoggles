package com.davenonymous.pipegoggles.setup;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.davenonymous.pipegoggles.PipeGoggles.MODID;

// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
// Event bus for receiving Registry Events)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

    @SubscribeEvent
    static void onCommonSetup(FMLCommonSetupEvent event) {
        Registration.blockGroupRecipeType = RecipeType.register(new ResourceLocation(MODID, "blockgroup").toString());
    }

    @SubscribeEvent
    public static void onClientReloadListener(RegisterClientReloadListenersEvent event) {
        var listener = new ResourceManagerReloadListener() {

            @Override
            public void onResourceManagerReload(ResourceManager pResourceManager) {

            }
        };
        event.registerReloadListener(listener);
    }


}