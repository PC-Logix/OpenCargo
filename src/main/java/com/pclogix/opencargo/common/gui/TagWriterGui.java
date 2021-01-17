package com.pclogix.opencargo.common.gui;

import com.pclogix.opencargo.OpenCargo;
import com.pclogix.opencargo.common.container.TagWriterContainer;
import com.pclogix.opencargo.common.tileentity.TagWriterTileEntity;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class TagWriterGui extends GuiContainer {
    public static final int WIDTH = 180;
    public static final int HEIGHT = 152;

    private static final ResourceLocation background = new ResourceLocation(OpenCargo.MODID, "textures/gui/testcontainer.png");

    public TagWriterGui(TagWriterTileEntity tileEntity, TagWriterContainer container) {
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