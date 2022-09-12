package com.pclogix.opencargo.common.items;

import net.minecraft.item.ItemStack;

public class ItemTagBulk extends ItemCard {
    public static final String NAME = "itemtagbulk";
    public static ItemStack DEFAULTSTACK;

    public ItemTagBulk(String name) {
        super(name);
    }

    public String getName() {
        return NAME;
    }
}
