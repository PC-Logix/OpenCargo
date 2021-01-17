package com.pclogix.opencargo.common;

import com.pclogix.opencargo.common.blocks.TagWriterBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
    @GameRegistry.ObjectHolder("opencargo:tagwriterblock")
    public static TagWriterBlock tagWriterBlock;

    @SideOnly(Side.CLIENT)
    public static void initModels() {

        tagWriterBlock.initModel();
    }
}
