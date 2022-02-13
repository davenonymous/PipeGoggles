package com.davenonymous.pipegoggles;

import com.davenonymous.pipegoggles.config.ClientConfig;
import com.davenonymous.pipegoggles.config.CommonConfig;
import com.davenonymous.pipegoggles.setup.ClientSetup;
import com.davenonymous.pipegoggles.setup.ForgeEventHandlers;
import com.davenonymous.pipegoggles.setup.ModSetup;
import com.davenonymous.pipegoggles.setup.Registration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PipeGoggles.MODID)
public class PipeGoggles {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "pipegoggles";

    public PipeGoggles() {
        ClientConfig.register();
        CommonConfig.register();

        Registration.init();

        MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());

        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
        modbus.addListener(ModSetup::init);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modbus.addListener(ClientSetup::init));
    }
}