package com.davenonymous.pipegoggles.item;

import com.davenonymous.libnonymous.base.BaseNBTSerializable;
import com.davenonymous.libnonymous.serialization.Store;
import com.davenonymous.pipegoggles.data.BlockGroup;
import com.davenonymous.pipegoggles.data.BlockGroupHelper;
import com.davenonymous.pipegoggles.setup.Registration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.ArrayList;
import java.util.List;

public class PipeGogglesItemData extends BaseNBTSerializable {
    @Store
    public List<ResourceLocation> slots;

    @Store
    public boolean enabled;

    @Store
    public long storedPower;

    @Store
    public int range;

    public PipeGogglesItemData() {
        super();
        slots = new ArrayList<>();
        enabled = true;
        storedPower = 0;
        range = 4;

        slots.add(0, BlockGroupHelper.emptyBlockGroup.getId());
        slots.add(1, BlockGroupHelper.emptyBlockGroup.getId());
        slots.add(2, BlockGroupHelper.emptyBlockGroup.getId());
        slots.add(3, BlockGroupHelper.emptyBlockGroup.getId());
    }

    public PipeGogglesItemData(CompoundTag nbt) {
        this();
        this.deserializeNBT(nbt);
    }

    public PipeGogglesItemData(ItemStack stack) {
        this();
        if(!stack.hasTag()) {
            return;
        }

        this.deserializeNBT(stack.getTag());
    }

    public BlockGroup getBlockGroupForSlot(RecipeManager recipeManager, int blockGroupIndex) {
        if(blockGroupIndex < 0 || blockGroupIndex >= slots.size()) {
            return BlockGroupHelper.emptyBlockGroup;
        }

        ResourceLocation groupId = slots.get(blockGroupIndex);
        return BlockGroupHelper.getBlockGroup(recipeManager, groupId);
    }

    public ItemStack createItemStack() {
        ItemStack result = new ItemStack(Registration.PIPE_GOOGLES.get(), 1);
        result.setTag(this.serializeNBT());
        return result;
    }

    public void setGroupForSlot(int slot, ResourceLocation group) {
        if(slot >= slots.size()) {
            return;
        }

        slots.set(slot, group);
    }
}