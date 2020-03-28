package com.mrghastien.quantum_machinery.util.helpers;

import com.mrghastien.quantum_machinery.blocks.IConnectable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;

public class BlockHelper {

	/**
	 * Requests a connection between the block at the specified position and the block next to it, depending on the facing.
	 * 
	 * @param pos the position of the block requesting a connection
	 * @param facing the direction of the connection the other block could have
	 * @param worldIn the world in which the block is
	 * 
	 * @return True if success.
	 */
	public static boolean requestConnection(Direction facing, BlockPos pos, World worldIn) {
		BlockPos facingPos = pos.offset(facing.getOpposite());
		Block block = worldIn.getBlockState(facingPos).getBlock();
		if (block instanceof IConnectable) {
			return worldIn.setBlockState(facingPos, ((IConnectable)block).tryConnect(pos, worldIn.getBlockState(pos), facingPos, worldIn.getBlockState(facingPos), facing, worldIn));
		} else {
			return false;
		}
	}
	
	/**
	 * Requests a connection refresh between the block at the specified position and the block next to it, depending on the facing.
	 * 
	 * @param pos the position of the block requesting a connection
	 * @param facing the direction of the connection the other block could have
	 * @param worldIn the world in which the block is
	 * 
	 * @return True if success.
	 */
	public static boolean requestRefresh(Direction facing, BlockPos pos, World worldIn) {
		BlockPos facingPos = pos.offset(facing.getOpposite());
		Block block = worldIn.getBlockState(facingPos).getBlock();
		if (block instanceof IConnectable) {
			return worldIn.setBlockState(facingPos, ((IConnectable)block).refreshConnections(facingPos, worldIn));
		} else {
			return false;
		}
	}

	/**
	 * Requests a deconnection between the block at the specified position and the block next to it, depending on the facing.
	 * 
	 * @param pos the position of the block requesting a connection
	 * @param facing the direction of the connection the other block could have
	 * @param worldIn the world in which the block is
	 * 
	 * @return True if success.
	 */
	public static boolean requestDeconnection(Direction facing, BlockPos pos, World world) {
		BlockPos facingPos = pos.offset(facing.getOpposite());
		Block block = world.getBlockState(facingPos).getBlock();
		if (block instanceof IConnectable) {
			return world.setBlockState(facingPos, ((IConnectable)block).tryDisconnect(pos, world.getBlockState(pos), facingPos, world.getBlockState(facingPos), facing, world));
		} else {
			return false;
		}
	}

	/**
	 * Requests a connection between the block at the specified position and all blocks around it.
	 * 
	 * @param pos the position of the block requesting a connection
	 * @param worldIn the world in which the block is
	 */
	public static void requestAllSidesConnect(BlockPos pos, World worldIn) {
		for (Direction direction : Direction.values()) {
			requestConnection(direction, pos, worldIn);
		}
	}

	/**
	 * Requests a deconnection between the block at the specified position and all blocks around it.
	 * 
	 * @param pos the position of the block requesting a connection
	 * @param worldIn the world in which the block is
	 */
	public static void requestAllSidesDisconnect(BlockPos pos, World world) {
		for (Direction direction : Direction.values()) {
			requestDeconnection(direction, pos, world);
		}
	}
	
	/**
	 * Requests a connection refresh between the block at the specified position and all blocks around it.
	 * 
	 * @param pos the position of the block requesting a connection
	 * @param worldIn the world in which the block is
	 */
	public static void requestAllSidesRefresh(BlockPos pos, World worldIn) {
		for (Direction direction : Direction.values()) {
			requestRefresh(direction, pos, worldIn);
		}
	}

	/**
	 * Checks if the block at the specified position has a connection for the specified direction.
	 * 
	 * @param pos the position of the block to test
	 * @param facing the direction of the connection to test
	 * @param worldIn the world in which the block is
	 * 
	 * @return True if the block has a connection, else returns false.
	 */
	public static boolean hasBlockConnection(Direction facing, BlockPos pos, World world) {
		BlockState state = world.getBlockState(pos);
		if (state.getBlock() instanceof IConnectable) {
			return ((IConnectable)state.getBlock()).isConnected(facing, pos, world);
		}
		return false;
	}
	
	/**
	 * Checks if the block at the specified position can handle energy through capabilities.
	 * 
	 * @param pos the position of the block to test
	 * @param facing the face of the block to test
	 * @param worldIn the world in which the block is
	 * 
	 * @return True if the block has the capability to handle energy and if it has a tile entity, else returns false.
	 */
	public static boolean canBlockHandleEnergy(BlockPos pos, Direction facing, World world) {
		BlockState state = world.getBlockState(pos);
		return state.hasTileEntity() && world.getTileEntity(pos).getCapability(CapabilityEnergy.ENERGY, facing).isPresent();
	}
	
}
