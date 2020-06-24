package com.mrghastien.quantum_machinery.common.init;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.common.blocks.accumulator.QuantumAccumulatorTile;
import com.mrghastien.quantum_machinery.common.blocks.blaster.BlasterTile;
import com.mrghastien.quantum_machinery.common.blocks.cable.CableTile;
import com.mrghastien.quantum_machinery.common.blocks.furnace.ElectricFurnaceTile;
import com.mrghastien.quantum_machinery.common.blocks.refinery.RefineryTile;
import com.mrghastien.quantum_machinery.common.multiblocks.fission.tileentities.FissionControllerTile;
import com.mrghastien.quantum_machinery.common.multiblocks.fission.tileentities.FissionEOTile;
import com.mrghastien.quantum_machinery.common.multiblocks.fission.tileentities.FissionPartTile;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
	
	public static final DeferredRegister<TileEntityType<?>> TILES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, QuantumMachinery.MODID);
	
	public static final RegistryObject<TileEntityType<?>> BLASTER = TILES.register("blaster_tile",
			() -> TileEntityType.Builder.create(BlasterTile::new, ModBlocks.BLASTER.get()).build(null));
	
	public static final RegistryObject<TileEntityType<?>> QUANTUM_ACCUMULATOR = TILES.register("quantum_accumulator_tile",
			() -> TileEntityType.Builder.create(QuantumAccumulatorTile::new, ModBlocks.QUANTUM_ACCUMULATOR.get()).build(null));
	
	public static final RegistryObject<TileEntityType<?>> REFINERY = TILES.register("refinery_tile",
			() -> TileEntityType.Builder.create(RefineryTile::new, ModBlocks.REFINERY.get()).build(null));
	
	public static final RegistryObject<TileEntityType<?>> CABLE = TILES.register("cable_tile",
			() -> TileEntityType.Builder.create(CableTile::new, ModBlocks.CABLE.get()).build(null));
	
	public static final RegistryObject<TileEntityType<?>> ELECTRIC_FURNACE = TILES.register("electric_furnace_tile",
			() -> TileEntityType.Builder.create(ElectricFurnaceTile::new, ModBlocks.ELECTRIC_FURNACE.get()).build(null));
	//Fission TileEntities
	public static final RegistryObject<TileEntityType<?>> FISSION_CONTROLLER = TILES.register("fission_controller_tile",
			() -> TileEntityType.Builder.create(FissionControllerTile::new, ModBlocks.FISSION_CONTROLLER.get()).build(null));
	
	public static final RegistryObject<TileEntityType<?>> FISSION_OUTLET= TILES.register("fission_outlet_tile",
			() -> TileEntityType.Builder.create(FissionEOTile::new, ModBlocks.FISSION_ENERGY_OUTLET.get()).build(null));
	
	public static final RegistryObject<TileEntityType<?>> FISSION_PART = TILES.register("fission_part_tile",
			() -> TileEntityType.Builder.create(FissionPartTile::new, ModBlocks.FISSION_ROD_CONTROLLER.get(), 
					ModBlocks.FISSION_CASING.get(), ModBlocks.FISSION_GLASS.get(), ModBlocks.FISSION_FUEL_ROD.get(), ModBlocks.MAGNETIC_MODULE.get()).build(null));
}
