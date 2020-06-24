package com.mrghastien.quantum_machinery.client.screens;

import java.util.Collections;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.common.blocks.blaster.BlasterContainer;
import com.mrghastien.quantum_machinery.common.blocks.blaster.BlasterTile;
import com.mrghastien.quantum_machinery.util.helpers.Units;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlasterScreen extends MachineScreen<BlasterContainer, BlasterTile> implements IHeatScreen {
	
	private double temp, maxTemp;
	
    private ResourceLocation GUI = new ResourceLocation(QuantumMachinery.MODID, "textures/gui/blaster_gui.png");
	private final int HEAT_WIDTH = 5;
	private final int HEAT_HEIGHT = 65;
	private final int ENERGY_WIDTH = 9;
	private final int ENERGY_HEIGHT = 70;

	public BlasterScreen(BlasterContainer screenContainer, PlayerInventory inv, ITextComponent titleIn, BlasterTile tileEntity) {
		super(screenContainer, inv, titleIn, tileEntity);
		this.xSize = 204;
	}

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        
		if (mouseX > guiLeft + 183 && mouseX < guiLeft + 183 + HEAT_WIDTH && mouseY > guiTop + 8
				&& mouseY < guiTop + 8 + HEAT_HEIGHT
				|| mouseX > guiLeft + 174 && mouseX < guiLeft + 174 + 23 && mouseY > guiTop + 70
						&& mouseY < guiTop + 70 + 22) {
			super.renderTooltip(Collections.singletonList("Heat : " + String.format("%3.2f", temp) + " / " + maxTemp + Units.TEMP), mouseX, mouseY, this.font);
		}
		if (mouseX > guiLeft + 152 && mouseX < guiLeft + 152 + ENERGY_WIDTH && mouseY > guiTop + 8
				&& mouseY < guiTop + 8 + ENERGY_HEIGHT) {
			super.renderTooltip(Collections.singletonList("Energy : " + energy + " / " + capacity + Units.ENERGY), mouseX, mouseY, this.font);
		}
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    	String s = "Blaster";
    	this.font.drawString(s, (float)(this.xSize / 2 - this.font.getStringWidth(s) / 2), 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
        
        int h = this.getHeatScaled(70);
        //this.blit(this.guiLeft + 80, this.guiTop + 17 + 114 - e, 176, 114 - e, 16, 114 - e);
        this.blit(this.guiLeft + 183, this.guiTop + 8, 230, 0, 5, 65 - h);
        
        int c = this.getWorkTimerScaled(18);
        this.blit(this.guiLeft + 121, this.guiTop + 59, 213, 0, 17, 0 + c);
        
        int e = this.getEnergyScaled(70);
        this.blit(this.guiLeft + 152, this.guiTop + 8, 204, 0, 9, 70 - e);
    }

    private int getHeatScaled(int pixels) {
    	double i = this.temp;
    	double c = this.maxTemp;
    	return (int) (c != 0 && i != 0 && i > 20 ? i * pixels / c : 0);
    }

    @Override
    public void syncTemperature(float temp, float capacity) {
    	
    }
    
}
