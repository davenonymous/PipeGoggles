package com.davenonymous.pipegoggles.data;

import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.setup.Registration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.stream.Stream;

public class BlockGroupHelper {
    public static BlockGroup emptyBlockGroup = new BlockGroup(new ResourceLocation(PipeGoggles.MODID, "disabled"));

    // TODO: Cache or direct access this?
    public static BlockGroup getBlockGroup(RecipeManager recipeManager, ResourceLocation id) {
        return getBlockGroups(recipeManager).filter(s -> s.getId().equals(id)).findFirst().orElse(null);
    }

    // TODO: Cache this
    public static Stream<BlockGroup> getBlockGroups(RecipeManager recipeManager) {
        return recipeManager.getRecipes().stream().filter(r -> r.getType() == Registration.blockGroupRecipeType).map(r -> (BlockGroup)r);
    }



    public static BlockGroup getBlockGroupForItem(Level level, ItemStack stack) {
        List<BlockGroup> blockGroups = getBlockGroups(level.getRecipeManager()).toList();
        for(BlockGroup bg : blockGroups) {
            if(bg.matchesItem(stack)) {
                return bg;
            }
        }

        return null;
    }

}