package com.mrghastien.quantum_machinery.common.init;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.common.recipes.refinery.RefineryRecipe;
import com.mrghastien.quantum_machinery.common.recipes.refinery.RefineryRecipeSerializer;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipes {

	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = new DeferredRegister<IRecipeSerializer<?>>(ForgeRegistries.RECIPE_SERIALIZERS, QuantumMachinery.MODID);
	
	public static final RegistryObject<IRecipeSerializer<?>> REFINING_SERIALIZER = RECIPE_SERIALIZERS.register("refining", RefineryRecipeSerializer::new);

	public static final IRecipeType<RefineryRecipe> REFINING_TYPE = new IRecipeType<RefineryRecipe>() {};
}
