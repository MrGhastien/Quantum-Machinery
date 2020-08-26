package com.mrghastien.quantum_machinery.common.compat.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.api.common.crafting.ModRecipeType;
import com.mrghastien.quantum_machinery.client.screens.AlloySmelterScreen;
import com.mrghastien.quantum_machinery.client.screens.ElectricFurnaceScreen;
import com.mrghastien.quantum_machinery.common.blocks.machines.alloy_smelter.AlloySmelterContainer;
import com.mrghastien.quantum_machinery.common.compat.jei.alloySmelting.AlloySmeltingCategory;
import com.mrghastien.quantum_machinery.common.init.ModBlocks;
import com.mrghastien.quantum_machinery.common.recipes.AlloySmeltingRecipe;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;

@JeiPlugin
public class JeiCompat implements IModPlugin {

	@Override
	public ResourceLocation getPluginUid() {
		return QuantumMachinery.location("jei_compat");
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		Minecraft instance = Minecraft.getInstance();
		Collection<AlloySmeltingRecipe> recipes = ModRecipeType.ALLOY_SMELTING.getRecipes(instance.world).values();
		registration.addRecipes(recipes, RecipeCategories.ALLOY_SMELTING);
	}
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		final IJeiHelpers helpers = registration.getJeiHelpers();
		final IGuiHelper gui = helpers.getGuiHelper();
		
		registration.addRecipeCategories(new AlloySmeltingCategory(gui));
	}
	
	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(AlloySmelterScreen.class, 124, 69, 14, 14, RecipeCategories.ALLOY_SMELTING);
		registration.addRecipeClickArea(ElectricFurnaceScreen.class, 86, 39, 26, 16, VanillaRecipeCategoryUid.FURNACE);
	}
	
	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.ALLOY_SMELTER.get().asItem()), RecipeCategories.ALLOY_SMELTING);
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.ELECTRIC_FURNACE.get().asItem()), VanillaRecipeCategoryUid.FURNACE);
	}
	
	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(AlloySmelterContainer.class, RecipeCategories.ALLOY_SMELTING, 0, 4, 5, 36);
	}

}
