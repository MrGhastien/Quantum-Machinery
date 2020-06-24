package com.mrghastien.quantum_machinery.common.recipes.refinery;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class RefineryRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
		implements IRecipeSerializer<RefineryRecipe> {

	@Override
	public RefineryRecipe read(ResourceLocation recipeId, JsonObject json) {
		JsonElement jsonelement = (JsonElement) (JSONUtils.isJsonArray(json, "ingredients")
				? JSONUtils.getJsonArray(json, "ingredients")
				: JSONUtils.getJsonObject(json, "ingredients"));
		// Récupération des ingrédients
		NonNullList<Ingredient> ingredients = readIngredients(JSONUtils.getJsonArray(jsonelement, "ingredients"));
		// Forge: Check if primitive string to keep vanilla or a object which can
		// contain a count field.
		if (!json.has("result"))
			throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
		ItemStack itemstack;
		if (json.get("result").isJsonObject())
			itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
		else {
			String s1 = JSONUtils.getString(json, "result");
			ResourceLocation resourcelocation = ResourceLocation.tryCreate(s1);
			itemstack = new ItemStack(ForgeRegistries.ITEMS.getValue(resourcelocation));
		}
		int e = JSONUtils.getInt(json, "energyUsage", 150);
		int t = JSONUtils.getInt(json, "ticks", 120);
		return new RefineryRecipe(recipeId, ingredients, itemstack, e, t);
	}

	private static NonNullList<Ingredient> readIngredients(JsonArray array) {
		NonNullList<Ingredient> nonnulllist = NonNullList.withSize(2, Ingredient.EMPTY);

		for (int i = 0; i < array.size(); ++i) {
			ItemStack stack = ShapedRecipe.deserializeItem(array.get(i).getAsJsonObject());
					//JSONUtils.getInt(array.get(i).getAsJsonObject(), "count", 1);
			Ingredient ingredient = Ingredient.fromStacks(stack);
			if (!ingredient.hasNoMatchingItems()) {
				nonnulllist.set(i, ingredient);
			}
		}

		return nonnulllist;
	}

	@Override
	public RefineryRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
		int i = buffer.readInt();
		NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

		for (int j = 0; j < ingredients.size(); ++j) {
			ingredients.set(j, Ingredient.read(buffer));
		}

		ItemStack itemstack = buffer.readItemStack();
		int e = buffer.readInt();
		int t = buffer.readInt();
		return new RefineryRecipe(recipeId, ingredients, itemstack, e, t);
	}

	public void write(PacketBuffer buffer, RefineryRecipe recipe) {
		buffer.writeInt(recipe.ingredients.size());
		for (Ingredient ing : recipe.ingredients) {
			ing.write(buffer);
		}
		buffer.writeItemStack(recipe.result);
		buffer.writeInt(recipe.energyUsage);
		buffer.writeInt(recipe.ticks);
	}
}
