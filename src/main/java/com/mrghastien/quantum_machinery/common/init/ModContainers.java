package com.mrghastien.quantum_machinery.common.init;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.common.blocks.accumulator.QuantumAccumulatorContainer;
import com.mrghastien.quantum_machinery.common.blocks.blaster.BlasterContainer;
import com.mrghastien.quantum_machinery.common.blocks.furnace.ElectricFurnaceContainer;
import com.mrghastien.quantum_machinery.common.blocks.refinery.RefineryContainer;
import com.mrghastien.quantum_machinery.common.multiblocks.fission.containers.FissionContainer;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
	
	public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<ContainerType<?>>(ForgeRegistries.CONTAINERS, QuantumMachinery.MODID);
	
	public static final RegistryObject<ContainerType<BlasterContainer>> BLASTER_CONTAINER = CONTAINERS.register("blaster", () -> IForgeContainerType.create((id, inv, data) -> {
		BlockPos pos = data.readBlockPos();
		return new BlasterContainer(id, QuantumMachinery.proxy.getClientWorld(), pos, inv,
				QuantumMachinery.proxy.getClientPlayer());
	}));
	
	public static final RegistryObject<ContainerType<QuantumAccumulatorContainer>> QUANTUM_ACCUMULATOR_CONTAINER = CONTAINERS.register("quantum_accumulator", () -> IForgeContainerType.create((id, inv, data) -> {
		BlockPos pos = data.readBlockPos();
		return new QuantumAccumulatorContainer(id, QuantumMachinery.proxy.getClientWorld(), pos, inv,
				QuantumMachinery.proxy.getClientPlayer());
	}));
	
	public static final RegistryObject<ContainerType<RefineryContainer>> REFINERY_CONTAINER = CONTAINERS.register("refinery", () -> IForgeContainerType.create((id, inv, data) -> {
		BlockPos pos = data.readBlockPos();
		return new RefineryContainer(id, QuantumMachinery.proxy.getClientWorld(), pos, inv,
				QuantumMachinery.proxy.getClientPlayer());
	}));
	
	public static final RegistryObject<ContainerType<ElectricFurnaceContainer>> ELECTRIC_FURNACE_CONTAINER = CONTAINERS.register("electric_furnace", () -> IForgeContainerType.create((id, inv, data) -> {
		BlockPos pos = data.readBlockPos();
		return new ElectricFurnaceContainer(id, QuantumMachinery.proxy.getClientWorld(), pos, inv,
				QuantumMachinery.proxy.getClientPlayer());
	}));
	
	public static final RegistryObject<ContainerType<FissionContainer>> FISSION_CONTROLLER_CONTAINER = CONTAINERS.register("fission_controller", () -> IForgeContainerType.create((id, inv, data) -> {
		BlockPos pos = data.readBlockPos();
		return new FissionContainer(id, QuantumMachinery.proxy.getClientWorld(), pos, inv,
				QuantumMachinery.proxy.getClientPlayer());
	})); 
}
