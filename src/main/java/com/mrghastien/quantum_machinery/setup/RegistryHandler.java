package com.mrghastien.quantum_machinery.setup;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.api.common.crafting.ItemStackIngredient;
import com.mrghastien.quantum_machinery.api.common.crafting.ModRecipeType;
import com.mrghastien.quantum_machinery.common.init.ModBlocks;
import com.mrghastien.quantum_machinery.common.init.ModContainers;
import com.mrghastien.quantum_machinery.common.init.ModItems;
import com.mrghastien.quantum_machinery.common.init.ModRecipes;
import com.mrghastien.quantum_machinery.common.init.ModTileEntities;
import com.mrghastien.quantum_machinery.common.multiblocks.MultiBlockHandler;
import com.mrghastien.quantum_machinery.common.multiblocks.fission.FissionMultiBlock;

/**A class handling registration
 * <p> Handles BlockItems registration automatically
 * @author MrGhastien
 */
@Mod.EventBusSubscriber(modid = QuantumMachinery.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class RegistryHandler {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static final <T extends IForgeRegistryEntry<T>> DeferredRegister<T> create(IForgeRegistry<T> reg) {
		return DeferredRegister.create(reg, QuantumMachinery.MODID);
	}
	
	public static void registerAll() {
		ModBlocks.register();
		ModItems.register();
		ModContainers.register();
		ModTileEntities.register();
		ModRecipes.register();
	}
	
	@SubscribeEvent
	public static void registerItems(final RegistryEvent.Register<Item> event) {
		final IForgeRegistry<Item> registry = event.getRegistry();
		//Enregistrement et création d'un Item de Bloc pour chaque bloc déjà enregistré
		ModBlocks.getModBlocks().stream()
				.map(RegistryObject::get)
				.forEach(block -> {
					final Item.Properties properties = new Item.Properties().group(Setup.MAIN_TAB);
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
	public static void registerRecipeSerializers(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
		//Recipe Serializers
		ModRecipeType.registerTypes(event.getRegistry());
		CraftingHelper.register(ItemStackIngredient.Serializer.ID, ItemStackIngredient.Serializer.INSTANCE);
		LOGGER.info("Recipe serializers registered");
	}
	
	public static ResourceLocation location(String name) {
		return new ResourceLocation(QuantumMachinery.MODID, name);
	}


}
