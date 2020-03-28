package com.mrghastien.quantum_machinery.init;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.multiblocks.fission.tileentities.FissionControllerTile;
import com.mrghastien.quantum_machinery.multiblocks.fission.tileentities.FissionEOTile;
import com.mrghastien.quantum_machinery.multiblocks.fission.tileentities.FissionPartTile;
import com.mrghastien.quantum_machinery.tileentities.BlasterTile;
import com.mrghastien.quantum_machinery.tileentities.CableTile;
import com.mrghastien.quantum_machinery.tileentities.ConnectorTile;
import com.mrghastien.quantum_machinery.tileentities.ElectricFurnaceTile;
import com.mrghastien.quantum_machinery.tileentities.QuantumAccumulatorTile;
import com.mrghastien.quantum_machinery.tileentities.RefineryTile;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {
	
	public static final DeferredRegister<TileEntityType<?>> TILES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, QuantumMachinery.MODID);
	
	public static final RegistryObject<TileEntityType<?>> BLASTER_TILE = TILES.register("blaster_tile",
			() -> TileEntityType.Builder.create(BlasterTile::new, ModBlocks.BLASTER.get()).build(null));
	
	public static final RegistryObject<TileEntityType<?>> QUANTUM_ACCUMULATOR_TILE = TILES.register("quantum_accumulator_tile",
			() -> TileEntityType.Builder.create(QuantumAccumulatorTile::new, ModBlocks.QUANTUM_ACCUMULATOR.get()).build(null));
	
	public static final RegistryObject<TileEntityType<?>> REFINERY_TILE = TILES.register("refinery_tile",
			() -> TileEntityType.Builder.create(RefineryTile::new, ModBlocks.REFINERY.get()).build(null));
	
	public static final RegistryObject<TileEntityType<?>> CABLE_TILE = TILES.register("cable_tile",
			() -> TileEntityType.Builder.create(CableTile::new, ModBlocks.CABLE.get()).build(null));
	
	public static final RegistryObject<TileEntityType<?>> CONNECTOR_TILE = TILES.register("connector_tile",
			() -> TileEntityType.Builder.create(ConnectorTile::new, ModBlocks.CONNECTOR.get()).build(null));
	
	public static final RegistryObject<TileEntityType<?>> ELECTRIC_FURNACE_TILE = TILES.register("electric_furnace_tile",
			() -> TileEntityType.Builder.create(ElectricFurnaceTile::new, ModBlocks.ELECTRIC_FURNACE.get()).build(null));
	//Fission TileEntities
	public static final RegistryObject<TileEntityType<?>> FISSION_CONTROLLER_TILE = TILES.register("fission_controller_tile",
			() -> TileEntityType.Builder.create(FissionControllerTile::new, ModBlocks.FISSION_CONTROLLER.get()).build(null));
	
	public static final RegistryObject<TileEntityType<?>> FISSION_OUTLET_TILE = TILES.register("fission_outlet_tile",
			() -> TileEntityType.Builder.create(FissionEOTile::new, ModBlocks.FISSION_ENERGY_OUTLET.get()).build(null));
	
	public static final RegistryObject<TileEntityType<?>> FISSION_PART_TILE = TILES.register("fission_part_tile",
			() -> TileEntityType.Builder.create(FissionPartTile::new, ModBlocks.FISSION_ROD_CONTROLLER.get(), 
					ModBlocks.FISSION_CASING.get(), ModBlocks.FISSION_GLASS.get(), ModBlocks.FISSION_FUEL_ROD.get(), ModBlocks.MAGNETIC_MODULE.get()).build(null));
}
