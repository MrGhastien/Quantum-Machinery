package mrghastien.quantum_machinery.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

import mrghastien.quantum_machinery.common.capabilities.energy.ItemEnergyStorage;
import mrghastien.quantum_machinery.common.capabilities.energy.ModEnergyStorage;

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
		return new ModEnergyStorage.Provider(new ItemEnergyStorage(stack, capacity, maxTransfer, maxTransfer));
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return (double)(capacity - stack.getCapability(CapabilityEnergy.ENERGY, null).map(e -> e.getEnergyStored()).orElse(0)) / (double)capacity;
	}
	
	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return 0x0000FFFF;
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}
}
