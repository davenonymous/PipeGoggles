package org.dave.pipemaster.data.blockgroups;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import org.dave.pipemaster.items.goggles.EnumBoxOptimizationStrategy;

import java.util.ArrayList;
import java.util.List;

public class BlockGroup {
    private String id;
    private ItemStack itemIcon;
    private String translationKey;
    private List<IBlockState> validStates;
    private EnumBoxOptimizationStrategy optimizationStrategy;

    public BlockGroup(String id) {
        this.id = id;
        this.validStates = new ArrayList<>();
        this.optimizationStrategy = EnumBoxOptimizationStrategy.REMOVE_DUPLICATE_LINES;
    }

    public BlockGroup setItemIcon(ItemStack itemIcon) {
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

    public String getId() {
        return id;
    }

    public ItemStack getItemIcon() {
        return this.itemIcon;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public boolean containsBlockState(IBlockState state) {
        //Logz.info("Checking whether %s is in %s", state, this.validStates);
        return this.validStates.contains(state.getBlock().getDefaultState());
    }

    public BlockGroup setValidBlockStates(List<IBlockState> blocks) {
        this.validStates.addAll(blocks);
        return this;
    }
}
