package com.mrghastien.quantum_machinery.items;

import com.mrghastien.quantum_machinery.capabilities.energy.ModEnergyStorage;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

public abstract class EnergyItem extends Item {

	private final int capacity;
	private final int maxTransfer;

	public EnergyItem(Properties properties, int capacity, int maxTransfer) {
		super(properties);
		this.capacity = capacity;
		this.maxTransfer = maxTransfer;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT nbt) {
		return new EnergyCapabilityProvider(stack, this);
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return (double)(capacity - stack.getCapability(CapabilityEnergy.ENERGY).map(e -> e.getEnergyStored()).orElse(0)) / (double)capacity;
	}
	
	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return 0x0000FFFF;
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	private static class EnergyCapabilityProvider implements ICapabilityProvider {

		public final LazyOptional<ModEnergyStorage> energy;

		public EnergyCapabilityProvider(final ItemStack stack, EnergyItem item) {
			this.energy = LazyOptional
					.of(() -> new ModEnergyStorage(item.capacity, item.maxTransfer, item.maxTransfer) {

						@Override
						public int getEnergyStored() {
							if (stack.hasTag()) {
								return stack.getTag().getInt("Energy");
							} else {
								return 0;
							}
						}

						@Override
						public void setEnergyStored(int energy) {
							if (!stack.hasTag()) {
								stack.setTag(new CompoundNBT());
							}

							stack.getTag().putInt("Energy", energy);
						}
					});
		}

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			if (cap == CapabilityEnergy.ENERGY) {
				return energy.cast();
			}
			return null;
		}

	}
}
