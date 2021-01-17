package com.pclogix.opencargo.common.blocks;

import com.pclogix.opencargo.OpenCargo;
import com.pclogix.opencargo.common.tileentity.TagWriterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TagWriterBlock extends Block implements ITileEntityProvider {

    public static final int GUI_ID = 1;

    public TagWriterBlock() {
        super(Material.ROCK);
        setUnlocalizedName(OpenCargo.MODID + ".tagwriterblock");
        //setRegistryName("tagwriterblock");
        setRegistryName(new ResourceLocation(OpenCargo.MODID, "tagwriterblock"));
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TagWriterTileEntity();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side,
                                    float hitX, float hitY, float hitZ) {
        // Only execute on the server
        if (world.isRemote) {
            return true;
        }
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TagWriterTileEntity)) {
            return false;
        }
        player.openGui(OpenCargo.instance, GUI_ID, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
}