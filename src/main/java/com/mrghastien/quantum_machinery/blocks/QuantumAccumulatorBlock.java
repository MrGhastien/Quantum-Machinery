package com.mrghastien.quantum_machinery.blocks;

import com.mrghastien.quantum_machinery.init.ModStateProperties;
import com.mrghastien.quantum_machinery.tileentities.QuantumAccumulatorTile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class QuantumAccumulatorBlock extends MachineBlock {

	public QuantumAccumulatorBlock() {
		super();
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
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.FACING, 
				ModStateProperties.NORTH, 
				ModStateProperties.SOUTH, 
				ModStateProperties.EAST, 
				ModStateProperties.WEST,
				ModStateProperties.UP,
				ModStateProperties.DOWN);
	}
}
