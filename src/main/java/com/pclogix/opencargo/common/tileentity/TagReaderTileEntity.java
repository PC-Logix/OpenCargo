package com.pclogix.opencargo.common.tileentity;

import com.pclogix.opencargo.common.container.ItemReaderInventory;
import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Visibility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TagReaderTileEntity extends TileEntityOCBase implements ITickable {

    public static final int SIZE = 1;
    public boolean hasCards = false;
    private ItemReaderInventory inventoryInput;

    public ItemReaderInventory getInventory() {
        return this.inventoryInput;
    }

    public TagReaderTileEntity() {
        super("oc_tagreader");
        this.inventoryInput = new ItemReaderInventory(SIZE);
        node = Network.newNode(this, Visibility.Network).withComponent(getComponentName()).withConnector(32).create();
    }

    @Callback
    public Object[] read(Context context, Arguments args) {

        Boolean delete = args.optBoolean(0, false);
        if (!inventoryInput.getStackInSlot(0).isEmpty()) {
            ItemStack cardData = inventoryInput.getStackInSlot(0);
            if (delete) {
                System.out.println("trying to delete");
                inventoryInput.setStackInSlot(0, new ItemStack(Items.AIR));
            }
            return new Object[] { true, cardData.getTagCompound().getString("data"), cardData.getCount(), cardData.getItem().getRegistryName() };
        }


        return new Object[] { false, "No tag" };
    }

    @Override
    public void update() {
        super.update();
        if (!hasCards && !inventoryInput.getStackInSlot(0).isEmpty()) {
            hasCards = true;
            if (node != null)
                node.sendToReachable("computer.signal", "readerTagInsert", inventoryInput.getStackInSlot(0).getCount());
        }

        if (hasCards && inventoryInput.getStackInSlot(0).isEmpty()) {
            hasCards = false;
            if (node != null)
                node.sendToReachable("computer.signal", "readerTagRemove", "readerTagRemove");
        }
    }


    // This item handler will hold our nine inventory slots
    private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
            TagReaderTileEntity.this.markDirty();
        }
    };

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("items")) {
            inventoryInput.deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("items", inventoryInput.serializeNBT());
        return compound;
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (facing != null && capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) {
            return (T) this.getInventory();
        }
        return super.getCapability(capability, facing);
    }
}