package com.mrghastien.quantum_machinery.common.recipes.furnace;

import com.mrghastien.quantum_machinery.common.recipes.BaseRecipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public class FurnaceRecipeWrapper extends BaseRecipe {

	private final FurnaceRecipe source;
	
	public FurnaceRecipeWrapper(ResourceLocation id, FurnaceRecipe source) {
		super(id);
		this.source = source;
	}
	
	public boolean matches(ItemStack stack) {
		return source.getIngredients().get(0).test(stack);
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return IRecipeSerializer.SMELTING;
	}

	@Override
	public IRecipeType<?> getType() {
		return IRecipeType.SMELTING;
	}

	@Override
	public void write(PacketBuffer buf) {
		
	}

	public Ingredient getIngredient() {
		return source.getIngredients().get(0);
	}
	
	public ItemStack getResult() {
		return source.getCraftingResult(DummyInventory.getInstance());
	}
	
	public int getCookTime() {
		return source.getCookTime();
	}
	
	public float getXp() {
		return source.getExperience();
	}
	
}
