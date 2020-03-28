package com.mrghastien.quantum_machinery.capabilities.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class ModEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT>{
	
	public ModEnergyStorage(int capacity, int maxInput, int maxOutput) {
		super(capacity, maxInput, maxOutput);
	}
	
	public ModEnergyStorage(int capacity, int maxInput,int maxOutput, int energy) {
		super(capacity, maxInput, maxOutput, energy);
	}
	
	public ModEnergyStorage(int capacity) {
		super(capacity, capacity, capacity);
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		
		if (!canReceive())
            return 0;

        int energyReceived = Math.min(this.maxReceive, maxReceive);
        if(getEnergyStored() + energyReceived > getMaxEnergyStored()) {
        	energyReceived -= getEnergyStored() + energyReceived - getMaxEnergyStored();
        }
        if (!simulate)
        	setEnergyStored(getEnergyStored() + energyReceived);
        return energyReceived;
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		
		if (!canExtract())
            return 0;

        int energyExtracted = Math.min(this.maxExtract, maxExtract);
        if(getEnergyStored() - energyExtracted < 0) {
        	energyExtracted -= energyExtracted - getEnergyStored();
        }
        if (!simulate)
            setEnergyStored(getEnergyStored() - energyExtracted);
        return energyExtracted;
	}
	
	public int receiveEnergy(int energy) {
		return receiveEnergy(energy, false);
	}
	
	public int extractEnergy(int energy) {
		return extractEnergy(energy, false);
	}
	
	/**Used by generators to allow them to produce energy.
	 * 
	 * @param energy
	 * @param simulate
	 * @return
	 */
	public int generateEnergy(int energy, boolean simulate) {
        if(getEnergyStored() + energy > getMaxEnergyStored()) {
        	energy -= getEnergyStored() + energy - getMaxEnergyStored();
        }
        if (!simulate)
        	setEnergyStored(getEnergyStored() + energy);
        return energy;
	}
	
	public int consumeEnergy(int maxExtract, boolean simulate) {

        if(getEnergyStored() - maxExtract < 0) {
        	maxExtract -= maxExtract - getEnergyStored();
        }
        if (!simulate)
            setEnergyStored(getEnergyStored() - maxExtract);
        return maxExtract;
	}
	
	public void setExtractRate(int value) {
		if(value != maxExtract)
			this.maxExtract = value < 0 ? 0 : value > this.capacity ? this.capacity : value;
	}
	
	public void setInsertRate(int value ) {
		if(value != maxReceive)
		this.maxReceive = value < 0 ? 0 : value > this.capacity ? this.capacity : value;
	}
	
	public void setEnergyStored(int energy) {
		this.energy = energy;
	}
	
	public void setCapacity(int value) {
		this.capacity = value < 0 ? 0 : value;
		if(energy > capacity) {
			energy = capacity;
		}
	}
	
	public int getMaxExtract() {
		return maxExtract;
	}
	
	public int getMaxReceive() {
		return maxReceive;
	}
	
	public boolean isFull() {
		return energy >= capacity;
	}
	
	public int getAvailableSpace() {
		return capacity - energy;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT tag = new CompoundNBT();
		tag.putInt("energy", getEnergyStored());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		setEnergyStored(nbt.getInt("energy"));
	}

}
