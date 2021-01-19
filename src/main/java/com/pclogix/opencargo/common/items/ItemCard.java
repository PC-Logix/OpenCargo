package com.pclogix.opencargo.common.items;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class ItemCard extends ItemOCBase {

    ItemCard(String name) {
        super(name);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.SUCCESS;
    }

    private boolean isBulk(ItemStack stack) {
        return getTagCompoundSafe(stack).hasKey("bulk");
    }

    private NBTTagCompound getTagCompoundSafe(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
            stack.setTagCompound(tagCompound);
        }
        return tagCompound;
    }

    public static class CardTag{
        public boolean locked = false;
        public String localUUID = UUID.randomUUID().toString();
        public String dataTag = "";
        public int color = 0xFFFFFF;

        public boolean isValid;

        public CardTag(ItemStack stack){
            if(stack.getItem() instanceof ItemCard)
                readFromNBT(stack.getTagCompound());
        }

        public CardTag(NBTTagCompound nbt){
            readFromNBT(nbt);
        }

        public void readFromNBT(NBTTagCompound nbt){
            if(nbt != null) {

                if (nbt.hasKey("data"))
                    dataTag = nbt.getString("data");

                if (nbt.hasKey("locked"))
                    locked = nbt.getBoolean("locked");

                if (nbt.hasKey("display", 10)) {
                    NBTTagCompound displayTag = nbt.getCompoundTag("display");
                    if (displayTag.hasKey("color", 3)) {
                        color = displayTag.getInteger("color");
                    }
                }
            }

            isValid = dataTag.length() > 0;
        }

        public NBTTagCompound writeToNBT(NBTTagCompound nbt){
            nbt.setString("data", dataTag);
            nbt.setBoolean("locked", locked);

            NBTTagCompound displayTag = new NBTTagCompound();
            displayTag.setInteger("color", color);

            nbt.setTag("display", displayTag);

            return nbt;
        }

    }

}

