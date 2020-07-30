package com.mrghastien.quantum_machinery.common.init;

import static com.mrghastien.quantum_machinery.setup.Setup.MAIN_TAB;

import java.util.Collection;

import com.mrghastien.quantum_machinery.common.items.AntiMatterDustItem;
import com.mrghastien.quantum_machinery.common.items.AstroniumIngotItem;
import com.mrghastien.quantum_machinery.common.items.BatteryItem;
import com.mrghastien.quantum_machinery.common.items.WrenchItem;
import com.mrghastien.quantum_machinery.setup.RegistryHandler;
import com.mrghastien.quantum_machinery.util.helpers.ItemHelper;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.Rarity;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
	
	public static final DeferredRegister<Item> ITEMS = RegistryHandler.create(ForgeRegistries.ITEMS);
	
	//Matériaux
	public static final RegistryObject<Item> ASTRONIUM_INGOT = ITEMS.register("astronium_ingot", () -> new AstroniumIngotItem(new Item.Properties().group(MAIN_TAB).rarity(Rarity.UNCOMMON))); 
	public static final RegistryObject<Item> ANTIMATTER_DUST = ITEMS.register("antimatter_dust", () -> new AntiMatterDustItem(new Item.Properties().group(MAIN_TAB).food(ModFoods.ASTRONIUM_FOOD).rarity(Rarity.UNCOMMON))); 
	public static final RegistryObject<Item> CATERIUM_INGOT = base("caterium_ingot");
	public static final RegistryObject<Item> CATERIUM_NUGGET = base("caterium_nugget"); 
	public static final RegistryObject<Item> MACHINE_CASING = base("machine_casing");
	public static final RegistryObject<Item> COAL_DUST = base("coal_dust");
	
	//Outils
	public static final RegistryObject<Item> ASTRONIUM_AXE = ITEMS.register("astronium_axe", () -> new AxeItem(ModToolMaterials.astronium, 4, -4 + 0.7f, ItemHelper.defaultProperties()));
	public static final RegistryObject<Item> ASTRONIUM_HOE = ITEMS.register("astronium_hoe", () -> new HoeItem(ModToolMaterials.astronium, -4 + 2.4f, ItemHelper.defaultProperties()));
	public static final RegistryObject<Item> ASTRONIUM_PICKAXE = ITEMS.register("astronium_pickaxe", () -> new PickaxeItem(ModToolMaterials.astronium, -8, -4 + 2.4f, ItemHelper.defaultProperties()));
	public static final RegistryObject<Item> ASTRONIUM_SHOVEL = ITEMS.register("astronium_shovel", () -> new ShovelItem(ModToolMaterials.astronium, -8.0f, -4 + 2.4f, ItemHelper.defaultProperties()));
	public static final RegistryObject<Item> ASTRONIUM_SWORD = ITEMS.register("astronium_sword", () -> new SwordItem(ModToolMaterials.astronium, 0, -4 + 2f, ItemHelper.defaultProperties()));
	public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", WrenchItem::new);
	public static final RegistryObject<Item> BATTERY = ITEMS.register("battery", () -> new BatteryItem(ItemHelper.defaultProperties()));
	
	//Armures
	public static final RegistryObject<Item> ASTRONIUM_HELMET = ITEMS.register("astronium_helmet", () -> new ArmorItem(ModArmorMaterials.astronium, EquipmentSlotType.HEAD, ItemHelper.defaultProperties()));
	public static final RegistryObject<Item> ASTRONIUM_CHESTPLATE = ITEMS.register("astronium_chestplate", () -> new ArmorItem(ModArmorMaterials.astronium, EquipmentSlotType.CHEST, ItemHelper.defaultProperties()));
	public static final RegistryObject<Item> ASTRONIUM_LEGGINGS = ITEMS.register("astronium_leggings", () -> new ArmorItem(ModArmorMaterials.astronium, EquipmentSlotType.LEGS, ItemHelper.defaultProperties()));
	public static final RegistryObject<Item> ASTRONIUM_BOOTS = ITEMS.register("astronium_boots", () -> new ArmorItem(ModArmorMaterials.astronium, EquipmentSlotType.FEET, ItemHelper.defaultProperties()));
	//public static final RegistryObject<Item> TEST_ENTITY_EGG = ITEMS.register("test_entity_egg", () -> ModEntities.createEntitySpawnEgg(ModEntities.TEST_ENTITY.get(), 0x000c63, 0x00ff88));
	//public static final RegistryObject<Item> GUARD_ENTITY_EGG = ITEMS.register("guard_entity_egg", () -> ModEntities.createEntitySpawnEgg(ModEntities.GUARD_ENTITY.get(), 0xff0c63, 0x000088));
	
	private static RegistryObject<Item> base(String name) {
		return ITEMS.register(name, () -> new Item(ItemHelper.defaultProperties()));
	}
	
	/**
	 * @return A list containing all the blocks in the mod.
	 */
	public static Collection<RegistryObject<Item>> getModItems() {
		return ITEMS.getEntries();
	}
	
	/** Adds all DeferredRegisters to the ModEventBus.
	 * 
	 * @param bus The ModEventBus
	 */
	public static final void register() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		MetalType.registerItems();
	}
}
