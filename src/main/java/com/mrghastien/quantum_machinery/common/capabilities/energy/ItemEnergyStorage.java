package com.mrghastien.quantum_machinery.common.capabilities.energy;

import net.minecraft.item.ItemStack;

public class ItemEnergyStorage extends ModEnergyStorage {

	private ItemStack stack;
	
	public ItemEnergyStorage(ItemStack stack, int capacity, int maxInput, int maxOutput) {
		this(stack, capacity, maxInput, maxOutput, 0);
	}

	public ItemEnergyStorage(ItemStack stack, int capacity, int maxInput, int maxOutput, int energy) {
		super(capacity, maxInput, maxOutput, energy);
		this.stack = stack;
	}

	public ItemEnergyStorage(ItemStack stack, int capacity) {
		this(stack, capacity, capacity, capacity);
	}
	
	@Override
	public int getEnergyStored() {
		return super.getEnergyStored();
	}
	
	@Override
	public void setEnergyStored(int energy) {
		stack.getOrCreateTag().putInt("Energy", energy);
		super.setEnergyStored(energy);
	}
}
