package com.mrghastien.quantum_machinery.common.blocks.accumulator;

import static com.mrghastien.quantum_machinery.common.init.ModContainers.QUANTUM_ACCUMULATOR_CONTAINER;

import com.mrghastien.quantum_machinery.common.blocks.MachineContainer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class QuantumAccumulatorContainer extends MachineContainer<QuantumAccumulatorTile>{
	
	public final static int SIZE = 0;
	
	public QuantumAccumulatorContainer(int id, World world, BlockPos pos, PlayerInventory playerinventory, PlayerEntity player) {
		super(QUANTUM_ACCUMULATOR_CONTAINER.get(), id, world, pos, playerinventory, player, (QuantumAccumulatorTile) world.getTileEntity(pos), SIZE);
		
		layoutPlayerInventorySlots(8, 150);
	} 
}
