package org.dave.pipemaster.data.blockgroups;

import com.google.common.base.Objects;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import org.dave.pipemaster.items.goggles.EnumBoxOptimizationStrategy;

import java.util.HashSet;
import java.util.List;

public class BlockGroup {
    private String id;
    private ItemStack itemIcon;
    private String translationKey;
    private HashSet<IBlockState> validStates;
    private EnumBoxOptimizationStrategy optimizationStrategy;
    private String modId;

    public BlockGroup(String id) {
        this.id = id;
        this.validStates = new HashSet<>();
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

    public String getModId() {
        return modId;
    }

    public BlockGroup setModId(String modId) {
        this.modId = modId;
        return this;
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
}
