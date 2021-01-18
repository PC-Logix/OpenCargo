package com.pclogix.opencargo;

import akka.event.Logging;
import com.pclogix.opencargo.common.ContentRegistry;
import com.pclogix.opencargo.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@Mod.EventBusSubscriber
@Mod(modid = OpenCargo.MODID, name = OpenCargo.NAME, version = OpenCargo.VERSION)
public
class OpenCargo
{
    public static final String MODID = "opencargo";
    public static final String NAME = "Open Cargo";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = "com.pclogix.opencargo.proxy.ClientProxy", serverSide = "com.pclogix.opencargo.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static OpenCargo instance;

    public static final Logger LOGGER = LogManager.getLogger(OpenCargo.MODID);

    public static ContentRegistry contentRegistry = new ContentRegistry();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("FMLPreInitializationEvent");
        proxy.preInit(event);
        ContentRegistry.preInit();
        MinecraftForge.EVENT_BUS.register(contentRegistry);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
        ContentRegistry.init();
        LOGGER.info("FMLInitializationEvent");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
        LOGGER.info("FMLPostInitializationEvent");
    }

    @SubscribeEvent
    public static void onRegisterModels(ModelRegistryEvent event) {
        proxy.registerModels();
    }
}
