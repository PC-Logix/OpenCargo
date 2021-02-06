package com.pclogix.opencargo.common.tileentity;

import com.pclogix.opencargo.common.container.ItemWriterInventory;
import com.pclogix.opencargo.common.items.ItemCard;
import com.pclogix.opencargo.common.items.ItemTag;
import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Visibility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.awt.*;

public class TagWriterTileEntity extends TileEntityOCBase implements ITickable {

    public static final int SIZE = 2;
    public boolean hasCards = false;

    private ItemWriterInventory inventory;


    public TagWriterTileEntity() {
        super("oc_tagwriter");
        this.inventory = new ItemWriterInventory(SIZE);
        node = Network.newNode(this, Visibility.Network).withComponent(getComponentName()).withConnector(32).create();
    }

    @Override
    public void update() {
        super.update();
        if (!hasCards && !inventory.getStackInSlot(0).isEmpty()) {
            hasCards = true;
            if (node != null)
                node.sendToReachable("computer.signal", "writerTagInsert", inventory.getStackInSlot(0).getCount());
        }

        if (hasCards && inventory.getStackInSlot(0).isEmpty()) {
            hasCards = false;
            if (node != null)
                node.sendToReachable("computer.signal", "writerTagRemove", "writerTagRemove");
        }
    }



    @Callback(doc = "function(string: data, string: displayName, int: count, int: color):string; writes data to the tag, 128 characters, the rest is silently discarded, 2nd argument will change the displayed name of the tag in your inventory. if you pass an integer to the 3rd argument you can craft up to 64 at a time, the 4th argument will set the color of the card, use OC's color api.", direct = true)
    public Object[] write(Context context, Arguments args) {
        String data = args.checkString(0);

        Integer count = args.optInteger(2, 1);

        if (data == null)
            return new Object[] { false, "Data is Null" };

        if (node.changeBuffer(-5) != 0)
            return new Object[] { false, "Not enough power in OC Network." };

        if (inventory.getStackInSlot(0).isEmpty())
            return new Object[] { false, "No card in slot" };

        if (inventory.getStackInSlot(0).getCount() - count < 0) {
            return new Object[] { false, "Not enough tags" };
        }

        if (inventory.getStackInSlot(1).getCount() >= (65 - count)) {
            return new Object[] { false, "Not enough empty slots" };
        }


        String title = args.optString(1, "");

        int colorIndex = Math.max(0, Math.min(args.optInteger(3, 0), 15));

        float dyeColor[] = EnumDyeColor.byMetadata(colorIndex).getColorComponentValues();
        int color = new Color(dyeColor[0], dyeColor[1], dyeColor[2]).getRGB();

        ItemStack outStack;

        if (inventory.getStackInSlot(0).getItem() instanceof ItemCard) {
            outStack = new ItemStack(inventory.getStackInSlot(0).getItem());
            if (data.length() > 64) {
                data = data.substring(0, 64);
            }
        }  else
            return new Object[] { false, "Wrong item in input slot" };

        ItemTag.CardTag cardTag = new ItemTag.CardTag(inventory.getStackInSlot(0));

        cardTag.color = color;
        cardTag.dataTag = data;

        outStack.setTagCompound(cardTag.writeToNBT(new NBTTagCompound()));

        if (!title.isEmpty()) {
            outStack.setStackDisplayName(title);
        }

        inventory.getStackInSlot(0).setCount(inventory.getStackInSlot(0).getCount() - count);
        if (inventory.getStackInSlot(1).getCount() > 0) {
            inventory.getStackInSlot(1).setCount(inventory.getStackInSlot(1).getCount() + count);
        } else {
            inventory.setStackInSlot(1, outStack);
            if (count > 1) {
                inventory.getStackInSlot(1).setCount(inventory.getStackInSlot(1).getCount() + (count -1));
            }
        }

        return new Object[] { true };
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        if (data.hasKey("items")) {
            //itemStackHandler.deserializeNBT((NBTTagCompound) data.getTag("items"));
        }

        inventory.deserializeNBT(data.getCompoundTag("inv"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setTag("inv", inventory.serializeNBT());
        //data.setTag("items", itemStackHandler.serializeNBT());
        return data;
    }

    // This item handler will hold our nine inventory slots
    private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
            TagWriterTileEntity.this.markDirty();
        }
    };

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
            return (T) inventory;
        }

        return super.getCapability(capability, facing);
    }

    public ItemWriterInventory getInventory() {
        return this.inventory;
    }
}