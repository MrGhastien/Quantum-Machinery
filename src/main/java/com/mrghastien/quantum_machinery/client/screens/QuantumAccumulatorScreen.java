package com.mrghastien.quantum_machinery.client.screens;

import java.util.Collections;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.common.blocks.accumulator.QuantumAccumulatorContainer;
import com.mrghastien.quantum_machinery.common.blocks.accumulator.QuantumAccumulatorTile;
import com.mrghastien.quantum_machinery.util.helpers.Units;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class QuantumAccumulatorScreen extends MachineScreen<QuantumAccumulatorContainer, QuantumAccumulatorTile>{

	private ResourceLocation GUI = new ResourceLocation(QuantumMachinery.MODID, "textures/gui/quantum_accumulator_gui.png");
	private final int BAR_WIDTH = 16;
	private final int BAR_HEIGHT = 114;
	
	private int in = 0;
	private int out = 0;

	public QuantumAccumulatorScreen(QuantumAccumulatorContainer screenContainer, PlayerInventory inv,ITextComponent titleIn, QuantumAccumulatorTile tileEntity) {
		super(screenContainer, inv, titleIn, tileEntity);
		this.ySize = 232;
	}
	
	 @Override
	 public void render(int mouseX, int mouseY, float partialTicks) {
	        this.renderBackground();
	        super.render(mouseX, mouseY, partialTicks);
	        this.renderHoveredToolTip(mouseX, mouseY);
	        
	        if (mouseX > guiLeft + 80 && mouseX < guiLeft + 80 + BAR_WIDTH && mouseY > guiTop + 17 && mouseY < guiTop + 17 + BAR_HEIGHT) {
	        		super.renderTooltip(Collections.singletonList("Energy: " + tileEntity.getEnergyStorage().getEnergyStored() + "/" + tileEntity.getEnergyStorage().getMaxEnergyStored() + Units.ENERGY), mouseX, mouseY, this.font);
	        }
	    }

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String tileName = "Astral Manipulator";
		this.font.drawString(tileName, (this.xSize / 2 - this.font.getStringWidth(tileName) / 2) - 5, 6, 4210752);
		this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float) (this.ySize - 96 + 2), 4210752);
		int transfert = in - out;
		font.drawStringWithShadow("Transfert : " + transfert, 20, this.guiTop + 30, 0x00FF00);

		// this.font.drawString("Energy : " + energy, 10, 35, 4210752);
	}
	
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    	RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        this.blit(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        	int e = this.getEnergyScaled(114);
        this.blit(this.guiLeft + 80, this.guiTop + 18, 176, 0, 16, 114 - e); 
    }
}
