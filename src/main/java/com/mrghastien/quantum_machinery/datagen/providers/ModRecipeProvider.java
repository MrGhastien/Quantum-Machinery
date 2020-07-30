package com.mrghastien.quantum_machinery.datagen.providers;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.function.Consumer;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.common.init.MetalType;
import com.mrghastien.quantum_machinery.common.recipes.ItemStackIngredient;

import static com.mrghastien.quantum_machinery.datagen.builders.recipe.AlloySmeltingRecipeBuilder.alloySmeltingRecipe;

public class ModRecipeProvider extends RecipeProvider {
 
	public ModRecipeProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}
	
	@Override
	public String getName() {
		return "Recipes: " + QuantumMachinery.MODID;
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		alloySmeltingRecipe(MetalType.STEEL.getIngot().get(), 1, 120)
				.ingredient(ItemStackIngredient.fromItemStacks(new ItemStack(Items.IRON_INGOT)))
				.ingredient(ItemStackIngredient.fromItemStacks(new ItemStack(Items.COAL)))
				.build(consumer);
	}
}