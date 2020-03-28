package com.mrghastien.quantum_machinery.client.screens;

import com.mrghastien.quantum_machinery.containers.MachineContainer;
import com.mrghastien.quantum_machinery.network.MachineInfoPacket;
import com.mrghastien.quantum_machinery.network.ModNetworking;
import com.mrghastien.quantum_machinery.tileentities.MachineTile;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class MachineScreen<T extends MachineContainer<U>, U extends MachineTile> extends ContainerScreen<T> {

	protected U tileEntity;
	protected int clientEnergy = 0;
	protected int clientMaxEnergy = 0;
	protected int clientWorkTimer = 0;
	protected int clientMaxTimer = 0;
	protected int syncCounter = 0;

	public MachineScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn, U tileEntity) {
		super(screenContainer, inv, titleIn);
		this.tileEntity = tileEntity;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		syncCounter++;
		syncCounter %= 2;
		if (syncCounter == 0) {
			ModNetworking.MAIN_CHANNEL.sendToServer(new MachineInfoPacket(this.container.getPos()));
		}
	}

	protected int getEnergyScaled(int pixels) {
    	int i = clientEnergy;
    	int c = clientMaxEnergy;
    	return c != 0 && i != 0 ? i * pixels / c : 0;
    }
	
    protected int getWorkTimerScaled(int pixels) {
    	int i = this.clientWorkTimer;
    	int c = this.clientMaxTimer;
    	return c != 0 && i != 0 ? i * pixels / c : 0;
    }
	
	public void sync(int energy, int maxEnergy, int workTimer, int maxTimer) {
		if (energy != clientEnergy) {
			this.clientEnergy = energy;
		}
		if (maxEnergy != clientMaxEnergy) {
			this.clientMaxEnergy = maxEnergy;
		}
		if (workTimer != clientWorkTimer) {
			this.clientWorkTimer = workTimer;
		}
		if (maxTimer != clientMaxTimer) {
			this.clientMaxTimer = maxTimer;
		}
	}
}
