package mrghastien.quantum_machinery.datagen.builders.recipe;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import static mrghastien.quantum_machinery.QuantumMachinery.location;

import mrghastien.quantum_machinery.QuantumMachinery;
import mrghastien.quantum_machinery.api.common.crafting.ItemStackIngredient;
import mrghastien.quantum_machinery.common.init.ModRecipeTypes;
import mrghastien.quantum_machinery.datagen.CriterionHelper;

public class AlloySmeltingRecipeBuilder extends ModRecipeBuilder<AlloySmeltingRecipeBuilder> {
	
	private final NonNullList<ItemStackIngredient> inputs = NonNullList.create();
	private final ItemStack result;
	private final int ticks;
	
	private AlloySmeltingRecipeBuilder(IItemProvider result, int count, int processingTime) {
		super(location(ModRecipeTypes.ALLOY_SMELTING));
		this.result = new ItemStack(result, count);
		this.ticks = processingTime;
	}
	
	public static AlloySmeltingRecipeBuilder alloySmeltingRecipe(IItemProvider result, int count, int ticks) {
		return new AlloySmeltingRecipeBuilder(result, count, ticks).addCriterion(result.asItem().getRegistryName().getPath(), CriterionHelper.hasItem(result));
	}
	
	public AlloySmeltingRecipeBuilder ingredient(ItemStackIngredient ingredient) {
		inputs.add(ingredient);
		return this;
	}
	
	public AlloySmeltingRecipeBuilder ingredient(IItemProvider item, int count) {
		return ingredient(ItemStackIngredient.fromItemStacks(new ItemStack(item, count)));
	}
	
	public AlloySmeltingRecipeBuilder ingredient(INamedTag<Item> tag, int count) {
		return ingredient(ItemStackIngredient.fromTagStack(tag, count));
	}

	public void build(Consumer<IFinishedRecipe> consumer) {
		this.build(consumer, QuantumMachinery.location("alloy_smelting/" + ForgeRegistries.ITEMS.getKey(this.result.getItem()).getPath()));
	}

	@Override
	protected ModRecipeBuilder<AlloySmeltingRecipeBuilder>.Result getResult(ResourceLocation id) {
		return new AlloySmeltingRecipeResult(id);
	}

	public class AlloySmeltingRecipeResult extends Result {

		public AlloySmeltingRecipeResult(ResourceLocation id) {
			super(id);
		}

		@Override
		public void serialize(JsonObject json) {
			JsonArray ingredients = new JsonArray();

			for (int i = 0; i < inputs.size(); i++) {
				ingredients.add(inputs.get(i).serialize());
			}

			json.add("ingredients", ingredients);

			JsonObject jsonobjectR = new JsonObject();
			jsonobjectR.addProperty("item", ForgeRegistries.ITEMS.getKey(result.getItem()).toString());
			if (result.getCount() > 1) {
				jsonobjectR.addProperty("count", result.getCount());
			}
			json.add("result", jsonobjectR);
			json.addProperty("ticks", ticks);
		}
	}
}
