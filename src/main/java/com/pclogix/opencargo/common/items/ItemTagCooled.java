package com.pclogix.opencargo.common.items;

import com.pclogix.opencargo.OpenCargo;
import net.minecraft.item.ItemStack;

public class ItemTagCooled extends ItemCard {
    public static final String NAME = "itemtagcooled";
    public static ItemStack DEFAULTSTACK;

    public ItemTagCooled(String name) {
        super(name);
        setCreativeTab(OpenCargo.CreativeTab);
    }
}
