package com.mrghastien.quantum_machinery.common.init;

import com.mrghastien.quantum_machinery.QuantumMachinery;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public enum ModArmorMaterials implements IArmorMaterial{
	astronium("astronium", 120, new int[]{5, 11, 14, 7}, 20, ModItems.ASTRONIUM_INGOT.get(), SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 6.0f);
	
	private static final int[] max_damage_array = new int[] {26, 30, 32, 22};
	private String name;
	private int durability, enchantability;
	private Item repairMaterial;
	private int[] defense;
	private float toughness;
	private SoundEvent equipSound;
	
	private ModArmorMaterials(String nameIn, int durabilityIn, int[] defenseIn, int enchantabilityIn, Item repairMaterialIn, SoundEvent equipSoundIn, float toughnessIn) {
		this.name = nameIn;
		this.durability = durabilityIn;
		this.enchantability = enchantabilityIn;
		this.defense = defenseIn;
		this.repairMaterial = repairMaterialIn;
		this.toughness = toughnessIn;
		this.equipSound = equipSoundIn;
	}

	@Override
	public int getDurability(EquipmentSlotType slotIn) {
		return max_damage_array[slotIn.getIndex()] * this.durability;
	}

	@Override
	public int getDamageReductionAmount(EquipmentSlotType slotIn) {
		return this.defense[slotIn.getIndex()];
	}

	@Override
	public int getEnchantability() {
		return this.enchantability;
	}

	@Override
	public SoundEvent getSoundEvent() {
		return this.equipSound;
	}

	@Override
	public Ingredient getRepairMaterial() {
		return Ingredient.fromItems(this.repairMaterial);
	}

	@Override
	public String getName() {
		return QuantumMachinery.MODID + ":" + this.name;
	}

	@Override
	public float getToughness() {
		return this.toughness;
	}

}
