package com.pclogix.opencargo;

import com.pclogix.opencargo.common.blocks.ModBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTab extends CreativeTabs {
    public CreativeTab(String unlocalizedName) {
        super(unlocalizedName);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(Item.getItemFromBlock(ModBlocks.BLOCKS[0]));
    }
}
