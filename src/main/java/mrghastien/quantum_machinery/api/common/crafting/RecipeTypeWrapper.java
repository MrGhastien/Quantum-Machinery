package mrghastien.quantum_machinery.api.common.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

public class RecipeTypeWrapper<T extends BaseRecipe, R extends IRecipe<IInventory>> extends ModRecipeType<T> {
	
	private IRecipeType<R> base;
	private Function<R, T> wrappedFunction;
	
	public RecipeTypeWrapper(IRecipeType<R> base, Function<R, T> wrappedFunction) {
		super(base.toString());
		this.base = base;
		this.wrappedFunction = wrappedFunction;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Map<ResourceLocation, T> getRecipes(World world) {
        if (world == null) {
            world = ServerLifecycleHooks.getCurrentServer().getWorld(World.field_234918_g_); // World.field_234918_g_ = World.OVERWORLD
            if (world == null) return Collections.emptyMap();
        }

        if (cache.isEmpty()) {
            RecipeManager recipeManager = world.getRecipeManager();
            Collection<?> recipes = recipeManager.getRecipes();
            recipes.forEach(r -> {
            	IRecipe<?> recipe = (IRecipe<?>) r;
            	if(recipe.getType() == base) {
            		cache.put(recipe.getId(), wrappedFunction.apply((R) recipe));
            	}
            });
        }
        return cache;
    }
}