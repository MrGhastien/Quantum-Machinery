package com.mrghastien.quantum_machinery.common.blocks.cable;

import static com.mrghastien.quantum_machinery.common.init.ModTileEntities.CABLE;

import com.mrghastien.quantum_machinery.common.capabilities.energy.ModEnergyStorage;
import com.mrghastien.quantum_machinery.util.helpers.BlockHelper;

import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class CableTile extends TileEntity implements ITickableTileEntity {

	public final int TRANSFER_RATE = 1024;
	private LazyOptional<IEnergyStorage> energy = LazyOptional.of(() -> new ModEnergyStorage(2048, 1024, 1024));

	public CableTile() {
		super(CABLE.get());
	}

	@Override
	public void tick() {
		if (!world.isRemote) {
			BlockHelper.sendOutPower(this);
			for (Direction dir : Direction.values()) {
				boolean canConnect = BlockHelper.canBlockHandleEnergy(pos.offset(dir), dir, world);
				switch (dir) {
				case NORTH:
					world.setBlockState(pos, world.getBlockState(pos).with(BlockStateProperties.NORTH, canConnect));
					break;
				case SOUTH:
					world.setBlockState(pos, world.getBlockState(pos).with(BlockStateProperties.SOUTH, canConnect));
					break;
				case EAST:
					world.setBlockState(pos, world.getBlockState(pos).with(BlockStateProperties.EAST, canConnect));
					break;
				case WEST:
					world.setBlockState(pos, world.getBlockState(pos).with(BlockStateProperties.WEST, canConnect));
					break;
				case UP:
					world.setBlockState(pos, world.getBlockState(pos).with(BlockStateProperties.UP, canConnect));
					break;
				case DOWN:
					world.setBlockState(pos, world.getBlockState(pos).with(BlockStateProperties.DOWN, canConnect));
					break;
				}
			}
		}
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == CapabilityEnergy.ENERGY) {
			return energy.cast();
		}
		return super.getCapability(cap, side);
	}

}
