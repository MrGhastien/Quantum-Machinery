package mrghastien.quantum_machinery.common.init;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;

import mrghastien.quantum_machinery.common.blocks.generators.blaster.BlasterContainer;
import mrghastien.quantum_machinery.common.blocks.machines.accumulator.QuantumAccumulatorContainer;
import mrghastien.quantum_machinery.common.blocks.machines.alloy_smelter.AlloySmelterContainer;
import mrghastien.quantum_machinery.common.blocks.machines.chipper.WoodChipperContainer;
import mrghastien.quantum_machinery.common.blocks.machines.furnace.ElectricFurnaceContainer;
import mrghastien.quantum_machinery.common.multiblocks.fission.containers.FissionContainer;
import mrghastien.quantum_machinery.setup.RegistryHandler;

public class ModContainers {
	
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = RegistryHandler.create(ForgeRegistries.CONTAINERS);
	
	public static final RegistryObject<ContainerType<BlasterContainer>> BLASTER_CONTAINER = CONTAINERS.register("blaster", () -> IForgeContainerType.create((id, inv, data) -> {
		BlockPos pos = data.readBlockPos();
		return new BlasterContainer(id, inv.player.world, pos, inv);
	}));
	
	public static final RegistryObject<ContainerType<QuantumAccumulatorContainer>> QUANTUM_ACCUMULATOR_CONTAINER = CONTAINERS.register("quantum_accumulator", () -> IForgeContainerType.create((id, inv, data) -> {
		BlockPos pos = data.readBlockPos();
		return new QuantumAccumulatorContainer(id, inv.player.world, pos, inv);
	}));
	
	public static final RegistryObject<ContainerType<AlloySmelterContainer>> ALLOY_SMELTER_CONTAINER = CONTAINERS.register("alloy_smelter", () -> IForgeContainerType.create((id, inv, data) -> {
		BlockPos pos = data.readBlockPos();
		return new AlloySmelterContainer(id, inv.player.world, pos, inv);
	}));
	
	public static final RegistryObject<ContainerType<ElectricFurnaceContainer>> ELECTRIC_FURNACE_CONTAINER = CONTAINERS.register("electric_furnace", () -> IForgeContainerType.create((id, inv, data) -> {
		BlockPos pos = data.readBlockPos();
		return new ElectricFurnaceContainer(id, inv.player.world, pos, inv);
	}));
	
	public static final RegistryObject<ContainerType<FissionContainer>> FISSION_CONTROLLER_CONTAINER = CONTAINERS.register("fission_controller", () -> IForgeContainerType.create((id, inv, data) -> {
		BlockPos pos = data.readBlockPos();
		return new FissionContainer(id, inv.player.world, pos, inv);
	}));
	
	public static final RegistryObject<ContainerType<WoodChipperContainer>> WOOD_CHIPPER = CONTAINERS
			.register("wood_chipper", () -> IForgeContainerType.create(
					(id, inv, data) -> new WoodChipperContainer(id, inv.player.world, data.readBlockPos(), inv)));
	
	/**
	 * @return A list containing all the blocks in the mod.
	 */
	public static Collection<RegistryObject<ContainerType<?>>> getModContainers() {
		return CONTAINERS.getEntries();
	}
	
	/** Adds all DeferredRegisters to the ModEventBus.
	 * 
	 * @param bus The ModEventBus
	 */
	public static final void register() {
		CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
