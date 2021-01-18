package com.pclogix.opencargo.common.gui;

import com.pclogix.opencargo.OpenCargo;
import com.pclogix.opencargo.common.container.TagReaderContainer;
import com.pclogix.opencargo.common.container.TagWriterContainer;
import com.pclogix.opencargo.common.tileentity.TagReaderTileEntity;
import com.pclogix.opencargo.common.tileentity.TagWriterTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class TagReaderGui extends GuiContainer {
    public static final int WIDTH = 175;
    public static final int HEIGHT = 195;

    private static final ResourceLocation background = new ResourceLocation(OpenCargo.MODID, "textures/gui/tagreader.png");

    public TagReaderGui(TagReaderTileEntity tileEntity, TagReaderContainer container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}