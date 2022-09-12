package com.pclogix.opencargo.common.items;

import com.pclogix.opencargo.OpenCargo;
import net.minecraft.item.ItemStack;

public class ItemTagFluid extends ItemCard {
    public static final String NAME = "itemtagfluid";
    public static ItemStack DEFAULTSTACK;

    public ItemTagFluid(String name) {
        super(name);
        setCreativeTab(OpenCargo.CreativeTab);
    }
}
