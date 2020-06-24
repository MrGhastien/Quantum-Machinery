package com.mrghastien.quantum_machinery.common.blocks;

import javax.annotation.Nullable;

import com.mrghastien.quantum_machinery.common.capabilities.energy.MachineEnergyStorage;
import com.mrghastien.quantum_machinery.common.network.GuiSynced;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class MachineTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

	@GuiSynced(0)
	protected MachineEnergyStorage energy;
	protected int input = 0;
	protected int output = 0;
	
	protected ITextComponent customName;
	protected String containerRegistryName;

	protected int workTimer;
	protected int maxTimer;

	public MachineTile(TileEntityType<?> tileEntityTypeIn, String containerRegistryNameIn, int capacity,
			int maxTransfert) {
		this(tileEntityTypeIn, containerRegistryNameIn, capacity, maxTransfert, maxTransfert);
	}

	public MachineTile(TileEntityType<?> tileEntityTypeIn, String containerRegistryNameIn, int capacity, int maxInput,
			int maxOutput) {
		super(tileEntityTypeIn);
		this.containerRegistryName = containerRegistryNameIn;
		energy = createEnergy(capacity, maxInput, maxOutput);
	}
	
	@Override
	public void tick() {
		input = 0;
		output = 0;
	}
	
	protected MachineEnergyStorage createEnergy(int capacity, int maxInput, int maxOutput) {
		return new MachineEnergyStorage(this, capacity, maxInput, maxOutput);
	}

	@Override
	public abstract Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player);

	public MachineEnergyStorage getEnergyStorage() {
		return energy;
	}

	public ITextComponent getName() {
		ITextComponent itextcomponent = this.getCustomName();
		return (ITextComponent) (itextcomponent != null ? itextcomponent
				: new TranslationTextComponent(containerRegistryName));
	}

	public boolean hasCustomName() {
		return this.customName != null;
	}

	@Nullable
	public ITextComponent getCustomName() {
		return this.customName;
	}

	@Override
	public ITextComponent getDisplayName() {
		return this.getName();
	}

	public void setCustomName(@Nullable ITextComponent name) {
		this.customName = name;
	}

	public abstract boolean isUsableByPlayer(PlayerEntity player);

	public int getWorkTimer() {
		return workTimer;
	}

	public int getMaxTimer() {
		return maxTimer;
	}
}
