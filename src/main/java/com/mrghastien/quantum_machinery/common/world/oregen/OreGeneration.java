package com.mrghastien.quantum_machinery.common.world.oregen;

import com.mrghastien.quantum_machinery.common.init.ModBlocks;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig.FillerBlockType;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class OreGeneration {
	
	public static void setupOreGeneration() {
		for(Biome biome : ForgeRegistries.BIOMES) {
			CountRangeConfig polzrite_ore_placement = new CountRangeConfig(20, 20, 0, 64);
			biome.addFeature(Decoration.UNDERGROUND_ORES,
					Feature.ORE
							.withConfiguration(new OreFeatureConfig(FillerBlockType.NATURAL_STONE,
									ModBlocks.CATERIUM_ORE.get().getDefaultState(), 5))
							.withPlacement(Placement.COUNT_RANGE.configure(polzrite_ore_placement)));
		}
	}
}
