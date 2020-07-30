package com.mrghastien.quantum_machinery.common.blocks.machines.furnace;

import com.mrghastien.quantum_machinery.common.blocks.machines.MachineBlock;
import com.mrghastien.quantum_machinery.common.init.ModTileEntities;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class ElectricFurnaceBlock extends MachineBlock {

	public ElectricFurnaceBlock() {
		super(ModTileEntities.ELECTRIC_FURNACE, Properties.create(Material.IRON)
				.sound(SoundType.METAL)
				.hardnessAndResistance(5.0f)
				.lightValue(15)
		);
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
