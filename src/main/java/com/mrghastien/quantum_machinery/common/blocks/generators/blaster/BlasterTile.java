package com.mrghastien.quantum_machinery.common.blocks.generators.blaster;

import com.mrghastien.quantum_machinery.common.blocks.generators.GeneratorTile;
import com.mrghastien.quantum_machinery.common.capabilities.items.BatteryItemStackHandler;
import com.mrghastien.quantum_machinery.common.capabilities.items.ModItemStackHandler;
import com.mrghastien.quantum_machinery.common.capabilities.temperature.CapabilityTemp;
import com.mrghastien.quantum_machinery.common.capabilities.temperature.TempHandler;
import com.mrghastien.quantum_machinery.common.init.ModTileEntities;
import com.mrghastien.quantum_machinery.util.helpers.ItemHelper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class BlasterTile extends GeneratorTile {

	public static final int ENERGY_PRODUCTION = 128;
	
	private ModItemStackHandler inputInv = new ModItemStackHandler(this) {
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return ForgeHooks.getBurnTime(stack) > 0;
		};
	};
	private BatteryItemStackHandler batteryInv = new BatteryItemStackHandler();
	private CombinedInvWrapper invWrapper = new CombinedInvWrapper(inputInv, batteryInv);
	private TempHandler temp = new TempHandler(1000);

	public BlasterTile() {
		super(ModTileEntities.BLASTER.get(), 50000, 1024);
	}
	
	@Override
	protected boolean containsFuel() {
		for(int i = 0; i < inputInv.getSlots(); i++) {
			if(ForgeHooks.getBurnTime(inputInv.getStackInSlot(i)) > 0) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected void consumeFuel() {
		int index = -1;
		int bestBurnTime = 0;
		for (int i = 0; i < inputInv.getSlots(); i++) {
			ItemStack stack = inputInv.getStackInSlot(i);
			if (ItemHelper.isFuel(stack)) {
				if (bestBurnTime < ForgeHooks.getBurnTime(stack)) {
					bestBurnTime = ForgeHooks.getBurnTime(stack);
					index = i;
				}
			}
		}
		if (index >= 0) {
			burnTime = totalBurnTime = ForgeHooks.getBurnTime(inputInv.getStackInSlot(index));
			inputInv.extractItem(index, 1, false);
		}
	}
	
	@Override
	protected int getEnergyProduction() {
		return ENERGY_PRODUCTION;
	}
	
	@Override
	protected void behavior() {
		super.behavior();
		if(batteryInv != null) 
			energy.extractEnergy(batteryInv.receiveEnergy(energy.getEnergyStored(), false), false);
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		inputInv.deserializeNBT(compound.getCompound("Input"));
		batteryInv.deserializeNBT(compound.getCompound("Batteries"));
		temp.deserializeNBT(compound.getCompound("Temperature"));
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("Input", inputInv.serializeNBT());
		compound.put("Batteries", batteryInv.serializeNBT());
		compound.put("Temperature", temp.serializeNBT());

		return compound;
	}
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if(side == Direction.UP)
				return inputInv.getLazy().cast();
			
			return LazyOptional.of(() -> invWrapper).cast();
		}
		if (cap == CapabilityTemp.HEAT) {
			return temp.getLazy().cast();
		}
		if (cap == CapabilityEnergy.ENERGY) {
			return energy.getLazy().cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public Container createMenu(int id, PlayerInventory playerinventory, PlayerEntity player) {
		return new BlasterContainer(id, world, pos, playerinventory);
	}
}