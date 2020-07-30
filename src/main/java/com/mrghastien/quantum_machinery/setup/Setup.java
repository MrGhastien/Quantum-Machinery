package com.mrghastien.quantum_machinery.setup;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mrghastien.quantum_machinery.client.screens.AlloySmelterScreen;
import com.mrghastien.quantum_machinery.client.screens.BlasterScreen;
import com.mrghastien.quantum_machinery.client.screens.ElectricFurnaceScreen;
import com.mrghastien.quantum_machinery.client.screens.FissionScreen;
import com.mrghastien.quantum_machinery.client.screens.QuantumAccumulatorScreen;
import com.mrghastien.quantum_machinery.common.blocks.generators.blaster.BlasterContainer;
import com.mrghastien.quantum_machinery.common.blocks.generators.blaster.BlasterTile;
import com.mrghastien.quantum_machinery.common.blocks.machines.accumulator.QuantumAccumulatorContainer;
import com.mrghastien.quantum_machinery.common.blocks.machines.accumulator.QuantumAccumulatorTile;
import com.mrghastien.quantum_machinery.common.blocks.machines.alloy_smelter.AlloySmelterContainer;
import com.mrghastien.quantum_machinery.common.blocks.machines.alloy_smelter.AlloySmelterTile;
import com.mrghastien.quantum_machinery.common.blocks.machines.furnace.ElectricFurnaceContainer;
import com.mrghastien.quantum_machinery.common.blocks.machines.furnace.ElectricFurnaceTile;
import com.mrghastien.quantum_machinery.common.init.ModContainers;
import com.mrghastien.quantum_machinery.common.init.ModItems;
import com.mrghastien.quantum_machinery.common.multiblocks.MultiBlockHandler;
import com.mrghastien.quantum_machinery.common.multiblocks.fission.containers.FissionContainer;
import com.mrghastien.quantum_machinery.common.multiblocks.fission.tileentities.FissionControllerTile;
import com.mrghastien.quantum_machinery.common.network.ModNetworking;
import com.mrghastien.quantum_machinery.common.world.oregen.OreGeneration;

import static com.mrghastien.quantum_machinery.common.init.ModContainers.ALLOY_SMELTER_CONTAINER;
import static com.mrghastien.quantum_machinery.common.init.ModContainers.BLASTER_CONTAINER;
import static com.mrghastien.quantum_machinery.common.init.ModContainers.ELECTRIC_FURNACE_CONTAINER;
import static com.mrghastien.quantum_machinery.common.init.ModContainers.QUANTUM_ACCUMULATOR_CONTAINER;

public class Setup {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static void init() {
		RegistryHandler.registerAll();
		Client.init();
		Server.init();
		final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		//Subscribing events
		bus.addListener(Setup::setup);
	}
	
	public static final ItemGroup MAIN_TAB = new ItemGroup("main_tab") {
		
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModItems.ANTIMATTER_DUST.get());
		}
	};
	
	public static final ItemGroup TOOLS = new ItemGroup("machines") {
		
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModItems.WRENCH.get());
		}
	};
	
	public static void setup(final FMLCommonSetupEvent event) {
		ModNetworking.registerNetworkPackets();
		OreGeneration.setupOreGeneration();
		MultiBlockHandler.setup();
		LOGGER.info("Main Setup executed");
	}
	
	public static class Client {
		
		public static void init() {
			FMLJavaModLoadingContext.get().getModEventBus().addListener(Client::clientSetup);
		}
		
		private static void clientSetup(final FMLClientSetupEvent event) {
			Minecraft instance = Minecraft.getInstance();
			ScreenManager.IScreenFactory<BlasterContainer, BlasterScreen> blasterFactory = (container, inventory, title) -> {
				BlasterTile tileEntity = (BlasterTile) instance.world.getTileEntity(container.getPos());
				return new BlasterScreen(container, inventory, title, tileEntity);
			};
			ScreenManager.registerFactory(BLASTER_CONTAINER.get(), blasterFactory);
			
			ScreenManager.IScreenFactory<QuantumAccumulatorContainer, QuantumAccumulatorScreen> accumulatorFactory = (container, inventory, title) -> {
				QuantumAccumulatorTile tileEntity = (QuantumAccumulatorTile) instance.world.getTileEntity(container.getPos());
				return new QuantumAccumulatorScreen(container, inventory, title, tileEntity);
			};
			ScreenManager.registerFactory(QUANTUM_ACCUMULATOR_CONTAINER.get(), accumulatorFactory);
			
			ScreenManager.IScreenFactory<AlloySmelterContainer, AlloySmelterScreen> refineryFactory = (container, inventory, title) -> {
				AlloySmelterTile tileEntity = (AlloySmelterTile) instance.world.getTileEntity(container.getPos());
				return new AlloySmelterScreen(container, inventory, title, tileEntity);
			};
			ScreenManager.registerFactory(ALLOY_SMELTER_CONTAINER.get(), refineryFactory);
			
			ScreenManager.IScreenFactory<ElectricFurnaceContainer, ElectricFurnaceScreen> furnaceFactory = (container, inventory, title) -> {
				ElectricFurnaceTile tileEntity = (ElectricFurnaceTile) instance.world.getTileEntity(container.getPos());
				return new ElectricFurnaceScreen(container, inventory, title, tileEntity);
			};
			ScreenManager.registerFactory(ELECTRIC_FURNACE_CONTAINER.get(), furnaceFactory);
			
			ScreenManager.IScreenFactory<FissionContainer, FissionScreen> fissionFactory = (container, inventory, title) -> {
				FissionControllerTile tileEntity = (FissionControllerTile) instance.world.getTileEntity(container.getPos());
				return new FissionScreen(container, inventory, title, tileEntity);
			};
			ScreenManager.registerFactory(ModContainers.FISSION_CONTROLLER_CONTAINER.get(), fissionFactory);
			//ModRenderRegistry.registryEntityRender();
			LOGGER.info("Client Setup executed");
		}	
	}
	
	public static class Server {
		
		public static void init() {
			FMLJavaModLoadingContext.get().getModEventBus().addListener(Server::serverSetup);
		}
		
		private static void serverSetup(final FMLDedicatedServerSetupEvent event) {
			LOGGER.info("Server Setup executed");
		}
	}
}
