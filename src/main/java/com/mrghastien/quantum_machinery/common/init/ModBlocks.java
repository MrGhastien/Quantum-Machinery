package com.mrghastien.quantum_machinery.common.init;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.common.blocks.accumulator.QuantumAccumulatorBlock;
import com.mrghastien.quantum_machinery.common.blocks.blaster.BlasterBlock;
import com.mrghastien.quantum_machinery.common.blocks.cable.CableBlock;
import com.mrghastien.quantum_machinery.common.blocks.furnace.ElectricFurnaceBlock;
import com.mrghastien.quantum_machinery.common.blocks.refinery.RefineryBlock;
import com.mrghastien.quantum_machinery.common.multiblocks.fission.blocks.FissionControllerBlock;
import com.mrghastien.quantum_machinery.common.multiblocks.fission.blocks.FissionPartBlock;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
	
	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, QuantumMachinery.MODID);

	public static final RegistryObject<Block> ASTRONIUM_BLOCK = BLOCKS.register("astronium_block", () -> new Block(
			Block.Properties.create(Material.IRON).hardnessAndResistance(5, 20).sound(SoundType.METAL)));
	
	public static final RegistryObject<Block> CATERIUM_ORE = BLOCKS.register("caterium_ore",() -> new Block(
			Block.Properties.create(Material.ROCK).hardnessAndResistance(7, 40).sound(SoundType.STONE)
					.harvestLevel(4).harvestTool(ToolType.PICKAXE)));
	
	public static final RegistryObject<Block> BLASTER = BLOCKS.register("blaster", BlasterBlock::new);
	public static final RegistryObject<Block> QUANTUM_ACCUMULATOR = BLOCKS.register("quantum_accumulator", QuantumAccumulatorBlock::new);
	public static final RegistryObject<Block> REFINERY = BLOCKS.register("refinery", RefineryBlock::new);
	public static final RegistryObject<Block> CABLE = BLOCKS.register("cable", () -> new CableBlock(1024));
	public static final RegistryObject<Block> ELECTRIC_FURNACE = BLOCKS.register("electric_furnace", ElectricFurnaceBlock::new);
	
	//Fission multiblock blocks
	public static final RegistryObject<Block> FISSION_CONTROLLER = BLOCKS.register("fission_controller", FissionControllerBlock::new);
	public static final RegistryObject<Block> FISSION_CASING = BLOCKS.register("fission_casing", FissionPartBlock::new);
	public static final RegistryObject<Block> FISSION_FUEL_ROD = BLOCKS.register("fission_fuel_rod", FissionPartBlock::new);
	public static final RegistryObject<Block> FISSION_ENERGY_OUTLET = BLOCKS.register("fission_energy_outlet", FissionPartBlock::new);
	public static final RegistryObject<Block> FISSION_ROD_CONTROLLER = BLOCKS.register("fission_rod_controller", FissionPartBlock::new);
	public static final RegistryObject<Block> FISSION_GLASS = BLOCKS.register("fission_glass", FissionPartBlock::new);
	public static final RegistryObject<Block> MAGNETIC_MODULE = BLOCKS.register("magnetic_module", FissionPartBlock::new);
}