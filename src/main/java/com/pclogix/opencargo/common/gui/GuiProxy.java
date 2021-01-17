package com.pclogix.opencargo.common.gui;

import com.pclogix.opencargo.common.container.TagWriterContainer;
import com.pclogix.opencargo.common.tileentity.TagWriterTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiProxy implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TagWriterTileEntity) {
            return new TagWriterContainer(player.inventory, (TagWriterTileEntity) te);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TagWriterTileEntity) {
            TagWriterTileEntity containerTileEntity = (TagWriterTileEntity) te;
            return new TagWriterGui(containerTileEntity, new TagWriterContainer(player.inventory, containerTileEntity));
        }
        return null;
    }
}
