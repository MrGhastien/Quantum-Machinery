package com.mrghastien.quantum_machinery.common.capabilities.energy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MachineEnergyStorage extends ModEnergyStorage {

	private long lastRecieveTick;
	private long lastExtractTick;
	
	public final TileEntity tile;
	
	public MachineEnergyStorage(TileEntity tile, int capacity) {
		this(tile, capacity, capacity, capacity, 0);
	}

	public MachineEnergyStorage(TileEntity tile, int capacity, int maxInput, int maxOutput, int energy) {
		super(capacity, maxInput, maxOutput, energy);
		this.tile = tile;
	}

	public MachineEnergyStorage(TileEntity tile, int capacity, int maxInput, int maxOutput) {
		this(tile, capacity, maxInput, maxOutput, 0);
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		World world = tile.getWorld();
		int recieved = super.receiveEnergy(maxReceive, simulate);
		if(!simulate)
			this.lastRecieveTick = world.getGameTime();
		return recieved;
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		World world = tile.getWorld();
		int extracted = super.extractEnergy(maxExtract, simulate);
		if(!simulate)
			this.lastExtractTick = world.getGameTime();
		return extracted;
	}
	
	/**Used by generators to allow them to produce energy.
	 * 
	 * @param energy the amount of energy we want to produce
	 * @param simulate set to false to really produce energy
	 * @return The amount of energy produced.
	 */
	public int generateEnergy(int energy, boolean simulate) {
        if(getEnergyStored() + energy > getMaxEnergyStored()) {
        	energy -= getEnergyStored() + energy - getMaxEnergyStored();
        }
        if (!simulate)
        	setEnergyStored(getEnergyStored() + energy);
        return energy;
	}
	
	/**Used by energy consumers to allow them to consume energy.
	 * 
	 * @param energy the amount of energy we want to consume
	 * @param simulate set to false to really consume energy
	 * @return The amount of energy consumed.
	 */
	public int consumeEnergy(int maxExtract, boolean simulate) {

        if(getEnergyStored() - maxExtract < 0) {
        	maxExtract -= maxExtract - getEnergyStored();
        }
        if (!simulate)
            setEnergyStored(getEnergyStored() - maxExtract);
        return maxExtract;
	}
	
	public long getLastRecieveTick() {
		return lastRecieveTick;
	}

	public long getLastExtractTick() {
		return lastExtractTick;
	}

}
