package mrghastien.quantum_machinery.common.init;

import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;

public enum ModToolMaterials implements IItemTier {
	astronium(11.0f, 20.0f, 8000, 4, 25, ModItems.ASTRONIUM_INGOT.get());
	
	private float attackDamage, efficiency;
	private int durability, harvestLevel, enchantability;
	private Item repairMaterial;

	private ModToolMaterials(float attackDamage, float efficiency, int durability, int harvestLevel, int enchantability, Item repairMaterial) {
		this.attackDamage = attackDamage;
		this.durability = durability;
		this.efficiency = efficiency;
		this.enchantability = enchantability;
		this.harvestLevel = harvestLevel;
		this.repairMaterial = repairMaterial;
	}

	@Override
	public float getAttackDamage() {
		return this.attackDamage;
	}

	@Override
	public float getEfficiency() {
		return this.efficiency;
	}

	@Override
	public int getEnchantability() {
		return this.enchantability;
	}

	@Override
	public int getHarvestLevel() {
		return this.harvestLevel;
	}

	@Override
	public int getMaxUses() {
		return this.durability;
	}

	@Override
	public Ingredient getRepairMaterial() {
		return Ingredient.fromItems(this.repairMaterial);
	}
}
