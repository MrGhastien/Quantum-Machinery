package com.mrghastien.quantum_machinery.common.init;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import com.mrghastien.quantum_machinery.common.recipes.AlloySmeltingRecipe;
import com.mrghastien.quantum_machinery.common.recipes.ChippingRecipe;
import com.mrghastien.quantum_machinery.setup.RegistryHandler;

public class ModRecipes {

	public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = RegistryHandler.create(ForgeRegistries.RECIPE_SERIALIZERS);
	
	public static final RegistryObject<AlloySmeltingRecipe.Serializer> ALLOY_SMELTING = SERIALIZERS.register(ModRecipeTypes.ALLOY_SMELTING, AlloySmeltingRecipe.Serializer::new);
	public static final RegistryObject<ChippingRecipe.Serializer> CHIPPING = SERIALIZERS.register("chipping", ChippingRecipe.Serializer::new);
	
	public static void register() {
		SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
