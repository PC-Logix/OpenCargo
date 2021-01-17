package com.pclogix.opencargo.proxy;

import com.pclogix.opencargo.OpenCargo;
import com.pclogix.opencargo.common.ModBlocks;
import com.pclogix.opencargo.common.blocks.TagWriterBlock;
import com.pclogix.opencargo.common.gui.GuiProxy;
import com.pclogix.opencargo.common.tileentity.TagWriterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new TagWriterBlock());
        GameRegistry.registerTileEntity(TagWriterTileEntity.class, OpenCargo.MODID + "_tagwriterblock");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(ModBlocks.tagWriterBlock).setRegistryName(ModBlocks.tagWriterBlock.getRegistryName()));
    }

    public void preInit(FMLPreInitializationEvent event) {
    }

    public void init(FMLInitializationEvent e) {
    }

    public void postInit(FMLPostInitializationEvent e) {
    }

}
