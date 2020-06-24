package com.mrghastien.quantum_machinery.common.compat.jei;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.client.screens.RefineryScreen;
import com.mrghastien.quantum_machinery.common.blocks.refinery.RefineryContainer;
import com.mrghastien.quantum_machinery.common.compat.jei.refinery.AtomRefiningCategory;
import com.mrghastien.quantum_machinery.common.compat.jei.refinery.AtomicRefineryRecipeMaker;
import com.mrghastien.quantum_machinery.common.init.ModBlocks;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class JeiCompat implements IModPlugin {

	@Override
	public ResourceLocation getPluginUid() {
		return QuantumMachinery.location("jei_compat");
	}
	
	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		//final IJeiHelpers jeiHelpers = registration.getJeiHelpers();
		
		registration.addRecipes(AtomicRefineryRecipeMaker.getRecipes(), RecipeCategories.ATOM_REFINING);
	}
	
	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		final IJeiHelpers helpers = registration.getJeiHelpers();
		final IGuiHelper gui = helpers.getGuiHelper();
		
		registration.addRecipeCategories(new AtomRefiningCategory(gui));
	}
	
	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(RefineryScreen.class, 16, 40, 16, 16, RecipeCategories.ATOM_REFINING);
	}
	
	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(ModBlocks.REFINERY.get().asItem()), RecipeCategories.ATOM_REFINING);
	}
	
	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
		registration.addRecipeTransferHandler(RefineryContainer.class, RecipeCategories.ATOM_REFINING, 0, 3, 3, 36);
	}

}
