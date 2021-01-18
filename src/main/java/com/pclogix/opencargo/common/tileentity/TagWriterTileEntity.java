package com.pclogix.opencargo.common.tileentity;

import com.pclogix.opencargo.OpenCargo;
import com.pclogix.opencargo.common.items.ItemTag;
import li.cil.oc.api.Network;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.Visibility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.awt.*;

public class TagWriterTileEntity extends TileEntityOCBase implements ITickable {

    public static final int SIZE = 2;
    public boolean hasCards = false;

    private ItemWriterInventory inventoryInput = new ItemWriterInventory();
    private ItemStackHandler inventoryOutput = new ItemStackHandler(1);

    class ItemWriterInventory extends ItemStackHandler {
        public ItemWriterInventory(){
            super(1);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if(!(stack.getItem() instanceof ItemTag)) {
                return stack;
            }
            if (stack.getTagCompound() != null) {
                if (stack.getTagCompound().hasKey("data")) {
                    return stack;
                }
            }
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack){
            switch(slot){
                case 0: return stack.getItem() instanceof ItemTag;
                default: return false;
            }
        }

        @Override
        public void onContentsChanged(int slot){
            super.onContentsChanged(slot);
            //updateStackTags(getStackInSlot(slot));
        }
    }

    public TagWriterTileEntity() {
        super("oc_tagwriter");
        node = Network.newNode(this, Visibility.Network).withComponent(getComponentName()).withConnector(32).create();
    }

    @Override
    public void update() {
        super.update();
        if (!hasCards && !inventoryInput.getStackInSlot(0).isEmpty()) {
            hasCards = true;
            if (node != null)
                node.sendToReachable("computer.signal", "writerTagInsert", inventoryInput.getStackInSlot(0).getCount());
        }

        if (hasCards && inventoryInput.getStackInSlot(0).isEmpty()) {
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

        if (inventoryInput.getStackInSlot(0).isEmpty())
            return new Object[] { false, "No card in slot" };

        if (inventoryInput.getStackInSlot(0).getCount() - count < 0) {
            return new Object[] { false, "Not enough tags" };
        }

        if (inventoryOutput.getStackInSlot(0).getCount() >= (65 - count)) {
            return new Object[] { false, "Not enough empty slots" };
        }


        String title = args.optString(1, "");

        int colorIndex = Math.max(0, Math.min(args.optInteger(3, 0), 15));

        float dyeColor[] = EnumDyeColor.byMetadata(colorIndex).getColorComponentValues();
        int color = new Color(dyeColor[0], dyeColor[1], dyeColor[2]).getRGB();

        ItemStack outStack;

        if (inventoryInput.getStackInSlot(0).getItem() instanceof ItemTag) {
            outStack = new ItemStack(ItemTag.DEFAULTSTACK.getItem());
            if (data.length() > 64) {
                data = data.substring(0, 64);
            }
        }  else
            return new Object[] { false, "Wrong item in input slot" };

        ItemTag.CardTag cardTag = new ItemTag.CardTag(inventoryInput.getStackInSlot(0));

        cardTag.color = color;
        cardTag.dataTag = data;

        outStack.setTagCompound(cardTag.writeToNBT(new NBTTagCompound()));

        if (!title.isEmpty()) {
            outStack.setStackDisplayName(title);
        }

        inventoryInput.getStackInSlot(0).setCount(inventoryInput.getStackInSlot(0).getCount() - count);
        if (inventoryOutput.getStackInSlot(0).getCount() > 0) {
            inventoryOutput.getStackInSlot(0).setCount(inventoryOutput.getStackInSlot(0).getCount() + count);
        } else {
            inventoryOutput.setStackInSlot(0, outStack);
            if (count > 1) {
                inventoryOutput.getStackInSlot(0).setCount(inventoryOutput.getStackInSlot(0).getCount() + (count -1));
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

        inventoryInput.deserializeNBT(data.getCompoundTag("invIn"));
        inventoryOutput.deserializeNBT(data.getCompoundTag("invOut"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setTag("invIn", inventoryInput.serializeNBT());
        data.setTag("invOut", inventoryOutput.serializeNBT());
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
            if(facing.equals(EnumFacing.DOWN))
                return (T) inventoryOutput;
            else
                return (T) inventoryInput;
        }

        return super.getCapability(capability, facing);
    }
}