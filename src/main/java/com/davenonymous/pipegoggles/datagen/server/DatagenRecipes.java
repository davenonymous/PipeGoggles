package com.davenonymous.pipegoggles.datagen.server;


import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.setup.Registration;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class DatagenRecipes extends RecipeProvider {
	public DatagenRecipes(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder
				.shaped(Registration.PIPE_GOOGLES.get())
				.pattern("g g").pattern("r r").pattern("iii")
				.m_206416_('g', Tags.Items.GLASS_PANES)
				.m_206416_('r', Tags.Items.DUSTS_REDSTONE)
				.m_206416_('i', Tags.Items.INGOTS_IRON)
				.group(PipeGoggles.MODID).unlockedBy("pipegoggles", InventoryChangeTrigger.TriggerInstance.hasItems(Items.GLASS_PANE, Items.REDSTONE, Items.IRON_INGOT)).save(consumer);
	}
}