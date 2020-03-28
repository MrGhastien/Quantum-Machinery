package com.mrghastien.quantum_machinery.multiblocks.fission.tileentities;

import java.util.concurrent.atomic.AtomicInteger;

import com.mrghastien.quantum_machinery.blocks.ConnectorBlock;
import com.mrghastien.quantum_machinery.blocks.EnergyTransfer;
import com.mrghastien.quantum_machinery.capabilities.energy.ModEnergyStorage;
import com.mrghastien.quantum_machinery.init.ModTileEntities;
import com.mrghastien.quantum_machinery.multiblocks.MultiBlockTile;
import com.mrghastien.quantum_machinery.tileentities.PowerTransmitterTile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class FissionEOTile extends MultiBlockTile implements ITickableTileEntity{

	private BlockPos controllerPos;
	protected LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergyHandler);
	
	public FissionEOTile() {
		super(ModTileEntities.FISSION_OUTLET_TILE.get());
	}
	
	private IEnergyStorage createEnergyHandler() {
		return new ModEnergyStorage(Integer.MAX_VALUE);
	}

	@Override
	public void tick() {
		if (!isFormed) {
			return;
		};
		LazyOptional<IEnergyStorage> controllerHandlerLazyOptional = world.getTileEntity(controllerPos).getCapability(CapabilityEnergy.ENERGY);
		sendOutPower(controllerHandlerLazyOptional);
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

	public boolean isUsableByPlayer(PlayerEntity player) {
		return this.world.getTileEntity(pos) != this ? false
				: player.getDistanceSq((double) this.pos.getX() + 0.5d, (double) this.pos.getY() + 0.5d,
						(double) this.pos.getZ() + 0.5d) <= 64d;
	}

	public int getEnergy() {
		return this.getCapability(CapabilityEnergy.ENERGY).map(e -> e.getEnergyStored()).orElse(0);
	}

	public int getMaxEnergy() {
		return this.getCapability(CapabilityEnergy.ENERGY).map(e -> e.getMaxEnergyStored()).orElse(0);
	}
	
	public boolean isFormed() {
		return isFormed;
	}
	
	public void setFormed(boolean formed) {
		this.isFormed = formed;
	}
	
	public BlockPos getControllerPos() {
		return controllerPos;
	}

}
