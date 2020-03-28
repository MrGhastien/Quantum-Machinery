package com.mrghastien.quantum_machinery.compat.jei.refinery;

import java.util.ArrayList;
import java.util.List;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.compat.jei.RecipeCategories;
import com.mrghastien.quantum_machinery.recipes.refinery.RefineryRecipe;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class AtomRefiningCategory implements IRecipeCategory<RefineryRecipe>{
	
	private static final ResourceLocation TEXTURES = QuantumMachinery.location("textures/gui/refinery_gui.png");
	private static final ResourceLocation ICON = QuantumMachinery.location("textures/item/astronium_ingot.png");

	protected static final int INPUT = 0;
	protected static final int INPUT_1 = 1;
	protected static final int OUTPUT = 2;
	
	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawableAnimated progressArrow;
	private final IDrawableAnimated energyBar;
	
	public AtomRefiningCategory(IGuiHelper helper) {
		IDrawableStatic staticProgressArrow = helper.createDrawable(TEXTURES, 179, 0, 45, 25);
		progressArrow = helper.createAnimatedDrawable(staticProgressArrow, 120, IDrawableAnimated.StartDirection.LEFT, false);
		IDrawableStatic staticEnergyBar = helper.createDrawable(TEXTURES, 176, 0, 3, 61);
		energyBar = helper.createAnimatedDrawable(staticEnergyBar, 480, IDrawableAnimated.StartDirection.TOP, true);
		background = helper.createDrawable(TEXTURES, 4, 5, 168, 88);
		icon = helper.createDrawable(ICON, 0, 0, 16, 16);
	}
	@Override
	public ResourceLocation getUid() {
		return RecipeCategories.ATOM_REFINING;
	}

	@Override
	public Class<? extends RefineryRecipe> getRecipeClass() {
		return RefineryRecipe.class;
	}

	@Override
	public String getTitle() {
		return "Atom Refining";
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
	public void setIngredients(RefineryRecipe recipe, IIngredients ingredients) {
		List<ItemStack> inputs = new ArrayList<>();
		inputs.add(recipe.getIngredients().get(0).getMatchingStacks()[0]);
		inputs.add(recipe.getIngredients().get(1).getMatchingStacks()[0]);
		ingredients.setInputs(VanillaTypes.ITEM, inputs);
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, RefineryRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		stacks.init(INPUT, true, 57, 11);
		stacks.init(INPUT_1, true, 57, 56);
		stacks.init(OUTPUT, false, 119, 34);
		stacks.set(ingredients);
	}
	
	@Override
	public List<String> getTooltipStrings(RefineryRecipe recipe, double mouseX, double mouseY) {
		List<String> tooltips = new ArrayList<String>();
		if(mouseX > 39 && mouseX < 39 + 3 && mouseY > 12 && mouseY < 12 + 61) {
			tooltips.add("Energy usage : " + recipe.getEnergyUsage() + " E/t");
		}
		return tooltips;
	}
	
	@Override
	public void draw(RefineryRecipe recipe, double mouseX, double mouseY) {
		progressArrow.draw(39 + 24, 12 + 18);
		energyBar.draw(39, 12);
	}

}
