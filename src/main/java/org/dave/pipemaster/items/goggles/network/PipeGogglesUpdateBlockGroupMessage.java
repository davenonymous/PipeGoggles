package org.dave.pipemaster.items.goggles.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PipeGogglesUpdateBlockGroupMessage implements IMessage {
    int slot;
    int groupNum;
    String groupId;

    public PipeGogglesUpdateBlockGroupMessage() {
    }

    public PipeGogglesUpdateBlockGroupMessage(int slot, int groupNum, String groupId) {
        this.slot = slot;
        this.groupNum = groupNum;
        this.groupId = groupId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.slot = buf.readInt();
        this.groupNum = buf.readInt();
        this.groupId = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(slot);
        buf.writeInt(groupNum);
        ByteBufUtils.writeUTF8String(buf, groupId);
    }
}
