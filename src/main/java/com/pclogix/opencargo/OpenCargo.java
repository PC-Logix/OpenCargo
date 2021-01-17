package com.pclogix.opencargo;

import com.pclogix.opencargo.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

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

    private static Logger logger;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}
