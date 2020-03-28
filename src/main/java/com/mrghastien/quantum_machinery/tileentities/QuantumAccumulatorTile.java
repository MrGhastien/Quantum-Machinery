package com.mrghastien.quantum_machinery.tileentities;

import static com.mrghastien.quantum_machinery.init.ModTileEntities.QUANTUM_ACCUMULATOR_TILE;

import com.mrghastien.quantum_machinery.containers.QuantumAccumulatorContainer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

public class QuantumAccumulatorTile extends MachineTile {

	public QuantumAccumulatorTile() {
		super(QUANTUM_ACCUMULATOR_TILE.get(), "container.quantum_accumulator.name", 1000000, 1024);
	}

	@Override
	public void tick() {
		if(world.isRemote) {
			return;
		}
		sendOutPower(energy);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void read(CompoundNBT compound) {
		CompoundNBT energyTag = compound.getCompound("energy");
		energy.ifPresent(e -> ((INBTSerializable<CompoundNBT>)e).deserializeNBT(energyTag));
		if (compound.contains("CustomName", 8)) 
		{
			this.customName = ITextComponent.Serializer.fromJson(compound.getString("CustomName"));
	    }
		super.read(compound);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		energy.ifPresent(e -> {
			CompoundNBT tag = ((INBTSerializable<CompoundNBT>)e).serializeNBT();
			compound.put("energy", tag);
		});
		ITextComponent itextcomponent = this.getCustomName();
	    if (itextcomponent != null) 
	    {
	       compound.putString("CustomName", ITextComponent.Serializer.toJson(itextcomponent));
	    }
		return super.write(compound);
	}
	
	@Override
	public Container createMenu(int id, PlayerInventory playerinventory, PlayerEntity player) {
		return new QuantumAccumulatorContainer(id, world, pos, playerinventory, player);
	}
	
    public boolean isUsableByPlayer(PlayerEntity player) {
		return this.world.getTileEntity(pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5d, (double)this.pos.getY() + 0.5d, (double)this.pos.getZ() + 0.5d) <= 64d;
	}
    
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == CapabilityEnergy.ENERGY) {
			return energy.cast();
		}
		return super.getCapability(cap, side);
	}

}
