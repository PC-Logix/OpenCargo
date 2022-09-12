package com.pclogix.opencargo.proxy;

import com.pclogix.opencargo.common.blocks.ModBlocks;
import com.pclogix.opencargo.common.items.ItemCard;
import com.pclogix.opencargo.common.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void init() {
        super.init();
        Minecraft mc = Minecraft.getMinecraft();
        //mc.getItemColors().registerItemColorHandler(new CardColorHandler(), ItemTag.DEFAULTSTACK.getItem());
        mc.getItemColors().registerItemColorHandler(new CardColorHandler(), ModItems.ITEMS[0]);
        mc.getItemColors().registerItemColorHandler(new CardColorHandler(), ModItems.ITEMS[1]);
        mc.getItemColors().registerItemColorHandler(new CardColorHandler(), ModItems.ITEMS[2]);
        mc.getItemColors().registerItemColorHandler(new CardColorHandler(), ModItems.ITEMS[3]);
        mc.getItemColors().registerItemColorHandler(new CardColorHandler(), ModItems.ITEMS[4]);
        mc.getItemColors().registerItemColorHandler(new CardColorHandler(), ModItems.ITEMS[5]);
    }

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void registerModels() {
        for(Block block : ModBlocks.BLOCKS)
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName().toString(), "inventory"));

        //for(ItemStack itemStack : ModBlocks.BLOCKS.modBlocksWithItem.values())
        //    ModelLoader.setCustomModelResourceLocation(itemStack.getItem(), 0, new ModelResourceLocation(itemStack.getItem().getRegistryName().toString()));

        for(Item item : ModItems.ITEMS)
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString()));
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