package mrghastien.quantum_machinery.common.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import mrghastien.quantum_machinery.api.common.crafting.BaseRecipe;
import mrghastien.quantum_machinery.api.common.crafting.ItemStackIngredient;
import mrghastien.quantum_machinery.api.common.crafting.ModRecipeType;
import mrghastien.quantum_machinery.common.init.ModBlocks;
import mrghastien.quantum_machinery.common.init.ModRecipes;

public class AlloySmeltingRecipe extends BaseRecipe {

	private NonNullList<ItemStackIngredient> inputs;
	private ItemStack result;
	private int ticks;

	public AlloySmeltingRecipe(ResourceLocation recipeId, NonNullList<ItemStackIngredient> inputs, ItemStack result,
			int ticks) {
		super(recipeId);
		this.inputs = NonNullList.from(ItemStackIngredient.EMPTY, inputs.toArray(new ItemStackIngredient[0]));
		this.result = result;
		this.ticks = ticks;
	}

	public boolean matches(ItemStack... stacks) {
		if(stacks.length != inputs.size())
			return false;
		//1. For each ingredients, check if any of the provided ItemStacks matches with the ingredient.
		//2. If any of the 1st checks fails, return false.
		return inputs.stream().allMatch(ing -> Stream.of(stacks).anyMatch(stack -> ing.test(stack)));
	}

	public NonNullList<ItemStackIngredient> getInputs() {
		return inputs;
	}

	public ItemStack getResult() {
		return result.copy();
	}

	public int getTicks() {
		return ticks;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModRecipes.ALLOY_SMELTING.get();
	}

	@Override
	public ItemStack getIcon() {
		return new ItemStack(ModBlocks.ALLOY_SMELTER.get());
	}

	@Override
	public IRecipeType<?> getType() {
		return ModRecipeType.ALLOY_SMELTING;
	}

	@Override
	public String getGroup() {
		return ModBlocks.ALLOY_SMELTER.get().getRegistryName().getPath();
	}

	@Override
	public void write(PacketBuffer buf) {
		for (ItemStackIngredient input : inputs) {
			input.write(buf);
		}
		buf.writeItemStack(result);
		buf.writeInt(ticks);
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<AlloySmeltingRecipe> {
		
		@Override
		public AlloySmeltingRecipe read(ResourceLocation recipeId, JsonObject json) {
			JsonElement jsonelement = JSONUtils.isJsonArray(json, "ingredients")
					? JSONUtils.getJsonArray(json, "ingredients")
					: JSONUtils.getJsonObject(json, "ingredients");
			// Récupération des ingrédients
			NonNullList<ItemStackIngredient> ingredients = ItemStackIngredient.readIngredients(JSONUtils.getJsonArray(jsonelement, "ingredients"));
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
			int t = JSONUtils.getInt(json, "ticks", 120);
			return new AlloySmeltingRecipe(recipeId, ingredients, itemstack, t);
		}

		@Override
		public AlloySmeltingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			int i = buffer.readInt();
			NonNullList<ItemStackIngredient> ingredients = NonNullList.withSize(i, ItemStackIngredient.EMPTY);

			for (int j = 0; j < ingredients.size(); ++j) {
				Ingredient ing = Ingredient.read(buffer);
				if(ing instanceof ItemStackIngredient)
					ingredients.set(j, (ItemStackIngredient) ing);
			}

			ItemStack itemstack = buffer.readItemStack();
			int t = buffer.readInt();
			return new AlloySmeltingRecipe(recipeId, ingredients, itemstack, t);
		}

		@Override
		public void write(PacketBuffer buffer, AlloySmeltingRecipe recipe) {
			recipe.write(buffer);
		}
	}
}
