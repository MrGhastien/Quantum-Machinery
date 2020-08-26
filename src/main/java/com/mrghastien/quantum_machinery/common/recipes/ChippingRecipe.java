package com.mrghastien.quantum_machinery.common.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mrghastien.quantum_machinery.api.common.crafting.BaseRecipe;
import com.mrghastien.quantum_machinery.api.common.crafting.ItemStackIngredient;
import com.mrghastien.quantum_machinery.api.common.crafting.ModRecipeType;
import com.mrghastien.quantum_machinery.common.init.ModRecipes;

public class ChippingRecipe extends BaseRecipe {

	private ItemStackIngredient input;
	private int duration;
	private ItemStack output;
	
	public ChippingRecipe(ResourceLocation id, ItemStackIngredient input, int duration, ItemStack output) {
		super(id);
		this.input = input;
		this.duration = duration;
		this.output = output;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipes.CHIPPING.get();
	}

	@Override
	public IRecipeType<?> getType() {
		return ModRecipeType.CHIPPING;
	}

	@Override
	public void write(PacketBuffer buf) {
		input.write(buf);
		buf.writeVarInt(duration);
		buf.writeItemStack(output);
	}
	
	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ChippingRecipe> {
		
		@Override
		public ChippingRecipe read(ResourceLocation recipeId, JsonObject json) {
			ItemStackIngredient input = (ItemStackIngredient) CraftingHelper.getIngredient(json);
			JsonElement outputElement = json.get("output");
			if(outputElement == null) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
			ItemStack output = CraftingHelper.getItemStack(outputElement.getAsJsonObject(), true);
			int duration = JSONUtils.getInt(json, "duration", 20);
			return new ChippingRecipe(recipeId, input, duration, output);
		}

		@Override
		public ChippingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			ItemStackIngredient ing = (ItemStackIngredient) Ingredient.read(buffer);
			int duration = buffer.readVarInt();
			ItemStack output = buffer.readItemStack();
			return new ChippingRecipe(recipeId, ing, duration, output);
		}

		@Override
		public void write(PacketBuffer buffer, ChippingRecipe recipe) {
			recipe.write(buffer);
		}		
	}
}