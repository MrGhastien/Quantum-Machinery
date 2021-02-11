package mrghastien.quantum_machinery.datagen.providers;

import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static mrghastien.quantum_machinery.datagen.builders.recipe.AlloySmeltingRecipeBuilder.alloySmeltingRecipe;

import mrghastien.quantum_machinery.QuantumMachinery;
import mrghastien.quantum_machinery.common.init.MetalType;
import mrghastien.quantum_machinery.common.init.ModBlocks;
import mrghastien.quantum_machinery.common.init.ModItems;
import mrghastien.quantum_machinery.datagen.CriterionHelper;

public class ModRecipeProvider extends RecipeProvider {
 
	public ModRecipeProvider(DataGenerator generatorIn) {
		super(generatorIn);
	}
	
	@Override
	public String getName() {
		return "Recipes: " + QuantumMachinery.MODID;
	}

	@Override
	protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
		//Shaped
		shapedRecipe(ModItems.MACHINE_CASING.get()).patternLine("III").patternLine("I I").patternLine("SSS").key('I', Items.IRON_INGOT).key('S', Items.SMOOTH_STONE).build(consumer);
		shapedRecipe(ModBlocks.CABLE.get()).patternLine("III").patternLine("RRR").patternLine("III").key('I', Items.IRON_INGOT).key('R', Items.REDSTONE).build(consumer);
		shapedRecipe(ModItems.SCREW.get(), 8).patternLine("I").patternLine("I").key('I', Items.IRON_INGOT).build(consumer);
		shapedRecipe(ModBlocks.ALLOY_SMELTER.get()).patternLine("STS").patternLine("FMF").patternLine("STS")
				.key('S', ModItems.SCREW.get()).key('F', Blocks.FURNACE).key('M', ModItems.MACHINE_CASING.get())
				.key('T', Items.SMOOTH_STONE).build(consumer);
		shapedRecipe(ModBlocks.BLASTER.get()).patternLine("SFS").patternLine("YMY").patternLine("STS")
				.key('S', ModItems.SCREW.get()).key('F', Blocks.FURNACE).key('M', ModItems.MACHINE_CASING.get())
				.key('T', Items.SMOOTH_STONE).key('Y', MetalType.STEEL.getIngot().get()).build(consumer);
		shapedRecipe(ModBlocks.QUANTUM_ACCUMULATOR.get()).patternLine("SRS").patternLine("YMY").patternLine("STS")
				.key('S', ModItems.SCREW.get()).key('R', Blocks.REDSTONE_BLOCK).key('M', ModItems.MACHINE_CASING.get())
				.key('T', Items.SMOOTH_STONE).key('Y', MetalType.STEEL.getIngot().get()).build(consumer);
		
		shapedRecipe(ModItems.WRENCH.get()).patternLine(" S").patternLine("WI").patternLine(" I")
				.key('S', MetalType.STEEL.getIngotTag().get()).key('W', ModItems.SCREW.get()).key('I', Tags.Items.INGOTS_IRON).build(consumer);
		shapedRecipe(ModBlocks.ELECTRIC_FURNACE.get()).patternLine("SFS").patternLine("TMT").patternLine("SHS").key('S', ModItems.SCREW.get()).key('F', Blocks.FURNACE).key('T', MetalType.STEEL.getIngotTag().get())
				.key('M', ModItems.MACHINE_CASING.get()).key('H', Blocks.SMOOTH_STONE).build(consumer);
		
		//Alloy smelting
		alloySmeltingRecipe(MetalType.STEEL.getIngot().get(), 1, 120)
				.ingredient(Items.IRON_INGOT, 1)
				.ingredient(Items.COAL, 1)
				.build(consumer);
		alloySmeltingRecipe(ModItems.ASTRONIUM_INGOT.get(), 1, 480)
				.ingredient(ModItems.ANTIMATTER_DUST.get(), 4)
				.ingredient(MetalType.STEEL.getIngotTag().get(), 2)
				.build(consumer);
		alloySmeltingRecipe(ModItems.ANTIMATTER_DUST.get(), 1, 480)
				.ingredient(Items.GLOWSTONE_DUST, 4)
				.ingredient(Items.DRAGON_BREATH, 1)
				.ingredient(Items.REDSTONE, 1)
				.build(consumer);
	}
	
	private static ShapedRecipeBuilder shapedRecipe(IItemProvider result) {
		return shapedRecipe(result, 1);
	}
	
	private static ShapedRecipeBuilder shapedRecipe(IItemProvider result, int count) {
		return ShapedRecipeBuilder.shapedRecipe(result, count).addCriterion(result.asItem().getRegistryName().getPath(), CriterionHelper.hasItem(result));
	}
}