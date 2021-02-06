package com.pclogix.opencargo.common.container;

import com.pclogix.opencargo.common.items.ItemCard;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ItemReaderInventory extends ItemStackHandler {

    public ItemReaderInventory(final int size) {
        super(size);
    }

    public ItemReaderInventory(final NonNullList<ItemStack> stacks) {
        super(stacks);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if(!(stack.getItem() instanceof ItemCard)) {
            return stack;
        }
        if (stack.getTagCompound() == null) {
            return stack;
        }
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack){
        switch(slot){
            case 0: return stack.getItem() instanceof ItemCard;
            default: return false;
        }
    }

    @Override
    public void onContentsChanged(int slot){
        super.onContentsChanged(slot);
        //updateStackTags(getStackInSlot(slot));
    }



    public void dropItems(final World world, final double x, final double y, final double z) {
        for (int i = 0; i < this.stacks.size(); i++) {
            if (this.stacks.get(i).isEmpty()) {
                continue;
            }

            final EntityItem entityitem = new EntityItem(world, x, y, z, this.stacks.get(i));
            entityitem.setDefaultPickupDelay();
            world.spawnEntity(entityitem);

            this.stacks.set(i, ItemStack.EMPTY);
        }
    }

    public NonNullList<ItemStack> getStacks() {
        return this.stacks;
    }

}