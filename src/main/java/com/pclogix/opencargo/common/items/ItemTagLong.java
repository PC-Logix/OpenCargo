package com.pclogix.opencargo.common.items;

import com.pclogix.opencargo.OpenCargo;
import net.minecraft.item.ItemStack;

public class ItemTagLong extends ItemCard {
    public static final String NAME = "itemtaglong";
    public static ItemStack DEFAULTSTACK;

    public ItemTagLong(String name) {
        super(name);
        setCreativeTab(OpenCargo.CreativeTab);
    }
}
