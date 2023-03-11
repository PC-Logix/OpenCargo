package com.pclogix.opencargo.common.tileentity;

import com.pclogix.opencargo.common.container.ItemWriterInventory;
import com.pclogix.opencargo.common.items.ItemTag;
import com.pclogix.opencargo.common.items.ModItems;
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

    public static final int SIZE = 9;
    public boolean hasCards = false;

    private ItemWriterInventory inventory;
    private String password = "";

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

    public String getPass() {
        return this.password;
    }

    @Callback
    public Object[] removePassword(Context context, Arguments args) {
            if (args.checkString(0).equals(getPass())) {
                setPassword("");
                return new Object[] { true, "Password Removed" };
            } else {
                return new Object[] { false, "Password was not removed" };
            }
    }

    @Callback
    public Object[] setPassword(Context context, Arguments args) {
            if (getPass().isEmpty()) {
                setPassword(args.checkString(0));

                return new Object[] { true, "Password set" };
            } else {
                if (args.checkString(0).equals(getPass())) {
                    try {
                        setPassword(args.checkString(1));
                        return new Object[]{true, "Password Changed"};
                    } catch (Exception ignored) {}
                }
                return new Object[] { false, "Password was not changed" };
            }
    }


    public void setPassword(String pass) {
        this.password = pass;
    }

    @Callback(doc = "function(string: password, string: cardType, int: slotIndex (1 based just like lua), string: data, string: displayName, int: count, int: color, int: color2, int: color3 Can either use a color index as the first value, or all three values as rgb):bool, int, string; writes data to the tag, 128 characters, the rest is silently discarded, displayName will change the displayed name of the tag in your inventory. if you pass an integer to count you can craft up to 64 at a time, color will set the color of the item using OC's color api.", direct = true)
    public Object[] write(Context context, Arguments args) {
        String password = args.checkString(0);
        String cardType = args.checkString(1);
        Integer slotIndex = args.checkInteger(2) - 1;
        String data = args.checkString(3);
        String title = args.optString(4, "");
        Integer count = args.optInteger(5, 1);
        Integer color1 = Math.max(0, Math.min(255, args.optInteger(6, 0))); // If either color2 and color3 are unset this should be a color index
        Integer color2 = Math.min(255, args.optInteger(7, -1));
        Integer color3 = Math.min(255, args.optInteger(8, -1));

        boolean colorIsIndex = false;
        if (color2 == -1 || color3 == -1) {
            color1 = Math.min(15, color1); // If color index clamp to max 15
            colorIsIndex = true;
        }

        if (!password.equals(getPass()))
            return new Object[] { false, 0, "Password mismatch" };

        if (data == null)
            return new Object[] { false, 0, "Data is Null" };

        if (node.changeBuffer(-5) != 0)
            return new Object[] { false, 0, "Not enough power in OC Network." };

        if (inventory.getStackInSlot(slotIndex).getCount() >= 64) {
            return new Object[] { true, 0, "Slot is full" };
        }

        int color;
        if (!colorIsIndex) {
            color = new Color(color1, color2, color3).getRGB();
        } else {
            float[] dyeColor = EnumDyeColor.byMetadata(color1).getColorComponentValues();
            color = new Color(dyeColor[0], dyeColor[1], dyeColor[2]).getRGB();
        }

        ItemStack outStack;
            if (cardType.equals("boxed")) {
                System.out.println("boxed");
                outStack = new ItemStack(ModItems.ITEMS[0]);
            } else if (cardType.equals("bulk")) {
                System.out.println("bulk");
                outStack = new ItemStack(ModItems.ITEMS[1]);
            } else if (cardType.equals("cooled")) {
                System.out.println("cooled");
                outStack = new ItemStack(ModItems.ITEMS[2]);
            } else if (cardType.equals("fluid")) {
                System.out.println("fluid");
                outStack = new ItemStack(ModItems.ITEMS[3]);
            } else if (cardType.equals("living")) {
                System.out.println("living");
                outStack = new ItemStack(ModItems.ITEMS[4]);
            } else if (cardType.equals("long")) {
                System.out.println("long");
                outStack = new ItemStack(ModItems.ITEMS[5]);
            } else if (cardType.equals("long2")) {
                System.out.println("long2");
                outStack = new ItemStack(ModItems.ITEMS[6]);
            } else {
                return new Object[] { false, 0, "invalid tag name passed" };
            }

            if (data.length() > 128) {
                data = data.substring(0, 128);
            }


        ItemTag.CardTag cardTag = new ItemTag.CardTag(inventory.getStackInSlot(slotIndex));

        cardTag.color = color;
        cardTag.dataTag = data;

        outStack.setTagCompound(cardTag.writeToNBT(new NBTTagCompound()));

        if (!title.isEmpty()) {
            outStack.setStackDisplayName(title);
        }

        outStack.setCount(count);

        //inventory.getStackInSlot(0).setCount(inventory.getStackInSlot(0).getCount() - count);

        if (inventory.getStackInSlot(slotIndex).isEmpty() || (inventory.getStackInSlot(slotIndex).getItem().getUnlocalizedName().equals(outStack.getUnlocalizedName()) && (inventory.getStackInSlot(slotIndex).getCount() > 0))) {
            ItemStack insert = inventory.insertItem(slotIndex, outStack, false);
            if (count.equals(insert.getCount())) { // If the inserted amount equals the requested amount return -1 ???
                return new Object[] { false, -1, "??" };
            }
            return new Object[] { true, (count - insert.getCount()), "Tags written successfully" };
        } else {
            return new Object[] { false, 0, "slot " + (slotIndex + 1) + " contains " + inventory.getStackInSlot(slotIndex).getItem().getUnlocalizedName() };
        }
    }

    @Callback(doc = "function(int: slotIndex): int; Gets the number of items in the specified slot.", direct = true)
    public Object[] getCountInSlot(Context context, Arguments args) {
        int slotIndex = args.checkInteger(0) -1;
        return new Object[] { inventory.getStackInSlot(slotIndex).getCount() };
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
