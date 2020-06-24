package com.mrghastien.quantum_machinery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mrghastien.quantum_machinery.common.events.TooltipRenderingEventHandler;
import com.mrghastien.quantum_machinery.common.init.ModBlocks;
import com.mrghastien.quantum_machinery.common.init.ModContainers;
import com.mrghastien.quantum_machinery.common.init.ModItems;
import com.mrghastien.quantum_machinery.common.init.ModRecipes;
import com.mrghastien.quantum_machinery.common.init.ModTileEntities;
import com.mrghastien.quantum_machinery.setup.ClientSetup;
import com.mrghastien.quantum_machinery.setup.ModSetup;
import com.mrghastien.quantum_machinery.util.proxy.ClientProxy;
import com.mrghastien.quantum_machinery.util.proxy.IProxy;
import com.mrghastien.quantum_machinery.util.proxy.ServerProxy;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * @author MrGhastien
 */

@Mod("quantum_machinery")
public class QuantumMachinery {
	
	public static QuantumMachinery instance;
	public static final String MODID = "quantum_machinery";
	public static final Logger LOGGER = LogManager.getLogger(MODID);
	
	public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());
	
	public QuantumMachinery() {
		instance = this;
		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(ModSetup::init);
		modEventBus.addListener(ClientSetup::init);
		
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(TooltipRenderingEventHandler.class);
		
		ModBlocks.BLOCKS.register(modEventBus);
		ModItems.ITEMS.register(modEventBus);
		ModContainers.CONTAINERS.register(modEventBus);
		ModTileEntities.TILES.register(modEventBus);
		ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
	}
	
	public static ResourceLocation location(String name) {
		return new ResourceLocation(MODID, name);
	}
}











































