package com.davenonymous.pipegoggles.data;

import com.davenonymous.libnonymous.utils.RecipeData;
import com.davenonymous.pipegoggles.setup.RecipeTypes;
import com.google.common.base.Objects;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.List;

public class BlockGroup extends RecipeData {
    private ResourceLocation id;
    private Ingredient itemIcon;
    private String translationKey;
    private HashSet<BlockState> validStates;
    private EnumBoxOptimizationStrategy optimizationStrategy;
    private String modId;

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

    public String getModId() {
        return modId;
    }

    public BlockGroup setModId(String modId) {
        this.modId = modId;
        return this;
    }

    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeTypes.blockGroupSerializer;
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeTypes.blockGroupRecipeType;
    }

    public Ingredient getItemIcon() {
        return this.itemIcon;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public boolean containsBlockState(BlockState state) {
        //Logz.info("Checking whether %s is in %s", state, this.validStates);
        return this.validStates.contains(state.getBlock().getDefaultState()) || this.validStates.contains(state);
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
