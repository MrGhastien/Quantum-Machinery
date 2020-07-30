package com.mrghastien.quantum_machinery.common.capabilities.energy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class MachineEnergyStorage extends ModEnergyStorage {

	private final List<Interaction> inputs = new ArrayList<>();
	private final List<Interaction> outputs = new ArrayList<>();
	
	private final EnumMap<Direction, LazyOptional<Facade>> facades = new EnumMap<>(Direction.class);
	
	private int input = -1;
	private int output = -1;
	
	private TileEntity tile;
	
	private boolean clientSide = false;
	
	public MachineEnergyStorage(TileEntity tile, int capacity) {
		this(tile, capacity, capacity, capacity, 0);
	}

	public MachineEnergyStorage(TileEntity tile, int capacity, int maxInput, int maxOutput, int energy) {
		super(capacity, maxInput, maxOutput, energy);
		this.tile = tile;
		Arrays.stream(Direction.values()).forEach(d -> facades.put(d, LazyOptional.of(Facade::new)));
	}

	public MachineEnergyStorage(TileEntity tile, int capacity, int maxInput, int maxOutput) {
		this(tile, capacity, maxInput, maxOutput, 0);
	}
	
	/** Used when decoding from the packet client side.
	 */
	public MachineEnergyStorage(int capacity, int maxInput, int maxOutput, int energy, int input, int output) {
		super(capacity, maxInput, maxOutput, energy);
		this.input = input;
		this.output = output;
		clientSide = true;
	}
	
	private MachineEnergyStorage(TileEntity tile, int capacity, int maxInput, int maxOutput, int energy, List<Interaction> inputs, List<Interaction> outputs) {
		super(capacity, maxInput, maxOutput, energy);
		this.tile = tile;
		this.inputs.addAll(inputs);
		this.outputs.addAll(outputs);
	}
	
	public LazyOptional<Facade> getLazy(Direction facing) {
		return facades.get(facing);
	}
	
	public void update() {
		checkInputs();
		checkOutputs();
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		if(clientSide)
			return 0;
		int recieved = super.receiveEnergy(maxReceive, true);
		
		if (!simulate) {
			if (tile != null) {
				//checkInputs();
				inputs.add(new Interaction(recieved, getTime()));
				input = -1;
			}
			setEnergyStored(getEnergyStored() + recieved);
		}
		return recieved;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		if(clientSide)
			return 0;
		int extracted = super.extractEnergy(maxExtract, true);

		if (!simulate) {
			if (tile != null) {
				//checkOutputs();
				outputs.add(new Interaction(extracted, getTime()));
				output = -1;
			}
			setEnergyStored(getEnergyStored() - extracted);
		}
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
        if (!simulate) {
        	setEnergyStored(getEnergyStored() + energy);
        	onChange();
        }
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
        if (!simulate) {
            setEnergyStored(getEnergyStored() - maxExtract);
            onChange();
        }
        return maxExtract;
	}

	public int getInput() {
		if(!clientSide) {
			if(input != 0) {
				input = addInputs();
				//checkInputs();
			}
		}
		return input;
	}

	public int getOutput() {
		if(!clientSide) {
			if(output != 0) {
				output = addOutputs();
				//checkOutputs();
			}
		}
		return output;
	}

	public TileEntity getTile() {
		return tile;
	}
	
	private long getTime() {
		if(tile != null)
			return tile.getWorld().getGameTime();
		return 0;
	}
	
	private void checkInputs() {
		long currentTime = getTime();
		if(inputs.isEmpty())
			return;
		for(Interaction i : inputs.toArray(new Interaction[0])) {
			if(i.time < currentTime) {
				int index = inputs.indexOf(i);
				Interaction inter = inputs.remove(index);
				inter.toString();
			}
		}
	}
	
	private void checkOutputs() {
		long currentTime = getTime();
		if(outputs.isEmpty())
			return;
		for(Interaction i : outputs.toArray(new Interaction[0])) {
			if(i.time < currentTime)
				outputs.remove(outputs.indexOf(i));
		}
	}
	
	private int addInputs() {
		AtomicInteger value = new AtomicInteger();
		inputs.forEach(i -> value.addAndGet(i.energy));
		return value.get();
	}
	
	private int addOutputs() {
		AtomicInteger value = new AtomicInteger();
		outputs.forEach(i -> value.addAndGet(i.energy));
		return value.get();
	}
	
	@Override
	protected void onChange() {
		if(tile != null)
			tile.markDirty();
	}
	
	public MachineEnergyStorage copy() {
		return new MachineEnergyStorage(tile, capacity, maxReceive, maxExtract, energy, inputs, outputs);
	}
	
	public class Interaction {
		private final int energy;
		private final long time;

		public Interaction(int energy, long time) {
			this.energy = energy;
			this.time = time;
		}

		public int getEnergy() {
			return energy;
		}

		public long getTime() {
			return time;
		}

		@Override
		public String toString() {
			return "Time : " + time + " Energy : " + energy;
		}
	}
	
	public class Facade implements IEnergyStorage {

		private long lastRecievedTick;

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			int received = MachineEnergyStorage.this.receiveEnergy(maxReceive, simulate);
			if(!clientSide && !simulate && received > 0) lastRecievedTick = getTime();
			else return 0;
			return received;
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			if(getTime() != lastRecievedTick)
				return MachineEnergyStorage.this.extractEnergy(maxExtract, simulate);
			return 0;
		}

		@Override
		public int getEnergyStored() {
			return MachineEnergyStorage.this.getEnergyStored();
		}

		@Override
		public int getMaxEnergyStored() {
			return MachineEnergyStorage.this.getMaxEnergyStored();
		}

		@Override
		public boolean canExtract() {
			if(!clientSide)
				if(getTime() != lastRecievedTick)
					return MachineEnergyStorage.this.canExtract();
			return false;
		}
 
		@Override
		public boolean canReceive() {
			return MachineEnergyStorage.this.canReceive();
		}
		
	}
}