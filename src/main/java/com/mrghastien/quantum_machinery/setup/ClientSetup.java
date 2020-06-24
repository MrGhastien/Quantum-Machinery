package com.mrghastien.quantum_machinery.setup;

import static com.mrghastien.quantum_machinery.common.init.ModContainers.BLASTER_CONTAINER;
import static com.mrghastien.quantum_machinery.common.init.ModContainers.ELECTRIC_FURNACE_CONTAINER;
import static com.mrghastien.quantum_machinery.common.init.ModContainers.QUANTUM_ACCUMULATOR_CONTAINER;
import static com.mrghastien.quantum_machinery.common.init.ModContainers.REFINERY_CONTAINER;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.client.multiblocks.fission.screens.FissionScreen;
import com.mrghastien.quantum_machinery.client.screens.BlasterScreen;
import com.mrghastien.quantum_machinery.client.screens.ElectricFurnaceScreen;
import com.mrghastien.quantum_machinery.client.screens.QuantumAccumulatorScreen;
import com.mrghastien.quantum_machinery.client.screens.RefineryScreen;
import com.mrghastien.quantum_machinery.common.blocks.accumulator.QuantumAccumulatorContainer;
import com.mrghastien.quantum_machinery.common.blocks.accumulator.QuantumAccumulatorTile;
import com.mrghastien.quantum_machinery.common.blocks.blaster.BlasterContainer;
import com.mrghastien.quantum_machinery.common.blocks.blaster.BlasterTile;
import com.mrghastien.quantum_machinery.common.blocks.furnace.ElectricFurnaceContainer;
import com.mrghastien.quantum_machinery.common.blocks.furnace.ElectricFurnaceTile;
import com.mrghastien.quantum_machinery.common.blocks.refinery.RefineryContainer;
import com.mrghastien.quantum_machinery.common.blocks.refinery.RefineryTile;
import com.mrghastien.quantum_machinery.common.init.ModContainers;
import com.mrghastien.quantum_machinery.common.multiblocks.fission.containers.FissionContainer;
import com.mrghastien.quantum_machinery.common.multiblocks.fission.tileentities.FissionControllerTile;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = QuantumMachinery.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientSetup {

	public static void init(final FMLClientSetupEvent event) {
		ScreenManager.IScreenFactory<BlasterContainer, BlasterScreen> blasterFactory = (container, inventory, title) -> {
			BlasterTile tileEntity = (BlasterTile) QuantumMachinery.proxy.getClientWorld().getTileEntity(container.getPos());
			return new BlasterScreen(container, inventory, title, tileEntity);
		};
		ScreenManager.registerFactory(BLASTER_CONTAINER.get(), blasterFactory);
		
		ScreenManager.IScreenFactory<QuantumAccumulatorContainer, QuantumAccumulatorScreen> accumulatorFactory = (container, inventory, title) -> {
			QuantumAccumulatorTile tileEntity = (QuantumAccumulatorTile) QuantumMachinery.proxy.getClientWorld().getTileEntity(container.getPos());
			return new QuantumAccumulatorScreen(container, inventory, title, tileEntity);
		};
		ScreenManager.registerFactory(QUANTUM_ACCUMULATOR_CONTAINER.get(), accumulatorFactory);
		
		ScreenManager.IScreenFactory<RefineryContainer, RefineryScreen> refineryFactory = (container, inventory, title) -> {
			RefineryTile tileEntity = (RefineryTile) QuantumMachinery.proxy.getClientWorld().getTileEntity(container.getPos());
			return new RefineryScreen(container, inventory, title, tileEntity);
		};
		ScreenManager.registerFactory(REFINERY_CONTAINER.get(), refineryFactory);
		
		ScreenManager.IScreenFactory<ElectricFurnaceContainer, ElectricFurnaceScreen> furnaceFactory = (container, inventory, title) -> {
			ElectricFurnaceTile tileEntity = (ElectricFurnaceTile) QuantumMachinery.proxy.getClientWorld().getTileEntity(container.getPos());
			return new ElectricFurnaceScreen(container, inventory, title, tileEntity);
		};
		ScreenManager.registerFactory(ELECTRIC_FURNACE_CONTAINER.get(), furnaceFactory);
		
		ScreenManager.IScreenFactory<FissionContainer, FissionScreen> fissionFactory = (container, inventory, title) -> {
			FissionControllerTile tileEntity = (FissionControllerTile) QuantumMachinery.proxy.getClientWorld().getTileEntity(container.getPos());
			return new FissionScreen(container, inventory, title, tileEntity);
		};
		ScreenManager.registerFactory(ModContainers.FISSION_CONTROLLER_CONTAINER.get(), fissionFactory);
		//ModRenderRegistry.registryEntityRender();
		QuantumMachinery.LOGGER.info("Client Setup executed");
	}
	
}
