package com.mrghastien.quantum_machinery.common.recipes;

import static com.mrghastien.quantum_machinery.QuantumMachinery.location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import com.mrghastien.quantum_machinery.common.init.ModRecipeTypes;
import com.mrghastien.quantum_machinery.common.recipes.alloy_smelting.AlloySmeltingRecipe;
import com.mrghastien.quantum_machinery.common.recipes.furnace.FurnaceRecipeWrapper;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Most of this code is "inspired" by PneumatiCraft : Repressurized, a mod made by Team Pneumatic.
 * Its Github gave me a really good example of a recipe system, so i took it.
 * @author MrGhastien
 *
 * @param <T>
 */
public class ModRecipeType<T extends BaseRecipe> implements IRecipeType<T> {
	
	private static final List<ModRecipeType<? extends BaseRecipe>> types = new ArrayList<>();
	private static final List<ModRecipeType<? extends BaseRecipe>> wrappers = new ArrayList<>();
	
	public static final ModRecipeType<AlloySmeltingRecipe> ALLOY_SMELTING = register(ModRecipeTypes.ALLOY_SMELTING);
	
	public static final Wrapper<FurnaceRecipeWrapper, FurnaceRecipe> SMELTING_WRAPPER = registerWrapper(IRecipeType.SMELTING, r -> new FurnaceRecipeWrapper(r.getId(), r));
	
	private static <T extends BaseRecipe> ModRecipeType<T> register(String name) {
		ModRecipeType<T> type = new ModRecipeType<>(name);
		types.add(type);
		return type;
	}
	
	private static <T extends BaseRecipe, R extends IRecipe<IInventory>> Wrapper<T, R> registerWrapper(IRecipeType<R> type, Function<R, T> func) {
		Wrapper<T, R> wrapper = new Wrapper<>(type, func);
		wrappers.add(wrapper);
		return wrapper;
	}
	
	public static void registerTypes(IForgeRegistry<IRecipeSerializer<?>> registry) {
		types.forEach(type -> Registry.register(Registry.RECIPE_TYPE, type.registryName, type));
	}
	
	private ResourceLocation registryName;
	final Map<ResourceLocation, T> cache = new HashMap<>();
	
	protected ModRecipeType(String name) {
        this.registryName = location(name);
    }
	
	public Map<ResourceLocation, T> getRecipes(World world) {
        if (world == null) {
            // we should pretty much always have a world, but here's a fallback
            world = ServerLifecycleHooks.getCurrentServer().getWorld(DimensionType.OVERWORLD);
            if (world == null) return Collections.emptyMap();
        }

        if (cache.isEmpty()) {
            RecipeManager recipeManager = world.getRecipeManager();
            List<T> recipes = recipeManager.getRecipes(this, BaseRecipe.DummyInventory.getInstance(), world);
            recipes.forEach(recipe -> cache.put(recipe.getId(), recipe));
        }
        return cache;
    }
	 	
	public Stream<T> stream(World world) {
		return getRecipes(world).values().stream();
	}
}
