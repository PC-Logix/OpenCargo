package com.pclogix.opencargo.common.items;

import com.pclogix.opencargo.OpenCargo;
import net.minecraft.item.Item;

public abstract class ItemOCBase extends Item {

    ItemOCBase(String name) {
        setUnlocalizedName("opencargo." + name);
        setRegistryName(OpenCargo.MODID, name);
        //setCreativeTab(ContentRegistry.creativeTab);
    }
}
