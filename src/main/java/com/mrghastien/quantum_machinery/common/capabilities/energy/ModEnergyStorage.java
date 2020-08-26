package com.mrghastien.quantum_machinery.common.capabilities.energy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

public class ModEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT>{
	
	private LazyOptional<IEnergyStorage> lazy;
	
	public ModEnergyStorage(int capacity, int maxInput, int maxOutput) {
		this(capacity, maxInput, maxOutput, 0);
	}
	
	public ModEnergyStorage(int capacity, int maxInput,int maxOutput, int energy) {
		super(capacity, maxInput, maxOutput, energy);
		this.lazy = LazyOptional.of(() -> this);
	}
	
	public ModEnergyStorage(int capacity) {
		this(capacity, capacity, capacity, 0);
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		if (!canReceive())
            return 0;

        int energyReceived = Math.min(this.maxReceive, maxReceive);
        if(getEnergyStored() + energyReceived > getMaxEnergyStored()) {
        	energyReceived -= getEnergyStored() + energyReceived - getMaxEnergyStored();
        }
        if (!simulate) {
        	setEnergyStored(getEnergyStored() + energyReceived);
        }
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
        if (!simulate) {
            setEnergyStored(getEnergyStored() - energyExtracted);
        }
        return energyExtracted;
	}
	
	public int receiveEnergy(int energy) {
		return receiveEnergy(energy, false);
	}
	
	public int extractEnergy(int energy) {
		return extractEnergy(energy, false);
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
		onChange();
	}
	
	public void setCapacity(int value) {
		this.capacity = value < 0 ? 0 : value;
		if(energy > capacity) {
			energy = capacity;
		}
		onChange();
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
		tag.putInt("Energy", getEnergyStored());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		setEnergyStored(nbt.getInt("Energy"));
	}

	public LazyOptional<IEnergyStorage> getLazy() {
		return lazy;
	}

	protected void onChange() {
		
	}
	
	public static class Provider implements ICapabilitySerializable<CompoundNBT> {

		private final ModEnergyStorage instance;
		
		public Provider(ModEnergyStorage instance) {
			this.instance = instance;
		}
		
		public void invalidate() {
			instance.getLazy().invalidate();
		}
		
		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			if(cap == CapabilityEnergy.ENERGY)
				return instance.getLazy().cast();
			return LazyOptional.empty();
		}

		@Override
		public CompoundNBT serializeNBT() {
			return instance.serializeNBT();
		}

		@Override
		public void deserializeNBT(CompoundNBT nbt) {
			instance.deserializeNBT(nbt);
		}
		
	}
	
}
