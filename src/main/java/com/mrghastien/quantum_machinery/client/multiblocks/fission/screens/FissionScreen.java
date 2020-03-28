package com.mrghastien.quantum_machinery.client.multiblocks.fission.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.multiblocks.fission.containers.FissionContainer;
import com.mrghastien.quantum_machinery.multiblocks.fission.tileentities.FissionControllerTile;
import com.mrghastien.quantum_machinery.multiblocks.fission.tileentities.FissionControllerTile.RunningState;
import com.mrghastien.quantum_machinery.network.FissionPacket;
import com.mrghastien.quantum_machinery.network.ModNetworking;
import com.mrghastien.quantum_machinery.util.helpers.MathPHelper;
import com.mrghastien.quantum_machinery.util.helpers.Units;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.Button.IPressable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class FissionScreen extends ContainerScreen<FissionContainer> {
	
    private ResourceLocation GUI = new ResourceLocation(QuantumMachinery.MODID, "textures/gui/multiblocks/fission.png");
    
    protected FissionControllerTile tileEntity;
	protected int clientEnergy = 0;
	protected int clientMaxEnergy = 0;
	protected int syncCounter = 0;
    
    private int fuelLevel = 0;
    private int maxCapacity = 0;
    private int powerSurge = 0;
    private int maxSurge = 0;
    private RunningState state = RunningState.STOPPED;
    private RunningState requestedState = RunningState.STOPPED;
    private int formPct = 0;
	private boolean isFormed;

	public FissionScreen(FissionContainer screenContainer, PlayerInventory inv, ITextComponent titleIn, FissionControllerTile tileEntity) {
		super(screenContainer, inv, titleIn);
		this.ySize = 232;
		this.xSize = 208;
	}
	
	@Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relX = (this.width - this.xSize) / 2;
        int relY = (this.height - this.ySize) / 2;
        this.blit(relX, relY, 0, 0, this.xSize, this.ySize);
        syncCounter++;
		syncCounter %= 2;
		if (syncCounter == 0) {
			ModNetworking.MAIN_CHANNEL.sendToServer(new FissionPacket(this.container.getPos(), requestedState));
		}
		if (state == RunningState.STOPPED) {
			addButton(new Button(this.guiLeft + 137, this.guiTop + 123, 64, 16, "Start", new IPressable() {

				@Override
				public void onPress(Button p_onPress_1_) {
					requestedState = RunningState.RUNNING;
				}
			}));
		}
		if (state == RunningState.INSERTING) {
			addButton(new Button(this.guiLeft + 137, this.guiTop + 100, 64, 16, "Stop Insert", new IPressable() {

				@Override
				public void onPress(Button p_onPress_1_) {
					requestedState = RunningState.STOPPED;
				}
			}));
		}
		if (state == RunningState.STOPPED) {
			addButton(new Button(this.guiLeft + 137, this.guiTop + 100, 64, 16, "Insert", new IPressable() {

				@Override
				public void onPress(Button p_onPress_1_) {
					requestedState = RunningState.INSERTING;
				}
			}));
		}
		if (state == RunningState.RUNNING) {
			addButton(new Button(this.guiLeft + 137, this.guiTop + 123, 46, 16, "Stop", new IPressable() {

				@Override
				public void onPress(Button p_onPress_1_) {
					requestedState = RunningState.STOPPED;
				}
			}));
		}
			
		this.blit(this.guiLeft + 194, this.guiTop + 7, 208, 0, 5, 88 - getEnergyScaled(88));
		this.blit(this.guiLeft + 178, this.guiTop + 7, 208, 0, 5, 88 - (int)MathPHelper.scale(fuelLevel, maxCapacity, 88));
		this.blit(this.guiLeft + 162, this.guiTop + 7, 208, 0, 5, 88 - (int)MathPHelper.scale(powerSurge, maxSurge, 88));
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.font.drawString("Status : " + (isFormed ? this.state.getLocalizedName().getFormattedText() : "DISASSEMBLED"), 5, 9, 4210752);
		this.font.drawString("Energy : " + clientEnergy + Units.ENERGY, 5, 18, 4210752);
		this.font.drawString("Fuel : " + fuelLevel + " m^3", 5, 27, 4210752);
		this.font.drawString("Formation : " + formPct + " %", 5, 36, 4210752);
		this.font.drawString("Is formed : " + isFormed, 5, 45, 4210752);
	}
	
	public void sync(int energy, int maxEnergy, int fuelLevel, int maxCapacity, int powerSurge, int maxSurge, RunningState state, int formPct, boolean isFormed) {
		if (energy != clientEnergy) {
			this.clientEnergy = energy;
		}
		if (maxEnergy != clientMaxEnergy) {
			this.clientMaxEnergy = maxEnergy;
		}
		this.fuelLevel = fuelLevel;
		this.maxCapacity = maxCapacity;
		this.powerSurge = powerSurge;
		this.maxSurge = maxSurge;
		this.state = state;
		this.formPct = formPct;
		this.isFormed = isFormed;
	}
	
	protected int getEnergyScaled(int pixels) {
    	int i = clientEnergy;
    	int c = clientMaxEnergy;
    	return c != 0 && i != 0 ? i * pixels / c : 0;
    }
	
	public void setRequestedState(RunningState requestedState) {
		this.requestedState = requestedState;
	}
	
	public RunningState getRequestedState() {
		return requestedState;
	}

}
