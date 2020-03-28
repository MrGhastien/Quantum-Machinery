package com.mrghastien.quantum_machinery.setup;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.init.ModItems;
import com.mrghastien.quantum_machinery.network.ModNetworking;
import com.mrghastien.quantum_machinery.world.oregen.OreGeneration;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = QuantumMachinery.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {
	
	public static final ItemGroup MAIN_TAB = new ItemGroup("main_tab") {
		
		@OnlyIn(Dist.CLIENT)
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModItems.ANTIMATTER_DUST.get());
		}
	};
	
	public final ItemGroup tools = new ItemGroup("machines") {
		
		@OnlyIn(Dist.CLIENT)
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ModItems.WRENCH.get());
		}
	};
	
	public static void init(final FMLCommonSetupEvent event) {
		ModNetworking.registerNetworkPackets();
		OreGeneration.setupOreGeneration();
		QuantumMachinery.LOGGER.info("Main Setup executed");
	}

}
