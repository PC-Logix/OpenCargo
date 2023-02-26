package com.pclogix.opencargo;

import com.pclogix.opencargo.common.RegistryEventHandler;
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
@Mod(modid = OpenCargo.MODID, name = OpenCargo.NAME, version = OpenCargo.VERSION - OpenCargo.BUILDNUMBER,
        dependencies = "required-after:opencomputers;")
public
class OpenCargo
{
    public static final String MODID = "opencargo";
    public static final String NAME = "Open Cargo";
    public static final String VERSION = "@VERSION@";
	public static final String BUILDNUMBER = "@BUILD@";

    @SidedProxy(clientSide = "com.pclogix.opencargo.proxy.ClientProxy", serverSide = "com.pclogix.opencargo.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static OpenCargo instance;

    public static final Logger LOGGER = LogManager.getLogger(OpenCargo.MODID);
    public static CreativeTabs CreativeTab = new CreativeTab("OpenCargo");

    public static RegistryEventHandler contentRegistry = new RegistryEventHandler();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        LOGGER.info(NAME + " FMLPreInitializationEvent");
        proxy.preInit(e);
        //ContentRegistry.preInit();
        MinecraftForge.EVENT_BUS.register(contentRegistry);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
        //ContentRegistry.init();
        LOGGER.info(NAME + " FMLInitializationEvent");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
        LOGGER.info(NAME + " FMLPostInitializationEvent");
    }

    @SubscribeEvent
    public static void onRegisterModels(ModelRegistryEvent event) {
        proxy.registerModels();
    }

    //public static class ConditionFactory implements IConditionFactory
    //{
    //    @Override
    //    public BooleanSupplier parse(JsonContext context, JsonObject json)
    //    {
    //        return () -> false;
    //    }
    //}
}
