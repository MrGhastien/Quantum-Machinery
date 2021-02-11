package mrghastien.quantum_machinery.common.blocks.machines.accumulator;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;

import static mrghastien.quantum_machinery.common.init.ModTileEntities.QUANTUM_ACCUMULATOR;

import mrghastien.quantum_machinery.common.blocks.BaseTile;

public class QuantumAccumulatorTile extends BaseTile {
		
	public QuantumAccumulatorTile() {
		super(QUANTUM_ACCUMULATOR.get(), 1000000, 1024);
	}
	
	@Override
	public Container createMenu(int id, PlayerInventory playerinventory, PlayerEntity player) {
		return new QuantumAccumulatorContainer(id, world, pos, playerinventory);
	}
}
