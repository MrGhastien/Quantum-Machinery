package com.mrghastien.quantum_machinery.tileentities;

import com.mrghastien.quantum_machinery.energynet.EnergyNetwork;
import com.mrghastien.quantum_machinery.energynet.EnergyNetworkHandler;
import com.mrghastien.quantum_machinery.energynet.EnergyNetworkHandler.NetworkTier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

public class InductorTile extends TileEntity {
	
	private boolean send;
	private boolean recieve;
	private EnergyNetwork network;
	
	public InductorTile(TileEntityType<?> tileEntityTypeIn, NetworkTier tier) {
		super(tileEntityTypeIn);
		this.network = EnergyNetworkHandler.changeNetwork(1, this);
	}

	public boolean canSend() {
		return send;
	}

	public void setSend(boolean send) {
		this.send = send;
	}

	public boolean canRecieve() {
		return recieve;
	}

	public void setRecieve(boolean recieve) {
		this.recieve = recieve;
	}

	public EnergyNetwork getNetwork() {
		return network;
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == CapabilityEnergy.ENERGY) {
			return network.getEnergy().cast();
		}
		return super.getCapability(cap);
	}
	
	public int getEnergy() {
		return this.getCapability(CapabilityEnergy.ENERGY).map(e -> e.getEnergyStored()).orElse(0);
	}
	
	public int getMaxEnergy() {
		return this.getCapability(CapabilityEnergy.ENERGY).map(e -> e.getMaxEnergyStored()).orElse(0);
	}

	public boolean isUsableByPlayer(PlayerEntity player) {
		return this.world.getTileEntity(pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5d, (double)this.pos.getY() + 0.5d, (double)this.pos.getZ() + 0.5d) <= 64d;
	}

}
