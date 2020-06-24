package com.mrghastien.quantum_machinery.common.recipes.refinery;

import com.mrghastien.quantum_machinery.common.init.ModBlocks;
import com.mrghastien.quantum_machinery.common.init.ModRecipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class RefineryRecipe implements IRecipe<IInventory>{

	public NonNullList<Ingredient> ingredients;
	public String group = "";
	public ItemStack result;
	public int energyUsage;
	public int ticks;
	public ResourceLocation recipeId;

	public RefineryRecipe(ResourceLocation recipeId, NonNullList<Ingredient> ingredients, ItemStack result, int energyUsage, int ticks) {
		super();
		this.recipeId = recipeId;
		this.ingredients = ingredients;
		this.result = result;
		this.energyUsage = energyUsage;
		this.ticks = ticks;
	}

	@Override
	public boolean matches(IInventory inv, World worldIn) {
			return (ingredients.get(0).test(inv.getStackInSlot(0)) && ingredients.get(1).test(inv.getStackInSlot(1)))
					|| (ingredients.get(0).test(inv.getStackInSlot(1)) && ingredients.get(1).test(inv.getStackInSlot(0)));
	}
	
	public boolean matches(IItemHandler handler) {
			return (ingredients.get(0).test(handler.getStackInSlot(0)) && ingredients.get(1).test(handler.getStackInSlot(1)))
					|| (ingredients.get(0).test(handler.getStackInSlot(1)) && ingredients.get(1).test(handler.getStackInSlot(0)));
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}
	
	@Override
	public ItemStack getCraftingResult(IInventory inv) {
		return result.copy();
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return result;
	}

	@Override
	public ResourceLocation getId() {
		return recipeId;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipes.REFINING_SERIALIZER.get();
	}
	
	@Override
    public ItemStack getIcon () {
        return new ItemStack(ModBlocks.REFINERY.get());
    }

	@Override
	public IRecipeType<?> getType() {
		return ModRecipes.REFINING_TYPE;
	}
	
	public int getEnergyUsage() {
		return energyUsage;
	}

	public int getTicks() {
		return ticks;
	}

}
