package org.dave.pipemaster.items.goggles;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import org.dave.pipemaster.PipeMaster;
import org.dave.pipemaster.data.blockgroups.BlockGroup;
import org.dave.pipemaster.init.Itemss;

import java.util.ArrayList;
import java.util.List;

public class PipeGogglesData implements INBTSerializable<NBTTagCompound> {
    List<PipeGogglesSlotConfiguration> slots;
    boolean enabled;
    long storedPower;
    int range;

    public PipeGogglesData() {
        slots = new ArrayList<>();
        enabled = true;
        storedPower = 0;
        range = 4;

        slots.add(0, new PipeGogglesSlotConfiguration("disabled"));
        slots.add(1, new PipeGogglesSlotConfiguration("disabled"));
        slots.add(2, new PipeGogglesSlotConfiguration("disabled"));
        slots.add(3, new PipeGogglesSlotConfiguration("disabled"));
    }

    public PipeGogglesData(NBTTagCompound nbt) {
        this();
        this.deserializeNBT(nbt);
    }

    public PipeGogglesData(ItemStack stack) {
        this();
        if(!stack.hasTagCompound()) {
            return;
        }

        this.deserializeNBT(stack.getTagCompound());
    }

    public boolean isEnabled() {
        return enabled;
    }

    public PipeGogglesData setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public PipeGogglesData toggleEnabled() {
        this.enabled = !this.enabled;
        return this;
    }

    public int getRange() {
        return range;
    }

    public PipeGogglesData setRange(int range) {
        this.range = range;
        return this;
    }

    public void setGroupForSlot(int slot, String group) {
        if(slot >= slots.size()) {
            return;
        }

        slots.set(slot, new PipeGogglesSlotConfiguration(group));
    }

    public BlockGroup getBlockGroupForSlot(int blockGroupIndex) {
        if(blockGroupIndex < 0 || blockGroupIndex >= slots.size()) {
            return PipeMaster.blockGroupRegistry.getEmptyBlockGroup();
        }

        return PipeMaster.blockGroupRegistry.getBlockGroupById(slots.get(blockGroupIndex).groupId);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound result = new NBTTagCompound();
        result.setBoolean("enabled", this.enabled);
        result.setLong("storedRF", this.storedPower);
        result.setInteger("range", this.range);

        NBTTagList tagList = new NBTTagList();
        for(PipeGogglesSlotConfiguration slot : this.slots) {
            tagList.appendTag(slot.serializeNBT());
        }
        result.setTag("slots", tagList);

        return result;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.enabled = nbt.getBoolean("enabled");
        this.storedPower = nbt.getLong("storedRF");
        this.range = nbt.getInteger("range");

        this.slots.clear();
        for(NBTBase listElementBase : nbt.getTagList("slots", Constants.NBT.TAG_COMPOUND)) {
            NBTTagCompound listElementCompound = (NBTTagCompound)listElementBase;
            this.slots.add(new PipeGogglesSlotConfiguration(listElementCompound));
        }
    }

    public ItemStack createItemStack() {
        ItemStack result = new ItemStack(Itemss.pipeGoggles, 1, 0);
        result.setTagCompound(this.serializeNBT());
        return result;
    }


}
