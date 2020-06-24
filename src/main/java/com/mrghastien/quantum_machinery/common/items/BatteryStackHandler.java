package com.mrghastien.quantum_machinery.common.items;

import com.mrghastien.quantum_machinery.common.capabilities.energy.ModEnergyStorage;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.ItemStackHandler;

public class BatteryStackHandler extends ItemStackHandler{
	
	public BatteryStackHandler() {
		this(1);
	}
	
	public BatteryStackHandler(int size) {
		super(size);
	}
	
	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return stack.getCapability(CapabilityEnergy.ENERGY, null).isPresent();
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		return super.insertItem(slot, stack, simulate);
	}
	
	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}
	
	public void fillBatteries(LazyOptional<IEnergyStorage> energy) {
		for (int i = 0; i < getSlots(); i++) {
			int cap = energy.map(e -> e.getMaxEnergyStored()).orElse(0);
			
			int received = getStackInSlot(i).getCapability(CapabilityEnergy.ENERGY, null).map(e -> e.receiveEnergy(cap, true)).orElse(0);
			if(received > 0 && energy.map(e -> e.getEnergyStored()).orElse(0) > 0) {
				energy.ifPresent(e -> e.extractEnergy(received, false));
				getStackInSlot(i).getCapability(CapabilityEnergy.ENERGY, null).map(e -> e.receiveEnergy(received, false)).orElse(0);
			}
		}
	}
	
	public void emptyBatteries(LazyOptional<IEnergyStorage> energy) {
		for (int i = 0; i < getSlots(); i++) {
			int cap = energy.map(e -> e.getMaxEnergyStored()).orElse(0);
			
			int extracted = getStackInSlot(i).getCapability(CapabilityEnergy.ENERGY, null).map(e -> e.extractEnergy(cap, true)).orElse(0);
			if(extracted > 0 && energy.map(e -> e.getEnergyStored()).orElse(0) < cap) {
				energy.ifPresent(e -> e.extractEnergy(extracted, false));
				getStackInSlot(i).getCapability(CapabilityEnergy.ENERGY, null).map(e -> e.receiveEnergy(extracted, false)).orElse(0);
			}
		}
	}
	
	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		//setSize(nbt.contains("Size", Constants.NBT.TAG_INT) ? nbt.getInt("Size") : stacks.size());
        ListNBT tagList = nbt.getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++)
        {
            CompoundNBT itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");
            int energy = itemTags.getInt("Energy");

            if (slot >= 0 && slot < stacks.size())
            {
                stacks.get(i).getCapability(CapabilityEnergy.ENERGY, null).ifPresent(e -> ((ModEnergyStorage)e).setEnergyStored(energy));
            }
        }
		super.deserializeNBT(nbt);
	}
	
	@Override
	public CompoundNBT serializeNBT() {
		ListNBT nbtTagList = new ListNBT();
        for (int i = 0; i < stacks.size(); i++)
        {
            if (!stacks.get(i).isEmpty())
            {
                CompoundNBT itemTag = new CompoundNBT();
                itemTag.putInt("Slot", i);
                stacks.get(i).write(itemTag);
                stacks.get(i).getCapability(CapabilityEnergy.ENERGY, null).ifPresent(e -> {
        			@SuppressWarnings("unchecked")
					CompoundNBT tag = ((INBTSerializable<CompoundNBT>)e).serializeNBT();
        			itemTag.put("Energy", tag);
        		});                
                nbtTagList.add(itemTag);
            }
        }
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", stacks.size());
        return nbt;
	}
	
	public LazyOptional<IEnergyStorage> getEnergyInSlot(int slot) {
		return getStackInSlot(slot).getCapability(CapabilityEnergy.ENERGY, null);
	}
}
