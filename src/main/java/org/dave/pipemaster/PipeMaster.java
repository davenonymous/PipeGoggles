package org.dave.pipemaster;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.dave.pipemaster.commands.CommandPipeMaster;
import org.dave.pipemaster.data.blockgroups.BlockGroupRegistry;
import org.dave.pipemaster.data.config.ConfigurationHandler;
import org.dave.pipemaster.misc.PipeMasterCreativeTab;
import org.dave.pipemaster.base.BaseEvents;
import org.dave.pipemaster.proxy.CommonProxy;
import org.dave.pipemaster.util.Logz;

@Mod(
        name = "Pipe Master",
        modid = PipeMaster.MODID,
        version = PipeMaster.VERSION,
        guiFactory = PipeMaster.GUI_FACTORY,
        acceptedMinecraftVersions = "[1.12,1.13)",
        dependencies = "after:*"
)
public class PipeMaster {
    public static final String MODID = "pipemaster";
    public static final String VERSION = "1.0.1";
    public static final String GUI_FACTORY = "org.dave.pipemaster.misc.ConfigGuiFactory";

    public static final PipeMasterCreativeTab CREATIVE_TAB = new PipeMasterCreativeTab();
    public static BlockGroupRegistry blockGroupRegistry;

    @Mod.Instance(PipeMaster.MODID)
    public static PipeMaster instance;

    @SidedProxy(clientSide = "org.dave.pipemaster.proxy.ClientProxy", serverSide = "org.dave.pipemaster.proxy.ServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Logz.logger = event.getModLog();

        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        MinecraftForge.EVENT_BUS.register(ConfigurationHandler.class);
        MinecraftForge.EVENT_BUS.register(BaseEvents.class);

        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        this.blockGroupRegistry = new BlockGroupRegistry();

        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandPipeMaster());
    }
}
