package mrghastien.quantum_machinery.datagen.providers;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelProvider;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mrghastien.quantum_machinery.QuantumMachinery;
import mrghastien.quantum_machinery.common.init.MetalType;
import mrghastien.quantum_machinery.common.init.ModItems;

public class ModItemModelProvider extends ItemModelProvider {

	private static final Logger LOGGER = LogManager.getLogger();
	
	public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, QuantumMachinery.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		Arrays.stream(MetalType.values()).forEach(metal -> {
			metal.getDust().ifPresent(i -> {
				 baseItem(i, i.getRegistryName().getPath());
			});
			metal.getIngot().ifPresent(i -> {
				 baseItem(i, i.getRegistryName().getPath());
			});
			metal.getNugget().ifPresent(i -> {
				 baseItem(i, i.getRegistryName().getPath());
			});
			metal.getShred().ifPresent(i -> {
				 baseItem(i, i.getRegistryName().getPath());
			});
		});
		baseItem(ModItems.ANTIMATTER_DUST.get());
		baseItem(ModItems.ASTRONIUM_AXE.get());
		baseItem(ModItems.ASTRONIUM_BOOTS.get());
		baseItem(ModItems.ASTRONIUM_CHESTPLATE.get());
		baseItem(ModItems.ASTRONIUM_HELMET.get());
		baseItem(ModItems.ASTRONIUM_HOE.get());
		baseItem(ModItems.ASTRONIUM_INGOT.get());
		baseItem(ModItems.ASTRONIUM_LEGGINGS.get());
		baseItem(ModItems.ASTRONIUM_PICKAXE.get());
		baseItem(ModItems.ASTRONIUM_SHOVEL.get());
		baseItem(ModItems.ASTRONIUM_SWORD.get());
		baseItem(ModItems.BATTERY.get());
		baseItem(ModItems.COAL_DUST.get());
	}
	
	private void baseItem(Item item) {
		baseItem(item, item.getRegistryName());
	}
	
	private void baseItem(Item item, String texture) {
		baseItem(item.getRegistryName().getPath(), modLoc(texture));
	}
	
	private void baseItem(Item item, ResourceLocation texture) {
		baseItem(item.getRegistryName().getPath(), texture);
	}
	
	private void baseItem(String name, ResourceLocation texture) {
		ResourceLocation loc = itemLocation(texture);
		if(textureExists(loc)) {
			singleTexture(name, mcLoc("item/generated"), "layer0", loc);
			//LOGGER.debug("Generated base Item : " + "item/" + name);
		} else {
			LOGGER.warn("Could not find texture " + loc);
		}
	}
	
	private ResourceLocation itemLocation(ResourceLocation loc) {
		return new ResourceLocation(loc.getNamespace(), ITEM_FOLDER + "/" + loc.getPath());
	}
	
	private ResourceLocation blockLocation(ResourceLocation loc) {
		return new ResourceLocation(loc.getNamespace(), BLOCK_FOLDER + "/" + loc.getPath());
	}
	
	private boolean textureExists(ResourceLocation loc) {
		return existingFileHelper.exists(loc, ResourcePackType.CLIENT_RESOURCES, ".png", "textures");
	}

	private boolean modelExists(ResourceLocation loc) {
		return existingFileHelper.exists(loc, ResourcePackType.CLIENT_RESOURCES, ".json", "models");
	}
}
