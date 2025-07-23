package com.davenonymous.pipegoggles.datagen;


import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.setup.ModItems;
import com.davenonymous.pipegoggles.setup.Registration;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class DGRecipes extends RecipeProvider {
	public DGRecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder
			.shaped(RecipeCategory.MISC, ModItems.PIPE_GOGGLE_ITEM.get())
			.pattern("g g").pattern("r r").pattern("iii")
			.define('g', Tags.Items.GLASS_PANES)
			.define('r', Tags.Items.DUSTS_REDSTONE)
			.define('i', Tags.Items.INGOTS_IRON)
			.group(PipeGoggles.MODID)
			.unlockedBy("pipegoggles", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GLASS_PANE, Items.REDSTONE, Items.IRON_INGOT)).save(recipeOutput);
	}
}
