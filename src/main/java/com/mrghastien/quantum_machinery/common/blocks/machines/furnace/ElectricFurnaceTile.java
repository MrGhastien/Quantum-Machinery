package com.mrghastien.quantum_machinery.common.blocks.machines.furnace;

import static com.mrghastien.quantum_machinery.common.init.ModTileEntities.ELECTRIC_FURNACE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.mrghastien.quantum_machinery.common.blocks.machines.MachineTile;
import com.mrghastien.quantum_machinery.common.capabilities.items.ModItemStackHandler;
import com.mrghastien.quantum_machinery.common.recipes.ModRecipeType;
import com.mrghastien.quantum_machinery.common.recipes.furnace.FurnaceRecipeWrapper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

public class ElectricFurnaceTile extends MachineTile<FurnaceRecipeWrapper>{
	
	private ModItemStackHandler inputHandler = new ModItemStackHandler(this, 1) {
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return stack.isEmpty() || ModRecipeType.SMELTING_WRAPPER.stream(world).anyMatch(r -> {
				return r.matches(stack);
			});
		};
	};
	private ModItemStackHandler outputHandler = new ModItemStackHandler(this);
	private CombinedInvWrapper invWrapper = new CombinedInvWrapper(inputHandler, outputHandler);
	
	public ElectricFurnaceTile() {
		super(ELECTRIC_FURNACE.get(), 50000, 512);
	}

	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
		return new ElectricFurnaceContainer(id, world, pos, playerInventory);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if(side == null)
				return LazyOptional.of(() -> invWrapper).cast();
		}
		return super.getCapability(cap, side);
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		inputHandler.deserializeNBT(compound.getCompound("Input"));
		outputHandler.deserializeNBT(compound.getCompound("Output"));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.put("Input", inputHandler.serializeNBT());
		compound.put("Output", outputHandler.serializeNBT());
		return compound;
	}

	@Override
	protected int getEnergyUsage() {
		return 32;
	}

	@Override
	protected ModItemStackHandler getInputInventory() {
		return inputHandler;
	}

	@Override
	protected ModItemStackHandler getOutputInventory() {
		return outputHandler;
	}

	@Override
	protected Collection<FurnaceRecipeWrapper> getRecipes() {
		ArrayList<FurnaceRecipeWrapper> recipes = new ArrayList<>();
		recipes.addAll(ModRecipeType.SMELTING_WRAPPER.getRecipes(world).values());
		return recipes;
	}

	@Override
	protected FurnaceRecipeWrapper getApplicableRecipe() {
		IItemHandler inv = getInputInventory();
		for(FurnaceRecipeWrapper r : getRecipes()) {
			if(r.matches(inv.getStackInSlot(0)))
				return r;
		}
		return null;
	}

	@Override
	protected int getProcessingTime() {
		return currentRecipe.getCookTime() / 3;
	}

	@Override
	protected Collection<ItemStack> getResults() {
		return Collections.singletonList(currentRecipe.getResult());
	}

	@Override
	protected Collection<Ingredient> getIngredients() {
		return Collections.singletonList(currentRecipe.getIngredient());
	}

}
