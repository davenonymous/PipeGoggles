package org.dave.pipemaster.items.goggles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class PipeGogglesSlotConfiguration implements INBTSerializable<NBTTagCompound> {
    String groupId;

    public PipeGogglesSlotConfiguration(String id) {
        this.groupId = id;
    }

    public PipeGogglesSlotConfiguration(NBTTagCompound tag) {
        this.deserializeNBT(tag);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound result = new NBTTagCompound();
        result.setString("groupId", this.groupId);

        return result;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.groupId = nbt.getString("groupId");
    }
}
