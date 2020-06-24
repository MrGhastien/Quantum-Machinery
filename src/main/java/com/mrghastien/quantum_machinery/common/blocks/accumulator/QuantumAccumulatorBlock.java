package com.mrghastien.quantum_machinery.common.blocks.accumulator;

import com.mrghastien.quantum_machinery.common.blocks.MachineBlock;
import com.mrghastien.quantum_machinery.common.init.ModTileEntities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class QuantumAccumulatorBlock extends MachineBlock {

	public QuantumAccumulatorBlock() {
		super(ModTileEntities.QUANTUM_ACCUMULATOR);
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new QuantumAccumulatorTile();
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.FACING);
	}
}
