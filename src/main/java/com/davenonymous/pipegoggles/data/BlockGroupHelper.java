package com.davenonymous.pipegoggles.data;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.setup.RecipeTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockGroupHelper {
    public static BlockGroup emptyBlockGroup = new BlockGroup(new ResourceLocation(PipeGoggles.MODID, "disabled"));

    // TODO: Cache or direct access this?
    public static BlockGroup getBlockGroup(RecipeManager recipeManager, ResourceLocation id) {
        return getBlockGroups(recipeManager).filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    // TODO: Cache this
    public static Stream<BlockGroup> getBlockGroups(RecipeManager recipeManager) {
        return recipeManager.getRecipes().stream().filter(r -> r.getType() == RecipeTypes.blockGroupRecipeType).map(r -> (BlockGroup)r);
    }



    public static BlockGroup getBlockGroupForItem(World world, ItemStack stack) {
        List<BlockGroup> blockGroups = getBlockGroups(world.getRecipeManager()).collect(Collectors.toList());
        for(BlockGroup bg : blockGroups) {
            if(bg.matchesItem(stack)) {
                return bg;
            }
        }

        return null;
    }

}
