	package mrghastien.quantum_machinery.api.common.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static mrghastien.quantum_machinery.QuantumMachinery.location;

import mrghastien.quantum_machinery.common.init.ModRecipeTypes;
import mrghastien.quantum_machinery.common.recipes.AlloySmeltingRecipe;
import mrghastien.quantum_machinery.common.recipes.ChippingRecipe;
import mrghastien.quantum_machinery.common.recipes.FurnaceRecipeWrapper;

public class ModRecipeType<T extends BaseRecipe> implements IRecipeType<T> {
	
	private static final List<ModRecipeType<? extends BaseRecipe>> types = new ArrayList<>();
	private static final List<ModRecipeType<? extends BaseRecipe>> wrappers = new ArrayList<>();
	
	public static final ModRecipeType<AlloySmeltingRecipe> ALLOY_SMELTING = register(ModRecipeTypes.ALLOY_SMELTING);
	public static final ModRecipeType<ChippingRecipe> CHIPPING = register(ModRecipeTypes.CHIPPING);
	
	public static final RecipeTypeWrapper<FurnaceRecipeWrapper, FurnaceRecipe> SMELTING_WRAPPER = register(IRecipeType.SMELTING, r -> new FurnaceRecipeWrapper(r.getId(), r));
	
	private static <T extends BaseRecipe> ModRecipeType<T> register(String name) {
		ModRecipeType<T> type = new ModRecipeType<>(name);
		types.add(type);
		return type;
	}
	
	/**Used to register vanilla recipe wrappers.
	 * 
	 * @param <T> The wrapper type
	 * @param <R> The base recipe type
	 * @param type The {@link IRecipeType} corresponding to the vanilla recipe
	 * @param func A function converting the vanilla recipe to a wrapper
	 */
	private static <T extends BaseRecipe, R extends IRecipe<IInventory>> RecipeTypeWrapper<T, R> register(IRecipeType<R> type, Function<R, T> func) {
		RecipeTypeWrapper<T, R> wrapper = new RecipeTypeWrapper<>(type, func);
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
            world = ServerLifecycleHooks.getCurrentServer().getWorld(World.field_234918_g_);
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
