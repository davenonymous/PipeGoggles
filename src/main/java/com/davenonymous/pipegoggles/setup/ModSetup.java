package com.davenonymous.pipegoggles.setup;

import com.davenonymous.pipegoggles.network.Networking;
import net.minecraftforge.common.MinecraftForge;

public class ModSetup {
    public void init() {
        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
        Networking.registerMessages();
    }
}
