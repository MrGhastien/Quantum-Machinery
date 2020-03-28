package com.mrghastien.quantum_machinery.tileentities;

import static com.mrghastien.quantum_machinery.init.ModTileEntities.CONNECTOR_TILE;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.mrghastien.quantum_machinery.blocks.ConnectorBlock;
import com.mrghastien.quantum_machinery.blocks.EnergyTransfer;
import com.mrghastien.quantum_machinery.capabilities.energy.ModEnergyStorage;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class ConnectorTile extends PowerTransmitterTile {
	
	private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
	private int capacity = 1024;
	
	public ConnectorTile() {
		super(CONNECTOR_TILE.get());
	}

	@Override
	public void tick() {
		if(!world.isRemote) {
			this.capacity = ((ConnectorBlock)world.getBlockState(pos).getBlock()).getTransferRate();
			if(world.getBlockState(pos).get(ConnectorBlock.ENERGY_HANDLING_MODE) == EnergyTransfer.SENDING) {
				sendOutPowerToReciever(energy);
			} else if(world.getBlockState(pos).get(ConnectorBlock.ENERGY_HANDLING_MODE) == EnergyTransfer.RECIEVE) {
				sendOutPower(energy);
			}
		}
	}
	
	private void sendOutPowerToReciever(LazyOptional<IEnergyStorage> energyHandler) {
		energyHandler.ifPresent(energy -> {
			AtomicInteger capacity = new AtomicInteger(energy.getEnergyStored());
			if(capacity.get() > 0) {
				for (Direction direction : Direction.values()) {
					TileEntity ctileentity = world.getTileEntity(pos.offset(direction));
					if(ctileentity != null ) {
						if(ctileentity instanceof CableTile) {
							List<ConnectorTile> rTiles = new ArrayList<>();
							for (BlockPos tePos : ((CableTile)ctileentity).getRecieverPosList()) {
								rTiles.add((ConnectorTile)world.getTileEntity(tePos));
							}
							for(int i = 0; i < rTiles.size(); i++) {
								ConnectorTile rTile = rTiles.get(i);
								boolean doContinue = rTile.getCapability(CapabilityEnergy.ENERGY, direction).map(handler -> {
									if(handler.canReceive()) {
										int recieved = handler.receiveEnergy(Math.min(capacity.get(), this.capacity), false);
										capacity.addAndGet(-recieved);
										((ModEnergyStorage)energy).extractEnergy(recieved);
										markDirty();
										return capacity.get() > 0;
									}else {
										return true;
									}
								}).orElse(true);
		 				if(!doContinue) return;
						}
						}
					}
				}
			}
		});
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		return super.write(compound);
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
	}
	
	public EnergyTransfer getState() {
		return world.getBlockState(pos).get(ConnectorBlock.ENERGY_HANDLING_MODE);
	}
	
	protected void sendOutPower(LazyOptional<IEnergyStorage> energyHandler) {
		energyHandler.ifPresent(energy -> {
			AtomicInteger capacity = new AtomicInteger(energy.getEnergyStored());
			if(capacity.get() > 0) {
				for (Direction direction : Direction.values()) {
					TileEntity tileentity = world.getTileEntity(pos.offset(direction));
					if(tileentity != null) {
						if (tileentity instanceof PowerTransmitterTile) {
							if (world.getBlockState(pos.offset(direction)).get(ConnectorBlock.ENERGY_HANDLING_MODE) != EnergyTransfer.RECIEVE) {
								boolean doContinue = tileentity.getCapability(CapabilityEnergy.ENERGY, direction).map(handler -> {
									if(handler.canReceive()) {
										int recieved = handler.receiveEnergy(Math.min(capacity.get(), 1024), false);
										capacity.addAndGet(-recieved);
										((ModEnergyStorage)energy).extractEnergy(recieved);
										markDirty();
										return capacity.get() > 0;
									} else {
										return true;
									}
								}
								).orElse(true);
		 				if(!doContinue) return;
							}
						} else if(!(tileentity instanceof PowerTransmitterTile)) {
							boolean doContinue = tileentity.getCapability(CapabilityEnergy.ENERGY, direction).map(handler -> {
								if(handler.canReceive()) {
									int recieved = handler.receiveEnergy(Math.min(capacity.get(), 1024), false);
									capacity.addAndGet(-recieved);
									((ModEnergyStorage)energy).extractEnergy(recieved);
									markDirty();
									return capacity.get() > 0;
								}else {
									return true;
								}
							}
							).orElse(true);
							if(!doContinue) return;
						}
					}
				}
			}
		});
	}
	
	public int getTransferRate() {
		return this.getCapability(CapabilityEnergy.ENERGY).map(e -> ((ModEnergyStorage)e).getMaxEnergyStored()).orElse(0);
	}
	
	protected IEnergyStorage createEnergy() {
		return new ModEnergyStorage(capacity);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == CapabilityEnergy.ENERGY) {
			return energy.cast();
		}
		return super.getCapability(cap, side);
	}

}
