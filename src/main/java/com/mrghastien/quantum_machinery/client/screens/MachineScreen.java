package com.mrghastien.quantum_machinery.client.screens;

import com.mrghastien.quantum_machinery.common.blocks.MachineContainer;
import com.mrghastien.quantum_machinery.common.blocks.MachineTile;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class MachineScreen<T extends MachineContainer<U>, U extends MachineTile> extends ContainerScreen<T> implements IEnergyScreen {

	protected U tileEntity;
	protected int energy;
	protected int capacity;
	protected int input;
	protected int output;
	protected int clientWorkTimer;
	protected int clientMaxTimer;

	public MachineScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn, U tileEntity) {
		super(screenContainer, inv, titleIn);
		this.tileEntity = tileEntity;
	}

	protected int getEnergyScaled(int pixels) {
    	int i = energy;
    	int c = capacity;
    	return c != 0 && i != 0 ? i * pixels / c : 0;
    }
	
    protected int getWorkTimerScaled(int pixels) {
    	int i = this.clientWorkTimer;
    	int c = this.clientMaxTimer;
    	return c != 0 && i != 0 ? i * pixels / c : 0;
    }
    
    @Override
    public void syncEnergy(int energy, int capacity, int input, int output) {
    	if(energy >= 0)
			this.energy = energy;
    	if(capacity >= 0)
			this.capacity = capacity;
    	if(input >= 0)
			this.input = input;
		if(output <= 0)
			this.output = output;
    }
}
