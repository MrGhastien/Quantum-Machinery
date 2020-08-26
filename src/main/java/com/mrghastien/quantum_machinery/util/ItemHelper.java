package com.mrghastien.quantum_machinery.util;

import com.mrghastien.quantum_machinery.setup.Setup;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

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
	
	public static boolean canItemsStack(ItemStack a, ItemStack b) {
		if(a.isEmpty() || b.isEmpty()) return true;
		return ItemHandlerHelper.canItemStacksStack(a, b) && a.getCount() + b.getCount() <= a.getMaxStackSize();
	}
	
	public static boolean isFuel(ItemStack stack) {
		return ForgeHooks.getBurnTime(stack) > 0;
	}
	
	public static NonNullList<ItemStack> getStacksFromHandler(IItemHandler handler) {
		NonNullList<ItemStack> stacks = NonNullList.create();
		for(int i = 0; i < handler.getSlots(); i++) {
			stacks.add(handler.getStackInSlot(i));
		}
		return stacks;
	}
	
	public static Item.Properties defaultProperties() {
		return new Item.Properties().group(Setup.MAIN_TAB);
	}
}
