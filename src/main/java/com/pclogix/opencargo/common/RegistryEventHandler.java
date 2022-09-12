package com.pclogix.opencargo.common;

import com.pclogix.opencargo.OpenCargo;
import com.pclogix.opencargo.common.blocks.ModBlocks;
import com.pclogix.opencargo.common.blocks.TagReaderBlock;
import com.pclogix.opencargo.common.blocks.TagWriterBlock;
import com.pclogix.opencargo.common.items.ModItems;
import com.pclogix.opencargo.common.tileentity.TagReaderTileEntity;
import com.pclogix.opencargo.common.tileentity.TagWriterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class RegistryEventHandler
{
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().registerAll(ModBlocks.BLOCKS);

        registerTileEntity(TagWriterTileEntity.class, TagWriterBlock.NAME);
        registerTileEntity(TagReaderTileEntity.class, TagReaderBlock.NAME);
        OpenCargo.LOGGER.info("Registered blocks");
    }

    private static void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String key) {
        // For better readability
        GameRegistry.registerTileEntity(tileEntityClass, new ResourceLocation(OpenCargo.MODID, key));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(ModItems.ITEMS);

        for (Block block : ModBlocks.BLOCKS)
        {
            event.getRegistry().register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }

        OpenCargo.LOGGER.info("Registered items");
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        for (Block block: ModBlocks.BLOCKS)
        {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
        }

        for (Item item: ModItems.ITEMS)
        {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }

        OpenCargo.LOGGER.info("Registered models");
    }
}