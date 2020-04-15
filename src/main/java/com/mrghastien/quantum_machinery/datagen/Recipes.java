package com.mrghastien.quantum_machinery.datagen;

import java.util.function.Consumer;

import com.mrghastien.quantum_machinery.init.ModItems;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class Recipes extends RecipeProvider {

	public Recipes(DataGenerator generatorIn) {
		super(generatorIn);
	}
	
	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		RefineryRecipeBuilder.refiningRecipe(ModItems.DENSE_COMPOUND.get(), 2, new ItemStack(Items.IRON_INGOT, 1), new ItemStack(Items.OBSIDIAN, 4))
				.addCriterion("dense_compound", InventoryChangeTrigger.Instance.forItems(ModItems.DENSE_COMPOUND.get()))
				.build(consumer);
		
		RefineryRecipeBuilder.refiningRecipe(ModItems.HCC_INGOT.get(), 3, new ItemStack(Items.IRON_INGOT, 1), new ItemStack(Items.REDSTONE, 6))
				.addCriterion("conductive_compound", InventoryChangeTrigger.Instance.forItems(ModItems.HCC_INGOT.get()))
				.build(consumer);
	}
}
