package com.pclogix.opencargo.proxy;

import com.pclogix.opencargo.common.ContentRegistry;
import com.pclogix.opencargo.common.items.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        super.init();
        Minecraft mc = Minecraft.getMinecraft();
        mc.getItemColors().registerItemColorHandler(new CardColorHandler(), ItemTag.DEFAULTSTACK.getItem());
        mc.getItemColors().registerItemColorHandler(new CardColorHandler(), ItemTagBulk.DEFAULTSTACK.getItem());
        mc.getItemColors().registerItemColorHandler(new CardColorHandler(), ItemTagCooled.DEFAULTSTACK.getItem());
        mc.getItemColors().registerItemColorHandler(new CardColorHandler(), ItemTagFluid.DEFAULTSTACK.getItem());
        mc.getItemColors().registerItemColorHandler(new CardColorHandler(), ItemTagLiving.DEFAULTSTACK.getItem());
        mc.getItemColors().registerItemColorHandler(new CardColorHandler(), ItemTagLong.DEFAULTSTACK.getItem());
        mc.getItemColors().registerItemColorHandler(new CardColorHandler(), ItemTagLong2.DEFAULTSTACK.getItem());
    }

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        MinecraftForge.EVENT_BUS.register(this);
    }



    @Override
    public void registerModels() {
        for(Block block : ContentRegistry.modBlocks)
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName().toString(), "inventory"));

        for(ItemStack itemStack : ContentRegistry.modBlocksWithItem.values())
            ModelLoader.setCustomModelResourceLocation(itemStack.getItem(), 0, new ModelResourceLocation(itemStack.getItem().getRegistryName().toString()));

        for(ItemStack itemStack : ContentRegistry.modItems)
            ModelLoader.setCustomModelResourceLocation(itemStack.getItem(), 0, new ModelResourceLocation(itemStack.getItem().getRegistryName().toString()));
    }

    private static class CardColorHandler implements IItemColor {
        private CardColorHandler() {}

        @Override
        public int colorMultiplier(ItemStack stack, int tintIndex) {
            // TODO Auto-generated method stub
            return tintIndex == 0 ? 0xFFFFFF : new ItemCard.CardTag(stack.getTagCompound()).color;
        }
    }
}