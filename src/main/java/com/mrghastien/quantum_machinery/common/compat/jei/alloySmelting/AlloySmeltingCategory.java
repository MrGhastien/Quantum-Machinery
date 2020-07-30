package com.mrghastien.quantum_machinery.common.compat.jei.alloySmelting;

import java.util.ArrayList;
import java.util.List;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.common.compat.jei.RecipeCategories;
import com.mrghastien.quantum_machinery.common.recipes.alloy_smelting.AlloySmeltingRecipe;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.util.ResourceLocation;

public class AlloySmeltingCategory implements IRecipeCategory<AlloySmeltingRecipe>{
	
	private static final ResourceLocation TEXTURES = QuantumMachinery.location("textures/gui/alloy_smelting_gui.png");
	private static final ResourceLocation ICON = QuantumMachinery.location("textures/item/astronium_ingot.png");
	
	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawableAnimated flame;
	
	public AlloySmeltingCategory(IGuiHelper helper) {
		IDrawableStatic staticFlame = helper.createDrawable(TEXTURES, 176, 0, 14, 14);
		flame = helper.createAnimatedDrawable(staticFlame, 60, IDrawableAnimated.StartDirection.BOTTOM, false);
		background = helper.createDrawable(TEXTURES, 38, 13, 110, 78);
		icon = helper.createDrawable(ICON, 0, 0, 16, 16);
	}
	@Override
	public ResourceLocation getUid() {
		return RecipeCategories.ALLOY_SMELTING;
	}

	@Override
	public Class<? extends AlloySmeltingRecipe> getRecipeClass() {
		return AlloySmeltingRecipe.class;
	}

	@Override
	public String getTitle() {
		return "Alloy Smelting";
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(AlloySmeltingRecipe recipe, IIngredients ingredients) {
		ingredients.setInputIngredients(recipe.getIngredients());
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, AlloySmeltingRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		stacks.init(0, true, 62, 17);
		stacks.init(1, true, 42, 33);
		stacks.init(2, true, 42, 55);
		stacks.init(3, true, 62, 71);
		stacks.init(4, false, 124, 45);
		stacks.set(ingredients);
	}
	
	@Override
	public List<String> getTooltipStrings(AlloySmeltingRecipe recipe, double mouseX, double mouseY) {
		List<String> tooltips = new ArrayList<>();
		return tooltips;
	}
	
	@Override
	public void draw(AlloySmeltingRecipe recipe, double mouseX, double mouseY) {
		flame.draw(124, 69);
	}
}