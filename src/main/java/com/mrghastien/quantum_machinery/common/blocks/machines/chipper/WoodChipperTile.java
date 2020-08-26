package com.mrghastien.quantum_machinery.common.blocks.machines.chipper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.Collection;

import com.mrghastien.quantum_machinery.common.blocks.machines.MachineTile;
import com.mrghastien.quantum_machinery.common.capabilities.items.ModItemStackHandler;
import com.mrghastien.quantum_machinery.common.init.ModTileEntities;
import com.mrghastien.quantum_machinery.common.recipes.ChippingRecipe;

public class WoodChipperTile extends MachineTile<ChippingRecipe> {

	public WoodChipperTile() {
		super(ModTileEntities.WOOD_CHIPPER.get(), 10000, 512);
	}

	@Override
	protected int getEnergyUsage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected ModItemStackHandler getInputInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ModItemStackHandler getOutputInventory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Collection<ChippingRecipe> getRecipes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ChippingRecipe getApplicableRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getProcessingTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected Collection<ItemStack> getResults() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Collection<Ingredient> getIngredients() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
		// TODO Auto-generated method stub
		return null;
	}

}
