package com.mrghastien.quantum_machinery.common.blocks.machines.alloy_smelter;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import com.mrghastien.quantum_machinery.common.blocks.machines.MachineBlock;
import com.mrghastien.quantum_machinery.common.init.ModTileEntities;
import com.mrghastien.quantum_machinery.util.BlockHelper;

public class AlloySmelterBlock extends MachineBlock {
	
	public AlloySmelterBlock() {
		super(ModTileEntities.ALLOY_SMELTER, BlockHelper.defaultProperties().lightValue(15));
		this.setDefaultState(this.stateContainer.getBaseState().with(BlockStateProperties.FACING, Direction.NORTH)
				.with(BlockStateProperties.LIT, false)
		);
	}
	
	@Override
	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
		return state.get(BlockStateProperties.LIT) ? super.getLightValue(state, world, pos) : 0;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

}

