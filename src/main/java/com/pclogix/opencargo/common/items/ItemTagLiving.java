package com.pclogix.opencargo.common.items;

import com.pclogix.opencargo.OpenCargo;
import net.minecraft.item.ItemStack;

public class ItemTagLiving extends ItemCard {
    public static final String NAME = "itemtagliving";
    public static ItemStack DEFAULTSTACK;

    public ItemTagLiving(String name) {
        super(name);
        setCreativeTab(OpenCargo.CreativeTab);
    }
}
