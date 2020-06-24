package com.mrghastien.quantum_machinery.datagen;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mrghastien.quantum_machinery.common.init.ModRecipes;
import com.mrghastien.quantum_machinery.common.recipes.refinery.RefineryRecipe;
import com.mrghastien.quantum_machinery.common.recipes.refinery.RefineryRecipeSerializer;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class RefineryRecipeBuilder {
	private final Item result;
	private final NonNullList<Ingredient> ingredients;
	private final int energyUsage;
	private final int ticks;
	private final Advancement.Builder advancementBuilder = Advancement.Builder.builder();
	private String group;
	private final RefineryRecipeSerializer recipeSerializer;
	private int outCount;

	private RefineryRecipeBuilder(IItemProvider resultIn, int outCount, NonNullList<Ingredient> ingredients,
			int energyUsage, int ticks, RefineryRecipeSerializer serializer) {
		this.result = resultIn.asItem();
		this.ingredients = ingredients;
		this.energyUsage = energyUsage;
		this.ticks = ticks;
		this.recipeSerializer = serializer;
	}

	/**Used to make a recipe.
	 * 
	 * @param resultIn The item that will be crafted (the result).
	 * @param outCount The amount of the result item per craft.
	 * @param ingredients A NonNullList of ingredients. If there is more than 2 ingredients, only the first 2 will be used.
	 * @param energyUsage The amount of energy used each tick by the Refinery.
	 * @param ticks The duration of the crafting process in ticks (1/20 sec)
	 * 
	 * @return An instance of {@link RefineryRecipeBuilder} containing all of the data passed to the method.
	 */
	public static RefineryRecipeBuilder refiningRecipe(IItemProvider resultIn, int outCount, NonNullList<Ingredient> ingredients, int energyUsage, int ticks) {
		NonNullList<Ingredient> ingredients1 = ingredients;
		if(ingredients.size() > 2) {
			ingredients1 = NonNullList.create();
			ingredients1.add(ingredients.get(0));
			ingredients1.add(ingredients.get(1));
		}
		return new RefineryRecipeBuilder(resultIn, outCount, ingredients1, energyUsage, ticks,
				(RefineryRecipeSerializer) ModRecipes.REFINING_SERIALIZER.get());
	}
	
	public static RefineryRecipeBuilder refiningRecipe(IItemProvider resultIn, int outCount, NonNullList<Ingredient> ingredients) {
		return refiningRecipe(resultIn, outCount, ingredients);
	}
	
	public static RefineryRecipeBuilder refiningRecipe(IItemProvider resultIn, int outCount, ItemStack ingredient1, ItemStack ingredient2, int energyUsage, int ticks) {
		NonNullList<Ingredient> ingredients = NonNullList.create();
		ingredients.add(Ingredient.fromStacks(ingredient1));
		ingredients.add(Ingredient.fromStacks(ingredient2));
		return refiningRecipe(resultIn, outCount, ingredients, energyUsage, ticks);
	}
	
	public static RefineryRecipeBuilder refiningRecipe(IItemProvider resultIn, int outCount, ItemStack ingredient1, ItemStack ingredient2) {
		return refiningRecipe(resultIn, outCount, ingredient1, ingredient2, 150, 120);
	}

	/**Used to add criterions for advancements. 
	 */
	public RefineryRecipeBuilder addCriterion(String name, ICriterionInstance criterionIn) {
		this.advancementBuilder.withCriterion(name, criterionIn);
		return this;
	}

	public void build(Consumer<IFinishedRecipe> consumerIn) {
		this.build(consumerIn, ForgeRegistries.ITEMS.getKey(this.result.getItem()));
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
		ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result.getItem());
		ResourceLocation resourcelocation1 = ResourceLocation.tryCreate(save);
		if (resourcelocation1.equals(resourcelocation)) {
			throw new IllegalStateException("Recipe " + resourcelocation1 + " should remove its 'save' argument");
		} else {
			this.build(consumerIn, resourcelocation1);
		}
	}

	public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
		this.validate(id);
		this.advancementBuilder.withParentId(new ResourceLocation("recipes/root"))
				.withCriterion("has_the_recipe", new RecipeUnlockedTrigger.Instance(id))
				.withRewards(AdvancementRewards.Builder.recipe(id)).withRequirementsStrategy(IRequirementsStrategy.OR);
		consumerIn.accept(new RefineryRecipeBuilder.Result(id, this.group == null ? "" : this.group, this.result,
				this.outCount, this.ingredients, this.energyUsage, this.ticks, this.advancementBuilder,
				new ResourceLocation(id.getNamespace(),
						"recipes/" + this.result.getItem().getGroup().getPath() + "/" + id.getPath()),
				this.recipeSerializer));
	}

	/**
	 * Makes sure that this obtainable.
	 */
	private void validate(ResourceLocation id) {
		if (this.advancementBuilder.getCriteria().isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + id);
		}
	}

	public static class Result implements IFinishedRecipe {
		private final ResourceLocation id;
		private final String group;
		private final NonNullList<Ingredient> ingredients;
		private final Item result;
		private final int outCount;
		private final int energyUsage;
		private final int ticks;
		private final Advancement.Builder advancementBuilder;
		private final ResourceLocation advancementId;
		private final IRecipeSerializer<? extends RefineryRecipe> serializer;

		public Result(ResourceLocation idIn, String groupIn, IItemProvider resultIn, int outCount,
				NonNullList<Ingredient> ingredients, int energyUsage, int ticks,
				Advancement.Builder advancementBuilderIn, ResourceLocation advancementIdIn,
				IRecipeSerializer<? extends RefineryRecipe> serializerIn) {

			this.id = idIn;
			this.group = groupIn;
			this.ingredients = ingredients;
			this.result = resultIn.asItem();
			this.outCount = outCount;
			this.energyUsage = energyUsage;
			this.ticks = ticks;
			this.advancementBuilder = advancementBuilderIn;
			this.advancementId = advancementIdIn;
			this.serializer = serializerIn;
		}

		public void serialize(JsonObject json) {
			if (!this.group.isEmpty()) {
				json.addProperty("group", this.group);
			}

			JsonArray jsonarray = new JsonArray();

			for (int i = 0; i < ingredients.size(); i++) {

					ItemStack item = this.ingredients.get(i).getMatchingStacks()[0];
					JsonObject jsonobjectR = new JsonObject();
					jsonobjectR.addProperty("item", ForgeRegistries.ITEMS.getKey(item.getItem()).toString());
					if (item.getCount() > 1) {
						jsonobjectR.addProperty("count", item.getCount());
					}

					jsonarray.add(jsonobjectR);
			}

			json.add("ingredients", jsonarray);

			JsonObject jsonobjectR = new JsonObject();
			jsonobjectR.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());
			if (this.outCount > 1) {
				jsonobjectR.addProperty("count", this.outCount);
			}

			json.add("result", jsonobjectR);

			json.addProperty("energyUsage", this.energyUsage);
			json.addProperty("ticks", this.ticks);
		}

		public IRecipeSerializer<?> getSerializer() {
			return this.serializer;
		}

		/**
		 * Gets the ID for the recipe.
		 */
		public ResourceLocation getID() {
			return this.id;
		}

		/**
		 * Gets the JSON for the advancement that unlocks this recipe. Null if there is
		 * no advancement.
		 */
		@Nullable
		public JsonObject getAdvancementJson() {
			return this.advancementBuilder.serialize();
		}

		/**
		 * Gets the ID for the advancement associated with this recipe. Should not be
		 * null if {@link #getAdvancementJson} is non-null.
		 */
		@Nullable
		public ResourceLocation getAdvancementID() {
			return this.advancementId;
		}
	}
}
