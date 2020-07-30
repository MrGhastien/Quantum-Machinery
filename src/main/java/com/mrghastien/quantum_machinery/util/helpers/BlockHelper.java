package com.mrghastien.quantum_machinery.util.helpers;

import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.List;

import com.mrghastien.quantum_machinery.common.blocks.MachineBaseTile;
import com.mrghastien.quantum_machinery.common.capabilities.energy.MachineEnergyStorage;

public final class BlockHelper {

	public static Block.Properties defaultProperties() { return Properties.create(Material.IRON).sound(SoundType.STONE).hardnessAndResistance(5.0f); }
	
	public static void sendOutPower(MachineBaseTile tile, int toSend) {
		if(!tile.getEnergyStorage().canExtract())
			return;
		if(toSend == 0)
			return;
		
		List<Direction> validDirections = new ArrayList<>();
		MachineEnergyStorage storage = tile.getEnergyStorage();
		int left = toSend;
		World world = tile.getWorld();
		for (Direction dir : Direction.values()) {
			BlockPos offsetPos = tile.getPos().offset(dir);
			if (BlockHelper.canBlockHandleEnergy(offsetPos, dir.getOpposite(), world)) {
				TileEntity tileEntity = world.getTileEntity(offsetPos);
				boolean canRecieve = tileEntity.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite())
						.orElseThrow(
								() -> new IllegalStateException("Failed power sending : Tile Entity does not exist !"))
						.canReceive();
				if (canRecieve) {
					validDirections.add(dir);
				}
			}
		}
		
		if(validDirections.isEmpty())
			return;
		
		int perSide = left / validDirections.size();
		for (Direction dir : validDirections) {
			BlockPos offsetPos = tile.getPos().offset(dir);
			TileEntity tileEntity = world.getTileEntity(offsetPos);
			IEnergyStorage otherStorage = tileEntity.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite())
					.orElseThrow(() -> new IllegalStateException(
							"Failed power sending : Tile Entity does not have Energy Capability !"));
			left -= otherStorage.receiveEnergy(perSide, false);
		}
		storage.extractEnergy(toSend - left);
	}
	
	public static void sendOutPower(MachineBaseTile tile) {
		sendOutPower(tile, tile.getEnergyStorage().getEnergyStored());
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
	
	public static IEnergyStorage getEnergyStorage(BlockPos pos, Direction facing, World world) {
		TileEntity tile = world.getTileEntity(pos);
		return tile == null ? null : tile.getCapability(CapabilityEnergy.ENERGY, facing.getOpposite()).orElse(null);
	}

}
