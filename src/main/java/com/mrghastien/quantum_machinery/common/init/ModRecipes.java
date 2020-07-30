package com.mrghastien.quantum_machinery.common.init;

import com.mrghastien.quantum_machinery.common.recipes.alloy_smelting.AlloySmeltingRecipe;
import com.mrghastien.quantum_machinery.setup.RegistryHandler;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipes {

	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = RegistryHandler.create(ForgeRegistries.RECIPE_SERIALIZERS);
	
	public static final RegistryObject<AlloySmeltingRecipe.Serializer<?>> ALLOY_SMELTING_ZERIALIZER = RECIPE_SERIALIZERS.register(ModRecipeTypes.ALLOY_SMELTING, () -> new AlloySmeltingRecipe.Serializer<>(AlloySmeltingRecipe::new));
	
	public static void register() {
		RECIPE_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
