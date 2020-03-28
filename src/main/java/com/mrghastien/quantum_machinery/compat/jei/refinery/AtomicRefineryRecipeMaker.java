package com.mrghastien.quantum_machinery.compat.jei.refinery;

import java.util.ArrayList;
import java.util.Collection;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.recipes.refinery.RefineryRecipe;

import net.minecraft.item.crafting.IRecipe;

public class AtomicRefineryRecipeMaker {

	
	public static Collection<RefineryRecipe> getRecipes() {
		Collection<RefineryRecipe> jeiRecipes = new ArrayList<RefineryRecipe>();
			for (IRecipe<?> iRecipe : QuantumMachinery.proxy.getClientWorld().getRecipeManager().getRecipes()) {
				if (iRecipe != null && iRecipe instanceof RefineryRecipe) {
					jeiRecipes.add((RefineryRecipe) iRecipe);
				}
			}
		return jeiRecipes;
	}
}
