package com.davenonymous.pipegoggles.setup;

import com.davenonymous.libnonymous.base.RecipeData;
import com.davenonymous.pipegoggles.data.BlockGroup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.davenonymous.pipegoggles.PipeGoggles.MODID;

// You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
// Event bus for receiving Registry Events)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEvents {

    @SubscribeEvent
    public static void createNewRegistry(RegistryEvent.NewRegistry event) {
        Registration.blockGroupRecipeType = createRecipeType(BlockGroup.class, "blockgroup");
    }

    private static <T extends RecipeData> RecipeType<T> createRecipeType(Class<T> type, String id) {
        ResourceLocation recipeTypeResource = new ResourceLocation(MODID, id);
        RecipeType<T> reg = Registry.register(Registry.RECIPE_TYPE, recipeTypeResource, new RecipeType<T>() {
            @Override
            public String toString() {
                return recipeTypeResource.toString();
            }
        });

        return reg;
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