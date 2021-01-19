package com.pclogix.opencargo.common.container;

import com.pclogix.opencargo.common.items.ItemCard;
import com.pclogix.opencargo.common.items.ItemTag;
import com.pclogix.opencargo.common.tileentity.TagWriterTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardInputSlot extends BaseSlot implements ISlotToolTip {

    public CardInputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        if(!(stack.getItem() instanceof ItemCard)) {
            return false;
        }
        if (stack.getTagCompound() != null) {
            return false;
        }
        return true;
    }

    @Override
    public List<String> getTooltip(){
        return new ArrayList<>(Arrays.asList("Accepted Items:", "Tag"));
    }
}
