package com.mrghastien.quantum_machinery.common.blocks.machines;

import java.util.Collection;

import com.mrghastien.quantum_machinery.api.common.crafting.BaseRecipe;
import com.mrghastien.quantum_machinery.common.blocks.BaseTile;
import com.mrghastien.quantum_machinery.common.capabilities.items.ModItemStackHandler;
import com.mrghastien.quantum_machinery.common.network.GuiSynced;
import com.mrghastien.quantum_machinery.util.ItemHelper;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public abstract class MachineTile<R extends BaseRecipe> extends BaseTile {

	@GuiSynced
	protected int progress;
	@GuiSynced
	protected int processingTime;
	
	protected R currentRecipe;
	@GuiSynced
	protected boolean active = false;
	
	public MachineTile(TileEntityType<?> tileEntityTypeIn, int capacity, int maxInput) {
		super(tileEntityTypeIn, capacity, maxInput, 0);
	}
	
	protected BlockState getInactiveState(BlockState state) {
		return state.with(BlockStateProperties.LIT, false);
	}
	
	protected BlockState getActiveState(BlockState state) {
		return state.with(BlockStateProperties.LIT, true);
	}
	
	protected abstract int getEnergyUsage();
	
	protected abstract ModItemStackHandler getInputInventory();
	
	protected abstract ModItemStackHandler getOutputInventory();
	
	protected abstract Collection<R> getRecipes(); 
	
	protected abstract R getApplicableRecipe();
	
	protected boolean canRun() {
		return world != null 
				&& energy.getEnergyStored() >= getEnergyUsage()
				&& hasRoomInOutput(getResults());
	}
		
	protected abstract int getProcessingTime();
	
	protected abstract Collection<ItemStack> getResults();
	
	protected abstract Collection<Ingredient> getIngredients();
	
	protected void setInactiveState() {
		updateBlockState(getInactiveState(world.getBlockState(pos)));
		active = false;
	}
	
	protected void setActiveState() {
		updateBlockState(getActiveState(world.getBlockState(pos)));
		active = true;
	}
	
	@Override
	protected void behavior() {
		
		this.currentRecipe = getApplicableRecipe();
		if(currentRecipe != null && canRun()) {
			processingTime = getProcessingTime();
			progress += 1;
			energy.consumeEnergy(getEnergyUsage(), false);
			
			if(progress >= processingTime) {
				getResults().forEach(this::storeResultItem);
				consumeIngredients();
				
				progress = 0;
				
				if(getApplicableRecipe() == null) {
					setInactiveState();
				}
			} else {
				setActiveState();
			}
		} else {
			if(currentRecipe == null)
				progress = 0;
			setInactiveState();
		}
	}
	
	protected boolean hasRoomInOutput(Iterable<ItemStack> results) {
		for (ItemStack itemStack : results) {
			if(!hasRoomForOutputItem(itemStack))
				return false;
		}
		return true;
	}
	
	private boolean hasRoomForOutputItem(ItemStack stack) {
		IItemHandler inv = getOutputInventory();
		for (int i = 0; i < inv.getSlots(); i++) {
			ItemStack output = inv.getStackInSlot(i);
			if(ItemHelper.canItemsStack(stack, output)) {
				return true;
			}
		}
		return false;
	}
	
	protected void storeResultItem(ItemStack stack) {
		IItemHandler inv = getOutputInventory();
		for(int i = 0; i < inv.getSlots(); i++) {
			ItemStack remainder = inv.insertItem(i, stack, false);
			if(remainder == ItemStack.EMPTY) return;
		}
	}
	
	protected void consumeIngredients() {
		IItemHandler inv = getInputInventory();
		Collection<Ingredient> ingredients = getIngredients();
		for (Ingredient ingredient : ingredients) {
			for(ItemStack stack : ingredient.getMatchingStacks()) {
				if(containsItemStackInInput(stack)) {
					int count = stack.getCount();
					for (int i = 0; i < inv.getSlots(); i++) {
						if (inv.getStackInSlot(i).isItemEqual(stack)) {
							count -= inv.extractItem(i, Math.min(count, inv.getStackInSlot(i).getCount()), false).getCount();
							if(count <= 0) {
								break; 
							}
						}
					}
				}
			}
		}
	}
	
	private boolean containsItemStackInInput(ItemStack stack) {
		IItemHandler inv = getInputInventory();
		int count = 0;
		for (int i = 0; i < inv.getSlots(); i++) {
			if(inv.getStackInSlot(i).isItemEqual(stack)) {
				count += inv.getStackInSlot(i).getCount();
				if(count >= stack.getCount()) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if(side == Direction.DOWN)
				return getOutputInventory().getLazy().cast();
			if(side == Direction.UP)
				return getInputInventory().getLazy().cast();

		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		this.progress = compound.getInt("Progress");
		this.processingTime = compound.getInt("ProcessingTime");

	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("Progress", progress);
		compound.putInt("ProcessingTime", processingTime);
	    return compound;
	}
	
	public int getProgress() {
		return progress;
	}
	
	public int getCurrentProcessingTime() {
		return processingTime;
	}
	
	public int getCurrentEnergyUsage() {
		return active ? getEnergyUsage() : 0;
	}
	
	public boolean isActive() {
		return active;
	}
}