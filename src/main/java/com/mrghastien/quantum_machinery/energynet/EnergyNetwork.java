package com.mrghastien.quantum_machinery.energynet;

import java.util.HashSet;
import java.util.Set;

import com.mrghastien.quantum_machinery.capabilities.energy.ModEnergyStorage;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

public class EnergyNetwork {
	public int id;
	Set<TileEntity> members = new HashSet<TileEntity>();

	EnergyNetwork(int id) {
		this.id = id;
	}

	public EnergyNetwork add(TileEntity te) {
		if (te.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
			members.add(te);
		}
		return this;
	}

	public EnergyNetwork remove(TileEntity te) {
		if (te.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
			members.remove(te);
		}
		return this;
	}

	public int getSize() {
		return members.size();
	}

	public LazyOptional<ModEnergyStorage> getEnergy() {
		int[] cap = {0};
		int[] en = {0};
		int[] in = {0};
		int[] out = {0};
		for (TileEntity tileEntity : members) {
			cap[0] += tileEntity.getCapability(CapabilityEnergy.ENERGY).map(e -> e.getMaxEnergyStored()).orElse(0);
			en[0] += tileEntity.getCapability(CapabilityEnergy.ENERGY).map(e -> e.getEnergyStored()).orElse(0);
			in[0] = Math.min(tileEntity.getCapability(CapabilityEnergy.ENERGY).map(e -> ((ModEnergyStorage)e).getMaxReceive()).orElse(0), in[0]);
			out[0] = Math.min(tileEntity.getCapability(CapabilityEnergy.ENERGY).map(e -> ((ModEnergyStorage)e).getMaxExtract()).orElse(0), out[0]);
		}
		LazyOptional<ModEnergyStorage> energy = LazyOptional.of(() -> new ModEnergyStorage(cap[0], in[0], out[0], en[0]));
		return energy;
	}
}