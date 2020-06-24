package com.mrghastien.quantum_machinery.common.init;

import static com.mrghastien.quantum_machinery.setup.ModSetup.MAIN_TAB;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.common.items.AntiMatterDustItem;
import com.mrghastien.quantum_machinery.common.items.AstroniumIngotItem;
import com.mrghastien.quantum_machinery.common.items.BatteryItem;
import com.mrghastien.quantum_machinery.common.items.WrenchItem;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
	
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<Item>(ForgeRegistries.ITEMS, QuantumMachinery.MODID);
	
	//Matériaux
	public static final RegistryObject<Item> ASTRONIUM_INGOT = ITEMS.register("astronium_ingot", () -> new AstroniumIngotItem(new Item.Properties().group(MAIN_TAB).rarity(Rarity.UNCOMMON))); 
	public static final RegistryObject<Item> ANTIMATTER_DUST = ITEMS.register("antimatter_dust", () -> new AntiMatterDustItem(new Item.Properties().group(MAIN_TAB).food(ModFoods.ASTRONIUM_FOOD).rarity(Rarity.UNCOMMON))); 
	public static final RegistryObject<Item> CATERIUM_INGOT = ITEMS.register("caterium_ingot", () -> new Item(ItemHelper.DEFAULT_PROPERTIES));
	public static final RegistryObject<Item> CATERIUM_NUGGET = ITEMS.register("caterium_nugget", () -> new Item(ItemHelper.DEFAULT_PROPERTIES)); 
	public static final RegistryObject<Item> RED_DIAMOND = ITEMS.register("red_diamond", () -> new Item(ItemHelper.DEFAULT_PROPERTIES));
	public static final RegistryObject<Item> SPACE_COMPOUND = ITEMS.register("space_compound", () -> new Item(ItemHelper.DEFAULT_PROPERTIES));
	public static final RegistryObject<Item> SCREW = ITEMS.register("screw", () -> new Item(ItemHelper.DEFAULT_PROPERTIES));
	public static final RegistryObject<Item> MACHINE_CASING = ITEMS.register("machine_casing", () -> new Item(ItemHelper.DEFAULT_PROPERTIES));
	public static final RegistryObject<Item> HCC_INGOT = ITEMS.register("hcc_ingot", () -> new Item(ItemHelper.DEFAULT_PROPERTIES.rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> HCC_NUGGET = ITEMS.register("hcc_nugget", () -> new Item(ItemHelper.DEFAULT_PROPERTIES.rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> DENSE_COMPOUND = ITEMS.register("dense_compound", () -> new Item(ItemHelper.DEFAULT_PROPERTIES));
	
	//Outils
	public static final RegistryObject<Item> ASTRONIUM_AXE = ITEMS.register("astronium_axe", () -> new AxeItem(ModToolMaterials.astronium, 4, -4 + 0.7f, ItemHelper.DEFAULT_PROPERTIES));
	public static final RegistryObject<Item> ASTRONIUM_HOE = ITEMS.register("astronium_hoe", () -> new HoeItem(ModToolMaterials.astronium, -4 + 2.4f, ItemHelper.DEFAULT_PROPERTIES));
	public static final RegistryObject<Item> ASTRONIUM_PICKAXE = ITEMS.register("astronium_pickaxe", () -> new PickaxeItem(ModToolMaterials.astronium, -8, -4 + 2.4f, ItemHelper.DEFAULT_PROPERTIES));
	public static final RegistryObject<Item> ASTRONIUM_SHOVEL = ITEMS.register("astronium_shovel", () -> new ShovelItem(ModToolMaterials.astronium, -8.0f, -4 + 2.4f, ItemHelper.DEFAULT_PROPERTIES));
	public static final RegistryObject<Item> ASTRONIUM_SWORD = ITEMS.register("astronium_sword", () -> new SwordItem(ModToolMaterials.astronium, 0, -4 + 2f, ItemHelper.DEFAULT_PROPERTIES));
	public static final RegistryObject<Item> WRENCH = ITEMS.register("wrench", WrenchItem::new);
	public static final RegistryObject<Item> BATTERY = ITEMS.register("battery", () -> new BatteryItem(ItemHelper.DEFAULT_PROPERTIES));
	
	//Armures
	public static final RegistryObject<Item> ASTRONIUM_HELMET = ITEMS.register("astronium_helmet", () -> new ArmorItem(ModArmorMaterials.astronium, EquipmentSlotType.HEAD, ItemHelper.DEFAULT_PROPERTIES));
	public static final RegistryObject<Item> ASTRONIUM_CHESTPLATE = ITEMS.register("astronium_chestplate", () -> new ArmorItem(ModArmorMaterials.astronium, EquipmentSlotType.CHEST, ItemHelper.DEFAULT_PROPERTIES));
	public static final RegistryObject<Item> ASTRONIUM_LEGGINGS = ITEMS.register("astronium_leggings", () -> new ArmorItem(ModArmorMaterials.astronium, EquipmentSlotType.LEGS, ItemHelper.DEFAULT_PROPERTIES));
	public static final RegistryObject<Item> ASTRONIUM_BOOTS = ITEMS.register("astronium_boots", () -> new ArmorItem(ModArmorMaterials.astronium, EquipmentSlotType.FEET, ItemHelper.DEFAULT_PROPERTIES));
	
	//public static final RegistryObject<Item> TEST_ENTITY_EGG = ITEMS.register("test_entity_egg", () -> ModEntities.createEntitySpawnEgg(ModEntities.TEST_ENTITY.get(), 0x000c63, 0x00ff88));
	//public static final RegistryObject<Item> GUARD_ENTITY_EGG = ITEMS.register("guard_entity_egg", () -> ModEntities.createEntitySpawnEgg(ModEntities.GUARD_ENTITY.get(), 0xff0c63, 0x000088));

	/*
	 * public static Item chipset;
	 * public static Item processor;
	 */
}
