package com.mrghastien.quantum_machinery.client.screens;

import java.util.Collections;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.containers.ElectricFurnaceContainer;
import com.mrghastien.quantum_machinery.tileentities.ElectricFurnaceTile;
import com.mrghastien.quantum_machinery.util.helpers.Units;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ElectricFurnaceScreen extends MachineScreen<ElectricFurnaceContainer, ElectricFurnaceTile> {
	
	private ResourceLocation GUI = new ResourceLocation(QuantumMachinery.MODID, "textures/gui/electric_furnace_gui.png");
	private final int BAR_WIDTH = 3;
	private final int BAR_HEIGHT = 61;
	
	public ElectricFurnaceScreen(ElectricFurnaceContainer screenContainer, PlayerInventory inv, ITextComponent titleIn, ElectricFurnaceTile tileEntity) {
		super(screenContainer, inv, titleIn, tileEntity);
		this.ySize = 176;
	}


	@Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        
        if(mouseX > guiLeft + 43 && mouseX < guiLeft + 43 + BAR_WIDTH && mouseY > guiTop + 17 && mouseY < guiTop + 17 + BAR_HEIGHT) {
        	super.renderTooltip(Collections.singletonList("Energy : " + clientEnergy + " / " + clientMaxEnergy + Units.ENERGY), mouseX, mouseY, this.font);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    	String s = "Electric Furnace";
    	this.font.drawString(s, (float)(this.xSize / 2 - this.font.getStringWidth(s) / 2), 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
        //this.font.drawString("" + this.container.getCounter(), 0, 0, 0xffffff);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    	super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    	RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
        
        if (mouseX > guiLeft + 16 && mouseX < guiLeft + 32 && mouseY > guiTop + 40 && mouseY < guiTop + 56) {
        	this.blit(guiLeft + 16, guiTop + 40, 179, 25, 16, 16);
        } else {
        	this.blit(guiLeft + 16, guiTop + 40, 195, 25, 16, 16);
        }
        
        int c = this.getWorkTimerScaled(26);
        this.blit(this.guiLeft + 86, this.guiTop + 38, 179, 0, 0 + c, 17);
        
        int e = this.getEnergyScaled(61);
        this.blit(this.guiLeft + 43, this.guiTop + 17 + 61 - e, 176, 0, 3, 0 + e);
    }

}
