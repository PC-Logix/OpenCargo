package com.pclogix.opencargo.common.items;

import com.pclogix.opencargo.OpenCargo;
import net.minecraft.item.ItemStack;

public class ItemTagLong2 extends ItemCard {
    public static final String NAME = "itemtaglong2";
    public static ItemStack DEFAULTSTACK;

    public ItemTagLong2(String name) {
        super(name);
        setCreativeTab(OpenCargo.CreativeTab);
    }
}
