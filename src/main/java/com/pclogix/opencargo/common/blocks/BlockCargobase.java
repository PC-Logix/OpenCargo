package com.pclogix.opencargo.common.blocks;

import com.pclogix.opencargo.OpenCargo;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/* implements facing blockstate */
public abstract class BlockCargobase extends Block implements ITileEntityProvider /*, IInventory*/ {
    protected Random random;

    public static final PropertyDirection PROPERTYFACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    protected BlockCargobase(String name, Material material)
    {
        super(material);

        this.setRegistryName(OpenCargo.MODID, name);
        this.setUnlocalizedName(this.getRegistryName().toString());
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getHorizontal(meta);
        return getDefaultState().withProperty(PROPERTYFACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int facingbits = getFacing(state).getHorizontalIndex();
        return facingbits;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PROPERTYFACING);
    }

    @Override
    @Deprecated
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing blockFaceClickedOn, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing enumfacing = (placer == null) ? EnumFacing.NORTH : EnumFacing.fromAngle(placer.rotationYaw);
        return getDefaultState().withProperty(PROPERTYFACING, enumfacing);
    }

    public static EnumFacing getFacing(IBlockState state) {
        return state.getValue(PROPERTYFACING);
    }
}
