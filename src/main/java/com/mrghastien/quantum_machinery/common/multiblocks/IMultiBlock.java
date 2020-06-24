package com.mrghastien.quantum_machinery.common.multiblocks;

import java.util.Set;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This interface is used to create MultiBlock structures.
 * 
 * <p>It contains all the methods required to create and manage multiblocks.
 */
public interface IMultiBlock {
	
	Set<BlockPos> isValidUnformedMultiBlock(Direction facing, World world, BlockPos pos);
	
	Set<BlockPos> isValidFormedMultiBlock(Direction facing, World world, BlockPos pos);
	
	BlockPos getControllerRelativePos(Direction facing);
	
	MultiBlockStruct getStruct(Direction facing);

	default int getWidth() {
		return getStruct(Direction.NORTH).getWidth();
	}

	default int getHeight() {
		return getStruct(Direction.NORTH).getHeight();
	}

	default int getDepth() {
		return getStruct(Direction.NORTH).getDepth();
	}
	
	BlockPos getOrigin(World world, BlockPos pos);
}
