package com.mrghastien.quantum_machinery.util.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class ItemHelper {

	public static boolean compareItemStacks(ItemStack itemStack1, ItemStack itemStack2) {
			if (itemStack1.getItem() == itemStack2.getItem()) {
				if (itemStack1.getCount() >= itemStack2.getCount()) {
					return true;
				}
			}
		return false;
	}
	
	public static boolean compareStackWithRecipe(IRecipe<?> recipe, ItemStack itemStack, int ingredientId) {
		for (ItemStack itemStack1 : recipe.getIngredients().get(ingredientId).getMatchingStacks()) {
			if (ItemHelper.compareItemStacks(itemStack, itemStack1)) {
				return true;
			} else if (ItemHelper.compareItemStacks(itemStack, itemStack1)) {
				return true;
			}
		}
		return false;
	}
}
