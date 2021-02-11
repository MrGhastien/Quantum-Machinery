package mrghastien.quantum_machinery.common.init;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import mrghastien.quantum_machinery.QuantumMachinery;
import mrghastien.quantum_machinery.common.entities.GuardEntity;
import mrghastien.quantum_machinery.common.entities.TestEntity;
import mrghastien.quantum_machinery.setup.RegistryHandler;
import mrghastien.quantum_machinery.setup.Setup;

public class ModEntities {
	
	public static final DeferredRegister<EntityType<?>> ENTITIES = RegistryHandler.create(ForgeRegistries.ENTITIES);
	
	public static final RegistryObject<EntityType<TestEntity>> TEST_ENTITY = ENTITIES.register("test_entity",
			() -> EntityType.Builder.create(TestEntity::new, EntityClassification.CREATURE)
					.build(QuantumMachinery.MODID + ":test_entity"));
	
	public static final RegistryObject<EntityType<GuardEntity>> GUARD_ENTITY = ENTITIES.register("guard_entity",
			() -> EntityType.Builder.create(GuardEntity::new, EntityClassification.MISC)
					.build(QuantumMachinery.MODID + ":villager_guard"));
	
	public static void registerEntityWorldSpawns() {
		//registerEntityWorldSpawn(TEST_ENTITY.get(), ForgeRegistries.);
	}
	
	public static Item createEntitySpawnEgg(EntityType<?> type, int color1, int color2) {
		SpawnEggItem item = new SpawnEggItem(type, color1, color2, new Item.Properties().group(Setup.MAIN_TAB));
		return item;
	}
	
	public static void registerEntityWorldSpawn(EntityType<?> entity, Biome... biomes) {
			for(Biome biome : biomes) {
				if(biome != null) {
					biome.func_242433_b().func_242559_a(entity.getClassification()).add(new MobSpawnInfo.Spawners(entity, 10, 1, 10));
				}
			}
	}
}
