package mrghastien.quantum_machinery.common.compat.jei.alloySmelting;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mrghastien.quantum_machinery.QuantumMachinery;
import mrghastien.quantum_machinery.api.common.crafting.ItemStackIngredient;
import mrghastien.quantum_machinery.common.compat.jei.RecipeCategories;
import mrghastien.quantum_machinery.common.init.ModBlocks;
import mrghastien.quantum_machinery.common.recipes.AlloySmeltingRecipe;

public class AlloySmeltingCategory implements IRecipeCategory<AlloySmeltingRecipe>{
	
	private static final ResourceLocation TEXTURES = QuantumMachinery.location("textures/gui/alloy_smelter_gui.png");
	
	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawableAnimated flame;
	
	public AlloySmeltingCategory(IGuiHelper helper) {
		IDrawableStatic staticFlame = helper.createDrawable(TEXTURES, 176, 0, 14, 14);
		flame = helper.createAnimatedDrawable(staticFlame, 60, IDrawableAnimated.StartDirection.BOTTOM, false);
		background = helper.createDrawable(TEXTURES, 38, 13, 110, 78);
		icon = helper.createDrawableIngredient(new ItemStack(ModBlocks.ALLOY_SMELTER.get()));
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
		List<List<ItemStack>> superList = new ArrayList<>();
		for(ItemStackIngredient ing : recipe.getInputs()) {
			List<ItemStack> stacks = new ArrayList<>();
			stacks.addAll(Arrays.asList(ing.getMatchingStacks()));
			superList.add(stacks);
		}
		
		ingredients.setInputLists(VanillaTypes.ITEM, superList);
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getResult());
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, AlloySmeltingRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
		stacks.init(0, true, 23, 3);
		stacks.init(1, true, 3, 19);
		stacks.init(2, true, 3, 41);
		stacks.init(3, true, 23, 57);
		stacks.init(4, false, 85, 31);
		stacks.set(ingredients);
	}
	
	@Override
	public void draw(AlloySmeltingRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		flame.draw(matrixStack, 86, 56);
	}
}