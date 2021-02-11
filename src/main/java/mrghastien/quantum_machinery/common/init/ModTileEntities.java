package mrghastien.quantum_machinery.common.init;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;

import mrghastien.quantum_machinery.common.blocks.cable.CableTile;
import mrghastien.quantum_machinery.common.blocks.generators.blaster.BlasterTile;
import mrghastien.quantum_machinery.common.blocks.machines.accumulator.QuantumAccumulatorTile;
import mrghastien.quantum_machinery.common.blocks.machines.alloy_smelter.AlloySmelterTile;
import mrghastien.quantum_machinery.common.blocks.machines.chipper.WoodChipperTile;
import mrghastien.quantum_machinery.common.blocks.machines.furnace.ElectricFurnaceTile;
import mrghastien.quantum_machinery.common.multiblocks.fission.tileentities.FissionControllerTile;
import mrghastien.quantum_machinery.common.multiblocks.fission.tileentities.FissionEOTile;
import mrghastien.quantum_machinery.common.multiblocks.fission.tileentities.FissionPartTile;
import mrghastien.quantum_machinery.setup.RegistryHandler;

public class ModTileEntities {
	
	public static final DeferredRegister<TileEntityType<?>> TILES = RegistryHandler.create(ForgeRegistries.TILE_ENTITIES);
	
	public static final RegistryObject<TileEntityType<?>> BLASTER = TILES.register("blaster_tile",
			() -> TileEntityType.Builder.create(BlasterTile::new, ModBlocks.BLASTER.get()).build(null));
	
	public static final RegistryObject<TileEntityType<?>> QUANTUM_ACCUMULATOR = TILES.register("quantum_accumulator_tile",
			() -> TileEntityType.Builder.create(QuantumAccumulatorTile::new, ModBlocks.QUANTUM_ACCUMULATOR.get()).build(null));
	
	public static final RegistryObject<TileEntityType<?>> ALLOY_SMELTER = TILES.register("alloy_smelter_tile",
			() -> TileEntityType.Builder.create(AlloySmelterTile::new, ModBlocks.ALLOY_SMELTER.get()).build(null));
	
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
	
	public static final RegistryObject<TileEntityType<?>> WOOD_CHIPPER = TILES.register("wood_chipper_tile",
			() -> TileEntityType.Builder.create(WoodChipperTile::new, ModBlocks.WOOD_CHIPPER.get()).build(null));
	
	/**
	 * @return A list containing all the tile entities in the mod.
	 */
	public static Collection<RegistryObject<TileEntityType<?>>> getModTiles() {
		return TILES.getEntries();
	}
	
	/** Adds all DeferredRegisters to the ModEventBus.
	 * 
	 * @param bus The ModEventBus
	 */
	public static final void register() {
		TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
