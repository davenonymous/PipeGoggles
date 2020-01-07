package com.davenonymous.pipegoggles.setup;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class ForgeEventHandlers {
    @SubscribeEvent
    public void serverLoad(FMLServerStartingEvent event) {
        //ModCommands.register(event.getCommandDispatcher());
    }
}
