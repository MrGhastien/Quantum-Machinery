package com.mrghastien.quantum_machinery.common.blocks.machines.accumulator;

import static com.mrghastien.quantum_machinery.common.init.ModContainers.QUANTUM_ACCUMULATOR_CONTAINER;

import com.mrghastien.quantum_machinery.common.blocks.MachineBaseContainer;
import com.mrghastien.quantum_machinery.common.capabilities.energy.EnergyBar;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class QuantumAccumulatorContainer extends MachineBaseContainer<QuantumAccumulatorTile>{
	
	public final static int SIZE = 0;
	
	public QuantumAccumulatorContainer(int id, World world, BlockPos pos, PlayerInventory playerinventory) {
		super(QUANTUM_ACCUMULATOR_CONTAINER.get(), id, world, pos, playerinventory, (QuantumAccumulatorTile) world.getTileEntity(pos), SIZE);
		addEnergyBar(new EnergyBar(() -> tileEntity.getEnergyStorage(), 80, 18, 16, 114));
		layoutPlayerInventorySlots(8, 150);
	} 
}
