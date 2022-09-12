package com.pclogix.opencargo.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModBlocks
{
    public static final Block[] BLOCKS = {
            new TagReaderBlock("tagreaderblock", Material.IRON),
            new TagWriterBlock("tagwriterblock", Material.IRON)
    };
}