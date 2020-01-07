package com.davenonymous.pipegoggles;

import com.davenonymous.libnonymous.setup.IProxy;
import com.davenonymous.pipegoggles.proxy.ProxyClient;
import com.davenonymous.pipegoggles.proxy.ProxyServer;
import com.davenonymous.pipegoggles.setup.Config;
import com.davenonymous.pipegoggles.setup.ModSetup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(PipeGoggles.MODID)
public class PipeGoggles {
    public static final String MODID = "pipegoggles";

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ProxyClient(), () -> () -> new ProxyServer());
    public static ModSetup setup = new ModSetup();

    public PipeGoggles() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("pipegoggles-client.toml"));
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("pipegoggles-common.toml"));
    }

    private void setup(final FMLCommonSetupEvent event) {
        setup.init();
        proxy.init();
    }
}
