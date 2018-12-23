package org.dave.pipemaster.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.dave.pipemaster.PipeMaster;
import org.dave.pipemaster.items.goggles.network.PipeGogglesUpdateBlockGroupMessage;
import org.dave.pipemaster.items.goggles.network.PipeGogglesUpdateBlockGroupMessageHandler;
import org.dave.pipemaster.items.goggles.network.PipeGogglesUpdateRangeMessage;
import org.dave.pipemaster.items.goggles.network.PipeGogglesUpdateRangeMessageHandler;

public class PipeMasterNetworkHandler {
    public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(PipeMaster.MODID);

    public static void init() {
        instance.registerMessage(PipeGogglesUpdateBlockGroupMessageHandler.class, PipeGogglesUpdateBlockGroupMessage.class, 1, Side.SERVER);
        instance.registerMessage(PipeGogglesUpdateRangeMessageHandler.class, PipeGogglesUpdateRangeMessage.class, 2, Side.SERVER);
    }
}
