package com.mrghastien.quantum_machinery.multiblocks.fission.tileentities;

import javax.annotation.Nullable;

import com.mrghastien.quantum_machinery.capabilities.energy.ModEnergyStorage;
import com.mrghastien.quantum_machinery.init.ModItems;
import com.mrghastien.quantum_machinery.init.ModTileEntities;
import com.mrghastien.quantum_machinery.multiblocks.ControllerTile;
import com.mrghastien.quantum_machinery.multiblocks.fission.FissionMultiBlock;
import com.mrghastien.quantum_machinery.multiblocks.fission.containers.FissionContainer;
import com.mrghastien.quantum_machinery.util.math.Coord3;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class FissionControllerTile extends ControllerTile implements INamedContainerProvider{

	//Naming
	protected ITextComponent customName;
	protected String containerRegistryName;
	
	//Energy
	private int capacity, maxInput, maxOutput;
	private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
	
	//Timers
	protected int workTimer;
	protected int maxTimer;
	
	private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createItemHandler);
	
	//Attributes
	public int maxCapacity = 12000;
	public int maxSurge = 500000;
	public int energyRate = 1200;
	private int fuelLevel = 0;
	private int powerSurge;
	private RunningState state = RunningState.STOPPED;
	private RunningState requestedState;
	 
	/* Slots :
	 * 0 -> fuel slot
	 * 1 -> waste slot
	 * 
	 * 
	 * 
	 */
	public FissionControllerTile() {
		super(ModTileEntities.FISSION_CONTROLLER_TILE.get());
		this.containerRegistryName = "container.fission.name";
	}
	
	//Technical methods
	
	@SuppressWarnings("unchecked")
	@Override
	public void read(CompoundNBT compound) {
		CompoundNBT invTag = compound.getCompound("inv");
		handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>)h).deserializeNBT(invTag));
		CompoundNBT energyTag = compound.getCompound("energy");
		energy.ifPresent(e -> ((INBTSerializable<CompoundNBT>)e).deserializeNBT(energyTag));
		this.workTimer = compound.getInt("counter");
		this.state = RunningState.getStateById(compound.getInt("stateId"));
		this.isFormed = compound.getBoolean("isFormed");
		if (compound.contains("CustomName", 8)) 
		{
			this.customName = ITextComponent.Serializer.fromJson(compound.getString("CustomName"));
	    }
		super.read(compound);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		handler.ifPresent(h -> {
			CompoundNBT tag = ((INBTSerializable<CompoundNBT>)h).serializeNBT();
			compound.put("inv", tag);
		});
		energy.ifPresent(e -> {
			CompoundNBT tag = ((INBTSerializable<CompoundNBT>)e).serializeNBT();
			compound.put("energy", tag);
		});
		compound.putInt("counter", this.workTimer);
		compound.putInt("stateId", state.id);
		compound.putBoolean("formed", isFormed());
		ITextComponent itextcomponent = this.getCustomName();
	    if (itextcomponent != null) 
	    {
	       compound.putString("CustomName", ITextComponent.Serializer.toJson(itextcomponent));
	    }
		return compound;
	}
	
	//Capabilities
	private IEnergyStorage createEnergy() {
		return new ModEnergyStorage(this.capacity, this.maxInput, this.maxOutput);
	}
	
	private IItemHandler createItemHandler() {
		return new ItemStackHandler(FissionContainer.SIZE) {
			@Override
			protected void onContentsChanged(int slot) {
				FissionControllerTile.this.markDirty();
			}
			
			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				return slot == 0 ? stack.getItem() == ModItems.RED_DIAMOND.get() : true;
			}
		};
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return handler.cast();
		}
		if (cap == CapabilityEnergy.ENERGY) {
			return energy.cast();
		}
		return super.getCapability(cap, side);
	}
	
	private boolean canRun() {
		return fuelLevel > 0 && powerSurge < maxSurge && requestedState == RunningState.RUNNING;
	}
	
	private boolean canInsertFuel() {
		return fuelLevel < maxCapacity && requestedState == RunningState.INSERTING;
	}

	@Override
	public void tick() {
		if (!world.isRemote) {
			if (fuelLevel < 0) {
				fuelLevel = 0;
			}
			if (canRun() && requestedState == RunningState.RUNNING) {
				this.state = RunningState.RUNNING;
				generate();
			} else {
				if (canInsertFuel() && requestedState == RunningState.INSERTING) {
					this.state = RunningState.INSERTING;
					insertFuel();
				} else {
					this.state = RunningState.STOPPED;
					this.requestedState = RunningState.STOPPED;
				}
			}
			
			Coord3 finalShape = getMultiBlockSize();
			
			if (formPos != null) {
				if (mode == FormMode.UNFORM) {
					unformBlock(
							pos.add(formPos.toVec3i())
							.subtract(FissionMultiBlock.INSTANCE.getControllerRelativePos(
								world.getBlockState(pos).get(BlockStateProperties.HORIZONTAL_FACING))));
				} else {
					if (mode == FormMode.FORM) {
						formBlock(
								pos.add(formPos.toVec3i())
								.subtract(FissionMultiBlock.INSTANCE.getControllerRelativePos(
									world.getBlockState(pos).get(BlockStateProperties.HORIZONTAL_FACING))));
					}
				}
				if (formPos.getZ() < finalShape.getZ()) {
					formPos = formPos.add(0, 0, 1);
				} else {
					formPos = formPos.resetZ();

					if (formPos.getY() < finalShape.getY()) {
						formPos = formPos.add(0, 1, 0);
					} else {
						formPos = formPos.resetY();

						if (formPos.getX() < finalShape.getX()) {
							formPos = formPos.add(1, 0, 0);
						}
					}
				}
			}
			if (hasFinishedFormation()) {
				if (mode == FormMode.UNFORM) {
					for (PlayerEntity player : listeners) {
						player.sendStatusMessage(new StringTextComponent(TextFormatting.GREEN + "Succesfully disassembled the reactor !"), false);
					}
				} else if(mode == FormMode.FORM) {
					for (PlayerEntity player : listeners) {
						player.sendStatusMessage(new StringTextComponent(TextFormatting.GREEN + "Succesfully assembled the reactor !"), false);
					}
				}
				formPos = null;
				mode = FormMode.NONE;
				listeners = null;
			}
		} else {
			if (formPos != null) {
				world.addParticle(ParticleTypes.CLOUD, pos.getX(), pos.getY(), pos.getZ(), .5, 0.5, 0.5);
			}
		}
	}
	
	//Utility methods
	private void generate() {
		if (state == RunningState.RUNNING) {
			if (!energy.map(e -> ((ModEnergyStorage)e).isFull()).orElse(true)) {
				energy.ifPresent(e -> {
					((ModEnergyStorage)e).receiveEnergy(1200);
				});
			} else {
				powerSurge += energyRate;
			}
			fuelLevel--;
		}
	}
	
	private void insertFuel() {
		if (state == RunningState.INSERTING) {
			handler.ifPresent(h -> {
				if (!h.getStackInSlot(0).isEmpty()) {
					this.fuelLevel += 25d;
					h.extractItem(0, 1, false);
				}
			});
		}
	}
	
	protected Coord3 getMultiBlockSize() {
		return FissionMultiBlock.INSTANCE.getStruct(world.getBlockState(pos).get(BlockStateProperties.HORIZONTAL_FACING)).getSize();
	}
	
	@Override
	public boolean hasFinishedFormation() {
		if ((formPos == null && this.mode != FormMode.FORM) || (formPos == null && this.mode != FormMode.UNFORM)) {
			return false;
		}
		if (this.mode == FormMode.NONE) {
			return true;
		}
		return formPos.getX() >= getMultiBlockSize().getX()-1 && formPos.getY() >= getMultiBlockSize().getY()-1 && formPos.getZ() >= getMultiBlockSize().getZ()-1;
	}
	
	public boolean isMultiblockFormed() {
		return false;
	}

	@Override
	public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
		return new FissionContainer(id, world, pos, inventory, player);
	}

	public boolean isUsableByPlayer(PlayerEntity player) {
		return this.world.getTileEntity(pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5d, (double)this.pos.getY() + 0.5d, (double)this.pos.getZ() + 0.5d) <= 64d;
	}
	
	public void requestState(RunningState requestedState) {
		if (this.state != requestedState) {
			this.requestedState = requestedState;
		}
	}
	
	@Override
	public int getFormPct() {
		if (formPos != null) {
			Coord3 frame = getMultiBlockSize();
			int pct = (int) Math.round(((float) ((formPos.getX()) * (frame.getY()) * (frame.getZ())
					+ (formPos.getY()) * (frame.getZ()) + formPos.getZ())
					/ ((float)(frame.volume()))) * 100d);
			return pct;
		} else {
			return isFormed ? 100 : 0;
		}
	}
    
	//Getters
	public int getEnergy() {
		return this.getCapability(CapabilityEnergy.ENERGY).map(e -> e.getEnergyStored()).orElse(0);
	}
	
	public int getMaxEnergy() {
		return this.getCapability(CapabilityEnergy.ENERGY).map(e -> e.getMaxEnergyStored()).orElse(0);
	}
	
	public int getFuelLevel() {
		return fuelLevel;
	}
	
	public int getPowerSurge() {
		return powerSurge;
	}
	
	public RunningState getState() {
		return state;
	}
	
	//Technical getters
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
  
	public ITextComponent getName() {
		ITextComponent itextcomponent = this.getCustomName();
		return (ITextComponent)(itextcomponent != null ? itextcomponent : new TranslationTextComponent(containerRegistryName));
	}
	
	public RunningState getRequestedState() {
		return requestedState;
	}
	
	//Setters
    public void setCustomName(@Nullable ITextComponent name) {
    	this.customName = name;
    }
    
	public enum RunningState{
		RUNNING(2, "multiblock.fission_reactor.running.name"),
		STOPPED(0, "multiblock.fission_reactor.stopped.name"),
		INSERTING(1, "multiblock.fission_reactor.inserting.name");
		
		private int id;
		private String translationKey;
		
		private RunningState(int id, String translationKey) {
			this.id = id;
			this.translationKey = translationKey;
		}
		
		public int getID() {
			return id;
		}
		
		public static RunningState getStateById(int id) {
			for (int i = 0; i < RunningState.values().length; i++) {
				if (RunningState.values()[i].getID() == id) {
					return RunningState.values()[i];
				}
			}
			return STOPPED;
		}
		
		public ITextComponent getLocalizedName() {
			return new TranslationTextComponent(translationKey);
		}
	}
}
