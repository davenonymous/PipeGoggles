package org.dave.pipemaster.items.goggles.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PipeGogglesUpdateRangeMessage implements IMessage {
    int slot;
    int range;

    public PipeGogglesUpdateRangeMessage() {
    }

    public PipeGogglesUpdateRangeMessage(int slot, int range) {
        this.slot = slot;
        this.range = range;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.slot = buf.readInt();
        this.range = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(slot);
        buf.writeInt(range);
    }
}
