package com.pclogix.opencargo.proxy;

import com.pclogix.opencargo.OpenCargo;
import com.pclogix.opencargo.common.gui.GuiProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {


    }

    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(OpenCargo.instance, new GuiProxy());
    }

    public void postInit(FMLPostInitializationEvent event) {

    }

    public void registerModels() {
    }
}
