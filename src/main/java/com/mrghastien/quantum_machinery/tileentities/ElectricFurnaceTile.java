package com.mrghastien.quantum_machinery.tileentities;

import static com.mrghastien.quantum_machinery.init.ModTileEntities.ELECTRIC_FURNACE_TILE;

import javax.annotation.Nullable;

import com.mrghastien.quantum_machinery.capabilities.energy.ModEnergyStorage;
import com.mrghastien.quantum_machinery.containers.ElectricFurnaceContainer;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class ElectricFurnaceTile extends MachineTile{
	
	private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createItemHandler);
	
	private IRecipeType<? extends AbstractCookingRecipe> recipeType;
	private int soundCounter;
	private int toConsume = 150;
	private boolean isRefining = false;
	private ItemStack smelting = ItemStack.EMPTY;
	
	public ElectricFurnaceTile() {
		super(ELECTRIC_FURNACE_TILE.get(), "container.electric_furnace.name", 50000, 512, 0);
		this.recipeType = IRecipeType.SMELTING;
		this.maxTimer = 120;
	}
	
	private IItemHandler createItemHandler() {
		return new ItemStackHandler(2) {
			@Override
			protected void onContentsChanged(int slot) {
				ElectricFurnaceTile.this.markDirty();
			}
			
			@Override
			public boolean isItemValid(int slot, ItemStack stack) {
				if(slot == 1) {
					return stack.getItem() == smelting.getItem();
				}
				else {
					return true;
				}
			}
		};
	}
	
	public boolean hasEnergy() {
		return this.getCapability(CapabilityEnergy.ENERGY).map(e -> e.getEnergyStored()).orElse(0) > 0;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void tick() {
		 if (!world.isRemote) {
				
				if(this.hasEnergy()) {
					if(workTimer > 0) {
						if(soundCounter >= 20) {
							this.getWorld().playSound(null, pos, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 0.5f, 0.5f);
							soundCounter = 0;
						}
						if(soundCounter < 20)
							soundCounter++;
						if(this.getStack(1).getItem() == smelting.getItem()|| this.getStack(1).isEmpty()) {
							workTimer++;
						}
						energy.ifPresent(e -> {
							((ModEnergyStorage)e).extractEnergy(toConsume);
						});
						if(workTimer >= maxTimer) {
							if(!this.getStack(1).isEmpty()) {
									ItemStack outputItem = new ItemStack(this.getStack(1).getItem(), 1);
								handler.ifPresent(h ->  h.insertItem(1, outputItem.copy(), false));
							}
							else {
								handler.ifPresent(h ->  h.insertItem(1, smelting.copy(), false));
							}
						
							smelting = ItemStack.EMPTY;
							workTimer = 0;
							maxTimer = 120;
							toConsume = 25;
							return;
						}
					}
					else {
						 IRecipe<?> irecipe = this.world.getRecipeManager().getRecipe((IRecipeType<AbstractCookingRecipe>)this.recipeType, new Inventory(this.getStack(0), this.getStack(1)), this.world).orElse(null);
						 if (irecipe != null) {
						 ItemStack output = irecipe.getRecipeOutput();
						if(!output.isEmpty() && this.getStack(1).getCount() < this.getStack(1).getMaxStackSize()) {
							handler.ifPresent(h ->  {
								if (canSmelt(irecipe)) {
									smelting = output;
									if(this.getStack(1).getItem() == smelting.getItem() || this.getStack(1).isEmpty()) {
											h.extractItem(0, 1, false);
											maxTimer = 35;
											toConsume = 256;
											workTimer++;
											isRefining = true;
										}
								} else {
									this.isRefining = false;
							}
						});
						energy.ifPresent(e -> {
							((ModEnergyStorage)e).extractEnergy(toConsume);
						});
					} else isRefining = false;
					} else isRefining = false;
				}
			}
				BlockState blockState = world.getBlockState(pos);
				if (blockState.get(BlockStateProperties.POWERED) != this.isRefining) {
					world.setBlockState(pos, blockState.with(BlockStateProperties.POWERED, this.isRefining), 3);
				}
				if (!this.hasEnergy()) {
					this.isRefining = false;
				}
		}
	}

	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
		return new ElectricFurnaceContainer(id, world, pos, playerInventory, player);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return handler.cast();
		}
		if(cap == CapabilityEnergy.ENERGY) {
			return energy.cast();
		}
		return super.getCapability(cap, side);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		CompoundNBT invTag = compound.getCompound("inv");
		handler.ifPresent(h -> ((INBTSerializable<CompoundNBT>)h).deserializeNBT(invTag));
		CompoundNBT energyTag = compound.getCompound("energy");
		energy.ifPresent(e -> ((INBTSerializable<CompoundNBT>)e).deserializeNBT(energyTag));
		this.workTimer = compound.getInt("counter");
		if (compound.contains("CustomName", 8)) 
		{
			this.customName = ITextComponent.Serializer.fromJson(compound.getString("CustomName"));
	    }
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
		compound.putInt("counter", workTimer);
		ITextComponent itextcomponent = this.getCustomName();
	    if (itextcomponent != null) 
	    {
	       compound.putString("CustomName", ITextComponent.Serializer.toJson(itextcomponent));
	    }
		return compound;
	}
	
	protected boolean canSmelt(@Nullable IRecipe<?> recipeIn) {
		if (!this.getStack(0).isEmpty() && recipeIn != null) {
			ItemStack itemstack = recipeIn.getRecipeOutput();
			if (itemstack.isEmpty()) {
				return false;
			} else {
				ItemStack itemstack1 = this.getStack(1);
					if (itemstack1.isEmpty()) {
						return true;
					} else if (!itemstack1.isItemEqual(itemstack)) {
						return false;
					} else if (itemstack1.getCount() + itemstack.getCount() <= 64
							&& itemstack1.getCount() + itemstack.getCount() <= itemstack1.getMaxStackSize()) { 
						return true;
					} else {
						return itemstack1.getCount() + itemstack.getCount() <= itemstack.getMaxStackSize(); 
					}
			}
				} else {
					return false;
				}
	}
	
	public ItemStack getStack(int index) {
		return this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(h -> h.getStackInSlot(index))
				.orElse(ItemStack.EMPTY);
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return this.world.getTileEntity(pos) != this ? false : player.getDistanceSq((double)this.pos.getX() + 0.5d, (double)this.pos.getY() + 0.5d, (double)this.pos.getZ() + 0.5d) <= 64d;
	}

	public int getCounter() {
		return workTimer;
	}
	
	public int getMaxCounter() {
		return maxTimer;
	}

	public void setCounter(int counter) {
		this.workTimer = counter;
	}

	public LazyOptional<IItemHandler> getHandler() {
		return handler;
	}

	public void setMaxCounter(int value) {
		maxTimer = value;
	}

}
