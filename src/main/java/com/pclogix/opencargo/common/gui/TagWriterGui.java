package com.pclogix.opencargo.common.gui;

import com.pclogix.opencargo.OpenCargo;
import com.pclogix.opencargo.common.container.TagWriterContainer;
import com.pclogix.opencargo.common.tileentity.TagWriterTileEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class TagWriterGui extends GuiContainer {
    public static final int WIDTH = 175;
    public static final int HEIGHT = 195;

    private static final ResourceLocation background = new ResourceLocation(OpenCargo.MODID, "textures/gui/tagwriter.png");

    public TagWriterGui(TagWriterTileEntity tileEntity, TagWriterContainer container) {
        super(container);

        xSize = WIDTH;
        ySize = HEIGHT;
    }

    void drawCenteredString(String string, int y, int color){
        FontRenderer fr = mc.fontRenderer;
        fr.drawString(string, getXSize()/2 - fr.getStringWidth(string)/2, y, color);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        drawCenteredString(new TextComponentTranslation("opencargo.gui.string.tagslot").getUnformattedText(), 20, 4210752);
        drawCenteredString(new TextComponentTranslation("opencargo.gui.string.tagwriter").getUnformattedText(), 5, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}