package com.mrghastien.quantum_machinery.datagen.builders.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ModRecipeBuilder<T extends ModRecipeBuilder<T>> {

	protected final List<ICondition> conditions = new ArrayList<>();
	protected final Advancement.Builder advancementBuilder = Advancement.Builder.builder();
	protected final ResourceLocation serializerName;

	public ModRecipeBuilder(ResourceLocation serializerName) {
		this.serializerName = serializerName;
	}

    @SuppressWarnings("unchecked")
	public T addCriterion(String name, ICriterionInstance criterion) {
        advancementBuilder.withCriterion(name, criterion);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
	public T addCondition(ICondition condition) {
        conditions.add(condition);
        return (T) this;
    }
	
	protected abstract Result getResult(ResourceLocation id);
	
	public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
		this.validate(id);
		this.advancementBuilder.withParentId(new ResourceLocation("recipes/root"))
				.withCriterion("has_the_recipe", new RecipeUnlockedTrigger.Instance(id))
				.withRewards(AdvancementRewards.Builder.recipe(id)).withRequirementsStrategy(IRequirementsStrategy.OR);
		consumerIn.accept(getResult(id));
	}
	
	protected void validate(ResourceLocation id) {
		if (this.advancementBuilder.getCriteria().isEmpty()) {
			throw new IllegalStateException("No way of obtaining recipe " + id);
		}
	}
	
	protected abstract class Result implements IFinishedRecipe {

		protected final ResourceLocation id;
		protected final ResourceLocation advancementId;
		
		public Result(ResourceLocation id) {
			this.id = id;
			this.advancementId = new ResourceLocation(id.getNamespace(), "recipes/" + id.getPath());
		}
		
		@Override
		public JsonObject getRecipeJson() {
			JsonObject json = new JsonObject();
			if(!conditions.isEmpty()) {
				JsonArray array = new JsonArray();
				for (ICondition condition : conditions) {
					array.add(CraftingHelper.serialize(condition));
				}
				json.add("conditions", array);
			}
			this.serialize(json);
			return json;
		}

		@Override
		public ResourceLocation getID() {
			return id;
		}

		@Override
		public IRecipeSerializer<?> getSerializer() {
			return ForgeRegistries.RECIPE_SERIALIZERS.getValue(serializerName);
		}

		
		@Override
		public JsonObject getAdvancementJson() {
			return advancementBuilder.serialize();
		}

		@Override
		
		public ResourceLocation getAdvancementID() {
			return advancementId;
		}
		
	}
	
	
}
