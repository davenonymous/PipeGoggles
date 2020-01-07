package com.davenonymous.pipegoggles.setup;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.data.BlockGroup;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class RecipeTypes {
    public static IRecipeType<BlockGroup> blockGroupRecipeType;
    public static IRecipeSerializer<BlockGroup> blockGroupSerializer;

    public static <T extends IRecipe<?>> IRecipeType<T> registerRecipeType (String typeId) {
        final IRecipeType<T> type = new IRecipeType<T>() {
            @Override
            public String toString () {
                return PipeGoggles.MODID + ":" + typeId;
            }
        };

        return Registry.register(Registry.RECIPE_TYPE,  new ResourceLocation(type.toString()), type);
    }


}
