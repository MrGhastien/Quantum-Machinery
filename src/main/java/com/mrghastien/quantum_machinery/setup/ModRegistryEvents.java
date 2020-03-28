package com.mrghastien.quantum_machinery.setup;

import org.apache.logging.log4j.Logger;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.init.ModBlocks;
import com.mrghastien.quantum_machinery.init.ModRecipes;
import com.mrghastien.quantum_machinery.multiblocks.MultiBlockHandler;
import com.mrghastien.quantum_machinery.multiblocks.fission.FissionMultiBlock;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod.EventBusSubscriber(modid = QuantumMachinery.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistryEvents {
	private static final String MODID = QuantumMachinery.MODID;
	private static final Logger LOGGER = QuantumMachinery.LOGGER;

	private static final ItemGroup MAIN_TAB = ModSetup.MAIN_TAB;

	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		//Enregistrement et création d'un Item de Bloc pour chaque bloc déjà enregistré
		ModBlocks.BLOCKS.getEntries().stream()
				.map(RegistryObject::get)
				.forEach(block -> {
					final Item.Properties properties = new Item.Properties().group(MAIN_TAB);
					final BlockItem blockItem = new BlockItem(block, properties);
					blockItem.setRegistryName(block.getRegistryName());
					registry.register(blockItem);
				});
		LOGGER.info("Items registered.");
	}

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {
		MultiBlockHandler.registerMultiblock(FissionMultiBlock.INSTANCE);
		LOGGER.info("Blocks registered.");
	}

	/*@SubscribeEvent
	public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event) {
		event.getRegistry().registerAll(
				// Entités
				ModEntities.TEST_ENTITY, ModEntities.GUARD_ENTITY);
		LOGGER.info("Entities registered");
	}
	*/

	@SubscribeEvent
	public static void registerDataSerializers(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
		//Recipe Serializers
		Registry.register(Registry.RECIPE_TYPE, ModRecipes.REFINING_SERIALIZER.getId(), ModRecipes.REFINING_TYPE);
		
		LOGGER.info("Data serializers registered");
	}
	
	public static ResourceLocation location(String name) {
		return new ResourceLocation(MODID, name);
	}
	
	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final String name) {
		return setup(entry, new ResourceLocation(QuantumMachinery.MODID, name));
	}

	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final ResourceLocation registryName) {
		entry.setRegistryName(registryName);
		return entry;
	}

}
