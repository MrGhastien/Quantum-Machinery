package com.mrghastien.quantum_machinery.common.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IConnectable {
	
	/**
	 * Checks if the block has any connections.
	 * 
	 * @param pos the position of the block
	 * @param world the world in which the block is
	 * 
	 * @return True if there is any connection, else returns false.
	 */
	boolean hasConnections(BlockPos pos, World world);
	
	/**
	 * Checks if there is a connection in the specified direction.
	 * 
	 * @param pos the position of the block
	 * @param facing the direction of the connection
	 * @param world the world in which the block is
	 * 
	 * @return True if there is a connection in the specified direction, else returns false.
	 */
	boolean isConnected(Direction facing, BlockPos pos, World world);
	
	/**
	 * Tries to connect a block extending this interface with the block next to it, in a specified direction.
	 * 
	 * @param requestingPos the position from the block requesting a connection
	 * @param requestingState TODO
	 * @param pos the position of the block to connect
	 * @param state TODO
	 * @param facing the direction of the connection
	 * @param worldIn the world in which the block is
	 * @return A blockstate with the connection in the specified direction
	 */
	BlockState tryConnect(BlockPos requestingPos, BlockState requestingState, BlockPos pos, BlockState state, Direction facing, World worldIn);
	
	/**
	 * Tries to disconnect a block extending this interface with the block next to it, in a specified direction.
	 * 
	 * @param requestingPos the position from the block requesting a connection
	 * @param requestingState TODO
	 * @param pos the position of the block to connect
	 * @param state TODO
	 * @param facing the direction of the connection
	 * @param worldIn the world in which the block is
	 * @return A blockstate with the connection in the specified direction
	 */
	BlockState tryDisconnect(BlockPos requestingPos, BlockState requestingState, BlockPos pos, BlockState state, Direction facing, World worldIn);
	
	/**
	 * Refreshes the connections of the block for all the directions.
	 * 
	 * @param pos the position of the block
	 * @param facingState TODO
	 * @param worldIn the world in which the block is
	 * @return A blockstate with the new connections
	 */
	BlockState refreshConnections(BlockPos pos, World worldIn);
	
}
