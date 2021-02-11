package mrghastien.quantum_machinery.common.capabilities.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;

public class BatteryItemStackHandler extends ItemStackHandler implements IEnergyStorage {

	public BatteryItemStackHandler() {
		super();
	}

	public BatteryItemStackHandler(int size) {
		super(size);
	}

	public BatteryItemStackHandler(NonNullList<ItemStack> stacks) {
		super(stacks);
	}
	
	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
	}
	
	private int getBatteryCount() {
		int count = 0;
		for (int i = 0; i < stacks.size(); i++) {
			ItemStack stack = stacks.get(i);
			if(stack.getCapability(CapabilityEnergy.ENERGY, null).map(e -> e.getEnergyStored() < e.getMaxEnergyStored()).orElse(false)) {
				count++;
			}
		}
		return count;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		if(!canReceive())
			return 0;
		int batteryCount = getBatteryCount();
		int remaining = maxReceive;
		
		if(batteryCount > 0) {
			int perBattery = remaining / batteryCount;
			for(int i = 0; i < stacks.size(); i++) {
				ItemStack stack = stacks.get(i);
				if(!stack.isEmpty())
					remaining -= stack.getCapability(CapabilityEnergy.ENERGY, null).map(e -> e.receiveEnergy(perBattery, simulate)).orElse(0);
			}
		}
		return maxReceive - remaining; 
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		if(!canExtract())
			return 0;
		int batteryCount = getBatteryCount();
		int remaining = maxExtract;
		
		if(batteryCount > 0) {
			int perBattery = remaining / batteryCount;
			for(int i = 0; i < stacks.size(); i++) {
				ItemStack stack = stacks.get(i);
				if(!stack.isEmpty())
					remaining -= stack.getCapability(CapabilityEnergy.ENERGY, null).map(e -> e.extractEnergy(perBattery, simulate)).orElse(0);
			}
		}
		return maxExtract - remaining;
	}

	@Override
	public int getEnergyStored() {
		int energy = 0;
		for(int i = 0; i < stacks.size(); i++) {
			ItemStack stack = stacks.get(i);
			if(!stack.isEmpty())
				energy += stack.getCapability(CapabilityEnergy.ENERGY, null).map(e -> e.getEnergyStored()).orElse(0);
		}
		return energy;
	}

	@Override
	public int getMaxEnergyStored() {
		int energy = 0;
		for(int i = 0; i < stacks.size(); i++) {
			ItemStack stack = stacks.get(i);
			if(!stack.isEmpty())
				energy += stack.getCapability(CapabilityEnergy.ENERGY, null).map(e -> e.getMaxEnergyStored()).orElse(0);
		}
		return energy;
	}

	@Override
	public boolean canExtract() {
		return true;
	}

	@Override
	public boolean canReceive() {
		return true;
	}
}
