package com.mrghastien.quantum_machinery.datagen.providers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.fml.RegistryObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.common.init.MetalType;
import com.mrghastien.quantum_machinery.common.init.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {

	private final List<RegistryObject<Block>> customItemModelBlocks = new ArrayList<>();
	
	public ModBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, QuantumMachinery.MODID, exFileHelper);
		customItemModelBlocks.add(ModBlocks.CABLE);
	}

	@Override	
	protected void registerStatesAndModels() {
		ResourceLocation topTexture = modLoc("block/blaster_top");
		ResourceLocation sideTexture = modLoc("block/blaster_side");
		directionalBlock(ModBlocks.QUANTUM_ACCUMULATOR.get(), models().cube("quantum_accumutator", topTexture, topTexture, modLoc("block/quantum_accumulator_front"), sideTexture, sideTexture, sideTexture));
		machineBlock(ModBlocks.ALLOY_SMELTER.get());
		machineBlock(ModBlocks.BLASTER.get());
		machineBlock(ModBlocks.ELECTRIC_FURNACE.get());
		
		Arrays.stream(MetalType.values()).forEach(t -> {
			t.getStorageBlock().ifPresent(this::simpleBlock);
			t.getOre().ifPresent(this::simpleBlock);
		});
		ModBlocks.getModBlocks().stream().filter(b -> !customItemModelBlocks.contains(b)).map(RegistryObject::get).forEach(b -> {
			itemModels().withExistingParent(b.getRegistryName().getPath(), modLoc("block/" + b.getRegistryName().getPath()));
		});
	}

	@Override
	public void directionalBlock(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset) {
		getVariantBuilder(block)
        .forAllStates(state -> {
            Direction dir = state.get(BlockStateProperties.FACING);
            return ConfiguredModel.builder()
                .modelFile(modelFunc.apply(state))
                .rotationX(dir == Direction.DOWN ? 90 : dir.getAxis().isHorizontal() ? 0 : -90)
                .rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.getHorizontalAngle()) + angleOffset) % 360)
                .build();
        });
	}
	
	private void machineBlock(Block block) {
		ResourceLocation topTexture = modLoc("block/blaster_top");
		ResourceLocation sideTexture = modLoc("block/blaster_side");
		ResourceLocation frontTexturePath = modLoc("block/" + block.getRegistryName().getPath() + "_front");
		ResourceLocation frontLitTexturePath = modLoc("block/" + block.getRegistryName().getPath() + "_front_lit");
		directionalBlock(block,
				state -> { boolean lit = state.get(BlockStateProperties.LIT);
					return models().cube(block.getRegistryName().getPath() + (lit ? "_lit" : ""), topTexture, topTexture,
						lit ? frontLitTexturePath : frontTexturePath,
						sideTexture, sideTexture, sideTexture);
		});
	}
	
}
