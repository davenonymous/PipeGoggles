package com.davenonymous.pipegoggles.data;


import com.davenonymous.libnonymous.base.RecipeData;
import com.davenonymous.pipegoggles.setup.Registration;
import com.google.common.base.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.List;


public class BlockGroup extends RecipeData {
    private ResourceLocation id;
    private Ingredient itemIcon;
    private String translationKey;
    private HashSet<BlockState> validStates;
    private EnumBoxOptimizationStrategy optimizationStrategy;

    public BlockGroup(ResourceLocation id) {
        this.id = id;
        this.validStates = new HashSet<>();
        this.optimizationStrategy = EnumBoxOptimizationStrategy.REMOVE_DUPLICATE_LINES;
    }

    public BlockGroup setItemIcon(Ingredient itemIcon) {
        this.itemIcon = itemIcon;
        return this;
    }

    public BlockGroup setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
        return this;
    }

    public BlockGroup setOptimizationStrategy(EnumBoxOptimizationStrategy optimizationStrategy) {
        this.optimizationStrategy = optimizationStrategy;
        return this;
    }

    public EnumBoxOptimizationStrategy getOptimizationStrategy() {
        return optimizationStrategy;
    }

    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Registration.blockGroupSerializer.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Registration.blockGroupRecipeType;
    }

    public Ingredient getItemIcon() {
        return this.itemIcon;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public boolean containsBlockState(BlockState state) {
        //Logz.info("Checking whether %s is in %s", state, this.validStates);
        return this.validStates.contains(state.getBlock().defaultBlockState()) || this.validStates.contains(state);
    }

    public BlockGroup setValidBlockStates(List<BlockState> blocks) {
        this.validStates.addAll(blocks);
        return this;
    }

    public HashSet<BlockState> getValidStates() {
        return validStates;
    }

    @Override
    public String toString() {
        return String.format("BlockGroup[id=%s]", this.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockGroup that = (BlockGroup) o;
        return Objects.equal(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public boolean matchesItem(ItemStack stack) {
        // TODO: Match stacks mod against blockgroup mod
        return true;
    }
}