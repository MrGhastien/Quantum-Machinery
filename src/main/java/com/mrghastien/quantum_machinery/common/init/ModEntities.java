package com.mrghastien.quantum_machinery.common.init;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.common.entities.GuardEntity;
import com.mrghastien.quantum_machinery.common.entities.TestEntity;
import com.mrghastien.quantum_machinery.setup.ModSetup;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.biome.Biomes;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntities {
	
	public static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<EntityType<?>>(ForgeRegistries.ENTITIES, QuantumMachinery.MODID);
	
	public static final RegistryObject<EntityType<?>> TEST_ENTITY = ENTITIES.register("test_entity",
			() -> EntityType.Builder.create(TestEntity::new, EntityClassification.CREATURE)
					.build(QuantumMachinery.MODID + ":test_entity"));
	
	public static final RegistryObject<EntityType<?>> GUARD_ENTITY = ENTITIES.register("guard_entity",
			() -> EntityType.Builder.create(GuardEntity::new, EntityClassification.MISC)
					.build(QuantumMachinery.MODID + ":villager_guard"));
	
	public static void registerEntityWorldSpawns() {
		registerEntityWorldSpawn(TEST_ENTITY.get(), Biomes.PLAINS);
	}
	
	public static Item createEntitySpawnEgg(EntityType<?> type, int color1, int color2) {
		SpawnEggItem item = new SpawnEggItem(type, color1, color2, new Item.Properties().group(ModSetup.MAIN_TAB));
		return item;
	}
	
	public static void registerEntityWorldSpawn(EntityType<?> entity, Biome... biomes) {
			for(Biome biome : biomes) {
				if(biome != null) {
					biome.getSpawns(entity.getClassification()).add(new SpawnListEntry(entity, 10, 1, 10));
				}
			}
	}
}
