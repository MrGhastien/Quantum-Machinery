package com.mrghastien.quantum_machinery.tileentities;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nullable;

import com.mrghastien.quantum_machinery.blocks.ConnectorBlock;
import com.mrghastien.quantum_machinery.blocks.EnergyTransfer;
import com.mrghastien.quantum_machinery.capabilities.energy.ModEnergyStorage;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class MachineTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

	protected LazyOptional<IEnergyStorage> energy;

	protected ITextComponent customName;
	protected String containerRegistryName;

	protected int workTimer;
	protected int maxTimer;

	public MachineTile(TileEntityType<?> tileEntityTypeIn, String containerRegistryNameIn, int capacity,
			int maxTransfert) {
		this(tileEntityTypeIn, containerRegistryNameIn, capacity, maxTransfert, maxTransfert);
	}

	public MachineTile(TileEntityType<?> tileEntityTypeIn, String containerRegistryNameIn, int capacity, int maxInput,
			int maxOutput) {
		super(tileEntityTypeIn);
		this.containerRegistryName = containerRegistryNameIn;
		energy = LazyOptional.of(() -> new ModEnergyStorage(capacity, maxInput, maxOutput));
	}

	@Override
	public abstract Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player);

	protected void sendOutPower() {
		energy.ifPresent(energy -> {
			AtomicInteger capacity = new AtomicInteger(energy.getEnergyStored());
			if (capacity.get() > 0) {
				for (Direction direction : Direction.values()) {
					TileEntity tileentity = world.getTileEntity(pos.offset(direction));
					if (tileentity != null) {
						if (tileentity instanceof PowerTransmitterTile) {
							if (world.getBlockState(pos.offset(direction)).get(ConnectorBlock.ENERGY_HANDLING_MODE) != EnergyTransfer.RECIEVE) {
								boolean doContinue = tileentity.getCapability(CapabilityEnergy.ENERGY, direction).map(handler -> {
											if (handler.canReceive()) {
												int recieved = handler.receiveEnergy(Math.min(capacity.get(), 1024), false);
												capacity.addAndGet(-recieved);
												((ModEnergyStorage) energy).extractEnergy(recieved);
												markDirty();
												return capacity.get() > 0;
											} else {
												return true;
											}
										}).orElse(true);
								if (!doContinue)
									return;
							}
						} else {
							boolean doContinue = tileentity.getCapability(CapabilityEnergy.ENERGY, direction).map(handler -> {
										if (handler.canReceive()) {
											int recieved = handler.receiveEnergy(Math.min(capacity.get(), 1024), false);
											capacity.addAndGet(-recieved);
											((ModEnergyStorage) energy).extractEnergy(recieved);
											markDirty();
											return capacity.get() > 0;
										} else {
											return true;
										}
									}).orElse(true);
							if (!doContinue)
								return;
						}
					}
				}
			}
		});
	}

	public int getEnergy() {
		return this.getCapability(CapabilityEnergy.ENERGY).map(e -> e.getEnergyStored()).orElse(0);
	}

	public int getMaxEnergy() {
		return this.getCapability(CapabilityEnergy.ENERGY).map(e -> e.getMaxEnergyStored()).orElse(0);
	}

	public int getMaxIn() {
		return this.getCapability(CapabilityEnergy.ENERGY).map(e -> ((ModEnergyStorage) e).getMaxReceive()).orElse(0);
	}

	public int getMaxOut() {
		return this.getCapability(CapabilityEnergy.ENERGY).map(e -> ((ModEnergyStorage) e).getMaxExtract()).orElse(0);
	}

	public ITextComponent getName() {
		ITextComponent itextcomponent = this.getCustomName();
		return (ITextComponent) (itextcomponent != null ? itextcomponent
				: new TranslationTextComponent(containerRegistryName));
	}

	public boolean hasCustomName() {
		return this.customName != null;
	}

	@Nullable
	public ITextComponent getCustomName() {
		return this.customName;
	}

	@Override
	public ITextComponent getDisplayName() {
		return this.getName();
	}

	public void setCustomName(@Nullable ITextComponent name) {
		this.customName = name;
	}

	public abstract boolean isUsableByPlayer(PlayerEntity player);

	public int getWorkTimer() {
		return workTimer;
	}

	public int getMaxTimer() {
		return maxTimer;
	}
}
