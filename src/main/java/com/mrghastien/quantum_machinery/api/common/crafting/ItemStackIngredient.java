package com.mrghastien.quantum_machinery.api.common.crafting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.util.ItemHelper;

public class ItemStackIngredient extends Ingredient {

	public static final ItemStackIngredient EMPTY = new ItemStackIngredient(Stream.empty());
	
	public ItemStackIngredient(Stream<? extends IItemList> itemLists) {
		super(itemLists);
	}
	
	@Override
	public boolean test(ItemStack stack) {
		if(stack.isEmpty() || stack == null) {
			return super.test(stack);
		} else {
			for(ItemStack itemstack : this.getMatchingStacks()) {
	            if (ItemHelper.compareItemStacks(stack, itemstack)) {
	               return true;
	            }
	         }
			return false;
		}
	}
	
	public static ItemStackIngredient fromItemStacks(ItemStack... stacks) {
		return fromItemListStream(Arrays.stream(stacks).map(ItemStackList::new));
	}
	
	public static ItemStackIngredient fromTagStack(Tag<Item> tag, int count) {
		return fromItemListStream(Stream.of(new TagStackList(tag, count)));
	}
	
	public static ItemStackIngredient fromItemListStream(Stream<? extends Ingredient.IItemList> stream) {
        ItemStackIngredient ingredient = new ItemStackIngredient(stream);
        return ingredient.hasNoMatchingItems() ? ItemStackIngredient.EMPTY : ingredient;
    }
	
	private static IItemList deserializeItemStackList(JsonObject json) {
		if (json.has("item") && json.has("tag")) {
	         throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
	      } else if (json.has("item")) {
	         ResourceLocation resourcelocation1 = new ResourceLocation(JSONUtils.getString(json, "item"));
	         if(ForgeRegistries.ITEMS.containsKey(resourcelocation1)) {
	        	 Item item = ForgeRegistries.ITEMS.getValue(resourcelocation1);
	        	 int count = json.has("count") ? JSONUtils.getInt(json, "count") : 1;
		         return new ItemStackList(new ItemStack(item, count));
	         } else 
	            throw new JsonSyntaxException("Unknown item '" + resourcelocation1 + "'");
	      } else if (json.has("tag")) {
	         ResourceLocation resourcelocation = new ResourceLocation(JSONUtils.getString(json, "tag"));
	         Tag<Item> tag = ItemTags.getCollection().get(resourcelocation);
	         if (tag == null) {
	            throw new JsonSyntaxException("Unknown item tag '" + resourcelocation + "'");
	         } else {
	        	 int count = json.has("count") ? JSONUtils.getInt(json, "count") : 1;
	            return new TagStackList(tag, count);
	         }
	      } else {
	         throw new JsonParseException("An ingredient entry needs either a tag or an item");
	      }
	}
	

	public static NonNullList<ItemStackIngredient> readIngredients(JsonArray array) {
		NonNullList<ItemStackIngredient> nonnulllist = NonNullList.withSize(array.size(), ItemStackIngredient.EMPTY);

		for (int i = 0; i < array.size(); ++i) {
			JsonObject obj = array.get(i).getAsJsonObject();
			ItemStackIngredient ingredient = (ItemStackIngredient) CraftingHelper.getIngredient(obj);
			if (!ingredient.hasNoMatchingItems()) {
				nonnulllist.set(i, ingredient);
			}
		}

		return nonnulllist;
	}
	
	@Override
	public IIngredientSerializer<? extends Ingredient> getSerializer() {
		return Serializer.INSTANCE;
	}
	
	public static class ItemStackList implements IItemList {

		private final ItemStack stack;
		
		public ItemStackList(ItemStack stack) {
			this.stack = stack;
		}
		
		@Override
		public Collection<ItemStack> getStacks() {
			return Collections.singletonList(stack);
		}

		@Override
        public JsonObject serialize() {
            JsonObject json = new JsonObject();
            json.addProperty("type", Serializer.ID.toString());
            json.addProperty("item", stack.getItem().getRegistryName().toString());
            json.addProperty("count", stack.getCount());
            return json;
        }
	}
	
	public static class TagStackList implements IItemList {

		private Tag<Item> tag;
		private int count;
		
		public TagStackList(Tag<Item> tag, int count) {
			this.tag = tag;
			this.count = count;
		}
		
		@Override
		public Collection<ItemStack> getStacks() {
			List<ItemStack> list = new ArrayList<>();
			for(Item item : this.tag.getAllElements()) {
				list.add(new ItemStack(item, count));
			}
			
			if(list.size() == 0 && !ForgeConfig.SERVER.treatEmptyTagsAsAir.get()) {
                list.add(new ItemStack(net.minecraft.block.Blocks.BARRIER).setDisplayName(new net.minecraft.util.text.StringTextComponent("Empty Tag: " + tag.getId().toString())));
			}
			return list;
		}

		@Override
        public JsonObject serialize() {
            JsonObject json = new JsonObject();
            json.addProperty("type", Serializer.ID.toString());
            json.addProperty("tag", this.tag.getId().toString());
            json.addProperty("count", count);
            return json;
        }
		
	}
	
	public static class Serializer implements IIngredientSerializer<ItemStackIngredient> {
		public static final Serializer INSTANCE  = new Serializer();
        public static final ResourceLocation ID = QuantumMachinery.location("stacked_item");
		
		@Override
		public ItemStackIngredient parse(PacketBuffer buffer) {
			return fromItemListStream(Stream.generate(() -> new ItemStackList(buffer.readItemStack())));
		}

		@Override
		public ItemStackIngredient parse(JsonObject json) {
			return fromItemListStream(Stream.of(deserializeItemStackList(json)));
		}

		@Override
		public void write(PacketBuffer buffer, ItemStackIngredient ingredient) {
			ItemStack[] items = ingredient.getMatchingStacks();
			buffer.writeVarInt(items.length);

			for (ItemStack stack : items)
				buffer.writeItemStack(stack);
		}
	}
}
