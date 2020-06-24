package com.mrghastien.quantum_machinery.util.helpers;

import java.util.concurrent.atomic.AtomicInteger;

import com.mrghastien.quantum_machinery.common.capabilities.energy.ModEnergyStorage;

import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class BlockHelper {

	public static void sendOutPower(TileEntity tile) {
		LazyOptional<IEnergyStorage> e = tile.getCapability(CapabilityEnergy.ENERGY);
		World world = tile.getWorld();
		BlockPos pos = tile.getPos();
		e.ifPresent(energy -> {
			AtomicInteger capacity = new AtomicInteger(energy.getEnergyStored());
			if (capacity.get() > 0) {
				int i = 0;
				for (Direction direction : Direction.values()) {
					TileEntity tileentity = world.getTileEntity(pos.offset(direction));
					if (tileentity != null) {
						int space = tileentity.getCapability(CapabilityEnergy.ENERGY).map(handler -> {
							return handler.getMaxEnergyStored() - handler.getEnergyStored();
						}).orElse(0);
						if (space > 0)
							i++;

					}
				}
				int[] count = { i };
				for (Direction direction : Direction.values()) {
					TileEntity tileentity = world.getTileEntity(pos.offset(direction));
					if (tileentity != null && i > 0) {
						boolean doContinue = tileentity.getCapability(CapabilityEnergy.ENERGY, direction)
								.map(handler -> {
									if (handler.canReceive()) {
										int output = Math.min(capacity.get() / count[0], 1024 / count[0]);
										int recieved = handler.receiveEnergy(output, false);
										capacity.addAndGet(-recieved);
										((ModEnergyStorage) energy).extractEnergy(recieved);
										tile.markDirty();
										return capacity.get() > 0;
									} else {
										return true;
									}
								}).orElse(true);
						if (!doContinue)
							return;
					}
				}
			}
		});
	}

	/**
	 * Checks if the block at the specified position can handle energy through
	 * capabilities.
	 * 
	 * @param pos     the position of the block to test
	 * @param facing  the face of the block to test
	 * @param worldIn the world in which the block is
	 * 
	 * @return True if the block has the capability to handle energy and if it has a
	 *         tile entity, else returns false.
	 */
	public static boolean canBlockHandleEnergy(BlockPos pos, Direction facing, World world) {
		BlockState state = world.getBlockState(pos);
		return state.hasTileEntity()
				&& world.getTileEntity(pos).getCapability(CapabilityEnergy.ENERGY, facing).isPresent();
	}

}
