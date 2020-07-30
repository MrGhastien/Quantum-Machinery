package com.mrghastien.quantum_machinery.common.blocks;

import javax.annotation.Nullable;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.common.capabilities.energy.MachineEnergyStorage;
import com.mrghastien.quantum_machinery.common.network.GuiSynced;
import com.mrghastien.quantum_machinery.util.helpers.BlockHelper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

public abstract class MachineBaseTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

	@GuiSynced
	protected MachineEnergyStorage energy;
	
	protected ITextComponent customName;
	protected final String containerRegistryName;

	public MachineBaseTile(TileEntityType<?> tileEntityTypeIn, int capacity,
			int maxTransfert) {
		this(tileEntityTypeIn, capacity, maxTransfert, maxTransfert);
	}

	public MachineBaseTile(TileEntityType<?> tileEntityTypeIn, int capacity, int maxInput,
			int maxOutput) {
		super(tileEntityTypeIn);
		this.containerRegistryName = "tileEntity." + QuantumMachinery.MODID + "." + tileEntityTypeIn.getRegistryName().getPath();
		energy = createEnergy(capacity, maxInput, maxOutput);
	}
	
	@Override
	public void remove() {
		super.remove();
		energy.getLazy().invalidate();
	}
	
	@Override
	public void tick() {
		if(world.isRemote) {
			return;
		}
		energy.update();
		behavior();
		BlockHelper.sendOutPower(this);
	}
	
	protected void behavior() {}
	
	protected void updateBlockState(BlockState newState) {
        if (world == null) return;
        BlockState oldState = world.getBlockState(pos);
        if (oldState != newState) {
            world.setBlockState(pos, newState, 3);
            world.notifyBlockUpdate(pos, oldState, newState, 3);
        }
    }
	
	protected MachineEnergyStorage createEnergy(int capacity, int maxInput, int maxOutput) {
		return new MachineEnergyStorage(this, capacity, maxInput, maxOutput);
	}

	@Override
	public abstract Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player);

	public MachineEnergyStorage getEnergyStorage() {
		return energy;
	}

	public ITextComponent getName() {
		ITextComponent itextcomponent = this.getCustomName();
		return itextcomponent != null ? itextcomponent
				: new TranslationTextComponent(containerRegistryName);
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

	public boolean isUsableByPlayer(PlayerEntity player) {
		return this.world.getTileEntity(pos) != this ? false
				: player.getDistanceSq(this.pos.getX() + 0.5d, this.pos.getY() + 0.5d,
						this.pos.getZ() + 0.5d) <= 64d;
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		energy.deserializeNBT(compound.getCompound("EnergyStorage"));
		if (compound.contains("CustomName", 8)) 
			this.customName = ITextComponent.Serializer.fromJson(compound.getString("CustomName"));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("EnergyStorage", energy.serializeNBT());
		ITextComponent name = this.getCustomName();
	    if (name != null) 
	       compound.putString("CustomName", ITextComponent.Serializer.toJson(name));
	    return compound;
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == CapabilityEnergy.ENERGY) {
			if(side != null)
				return energy.getLazy(side).cast();
			return energy.getLazy().cast();
		}
		return super.getCapability(cap, side);
	}
}
