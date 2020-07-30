package com.mrghastien.quantum_machinery.common.energynet;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mrghastien.quantum_machinery.common.blocks.cable.CableBlock;
import com.mrghastien.quantum_machinery.common.blocks.cable.CableTile;
import com.mrghastien.quantum_machinery.common.capabilities.energy.ConnectionType;
import com.mrghastien.quantum_machinery.util.helpers.BlockHelper;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyNetwork implements IEnergyStorage {

	private boolean built;
	private final Map<BlockPos, Set<Connection>> cables = new HashMap<>();
	private final int capacity;
	private int energy;
	private final LazyOptional<EnergyNetwork> lazy;
	private World world;

	public EnergyNetwork(World world, Set<BlockPos> cables, int energy, int capacity) {
		this.world = world;
		cables.forEach(b -> this.cables.put(b, Collections.emptySet()));
		this.energy = energy;
		this.capacity = capacity;
		lazy = LazyOptional.of(() -> this);
	}
	
	public LazyOptional<EnergyNetwork> getLazy() {
		return lazy;
	}

	public boolean contains(World world, BlockPos pos) {
		return this.world == world && cables.containsKey(pos);
	}

	public Connection getConnection(BlockPos pos, Direction facing) {
		if (cables.containsKey(pos)) {
			for (Connection c : cables.get(pos)) {
				if (c.dir == facing) {
					return c;
				}
			}
		}
		return new Connection(facing, this, ConnectionType.NONE);
	}

	public int getSize() {
		return cables.size();
	}

	public TileEntity getTile(BlockPos pos) {
		return world.getTileEntity(pos);
	}

	public void invalidate() {
		lazy.invalidate();
		cables.values().forEach(set -> set.forEach(con -> con.lazy.invalidate()));
	}

	//Energy Storage implementation

	@Override
	public int getEnergyStored() {
		return energy;
	}

	@Override
	public int getMaxEnergyStored() {
		return capacity;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		buildConnections();

		if (getEnergyStored() - maxExtract < 0) {
			maxExtract -= maxExtract - getEnergyStored();
		}
		if (!simulate) {
			energy -= maxExtract;
			handleEnergy();
		}
		return maxExtract;
	}

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		buildConnections();

		if (getEnergyStored() + maxReceive > getMaxEnergyStored()) {
			maxReceive -= getEnergyStored() + maxReceive - getMaxEnergyStored();
		}
		if (!simulate) {
			energy += maxReceive;
			handleEnergy();
		}
		return maxReceive;
	}

	@Override
	public boolean canExtract() {
		return true;
	}

	@Override
	public boolean canReceive() {
		return true;
	}

	private void buildConnections() {
		if (!built) {
			cables.keySet().forEach(p -> cables.put(p, getConnections(world, p)));
			built = true;
		}
	}

	private Set<Connection> getConnections(World world, BlockPos pos) {
		Set<Connection> connections = new HashSet<>();
		for (Direction dir : Direction.values()) {
			Block block = world.getBlockState(pos).getBlock();
			TileEntity tile = world.getTileEntity(pos.offset(dir));
			if (tile != null && block != null && !(tile instanceof CableTile) && BlockHelper.canBlockHandleEnergy(pos, null, world)) {
				ConnectionType type = CableBlock.getConnection(world.getBlockState(pos), dir);
				connections.add(new Connection(dir, this, type));
			}
		}
		return connections;
	}

	private void handleEnergy() {
		int perCable = energy / getSize();
		cables.keySet().forEach(p -> {
			TileEntity tile = world.getTileEntity(p);
			if (tile instanceof CableTile)
				((CableTile) tile).energy = perCable;
		});
	}

	void sendOutEnergy() {
		buildConnections();
		cables.forEach((p, cs) -> {
			Block block = world.getBlockState(p).getBlock();
			if (block instanceof CableBlock) {
				int rate = ((CableBlock) block).transferRate;
				cs.forEach(c -> {
					IEnergyStorage storage = BlockHelper.getEnergyStorage(p.offset(c.dir), c.dir.getOpposite(), world);
					if(storage != null) {
						int toSend = extractEnergy(rate, true);
						extractEnergy(storage.receiveEnergy(toSend, false), false);
					}
				});
			}
		});
	}

	public static EnergyNetwork buildNetwork(World world, BlockPos pos) {
		Set<BlockPos> cables = buildPosSet(world, pos);
		int energy = cables.stream().mapToInt(p -> {
			TileEntity te = world.getTileEntity(p);
			return te instanceof CableTile ? ((CableTile) te).energy : 0;
		}).sum();
		int maxEnergy = cables.stream().mapToInt(p -> {
			Block block = world.getBlockState(p).getBlock();
			return block instanceof CableBlock ? ((CableBlock) block).transferRate : 0;
		}).sum();
		return new EnergyNetwork(world, cables, energy, maxEnergy);
	}

	private static Set<BlockPos> buildPosSet(World world, BlockPos pos) {
		return buildPosSet(world, pos, new HashSet<>());
	}

	private static Set<BlockPos> buildPosSet(World world, BlockPos pos, Set<BlockPos> set) {
		set.add(pos);
		for (Direction dir : Direction.values()) {
			BlockPos offsetPos = pos.offset(dir);
			if (!set.contains(offsetPos) && world.getTileEntity(offsetPos) instanceof CableTile) {
				set.add(offsetPos);
				set.addAll(buildPosSet(world, offsetPos, set));
			}
		}
		return set;
	}

	public static class Connection implements IEnergyStorage {

		private final Direction dir;
		private final EnergyNetwork net;
		private final ConnectionType type;
		private final LazyOptional<Connection> lazy;

		public Connection(Direction dir, EnergyNetwork net, ConnectionType type) {
			this.dir = dir;
			this.net = net;
			this.type = type;
			this.lazy = LazyOptional.of(() -> this);
		}

		public LazyOptional<Connection> getLazy() {
			return lazy;
		}
		

		@Override
		public int getEnergyStored() {
			return net.getEnergyStored();
		}

		@Override
		public int getMaxEnergyStored() {
			return net.getMaxEnergyStored();
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			if (!canExtract())
				return 0;
			return net.extractEnergy(maxExtract, simulate);
		}		
		
		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			if (!canReceive())
				return 0;
			return net.receiveEnergy(maxReceive, simulate);
		}
		
		@Override
		public boolean canExtract() {
			return type.canExtract();
		}

		@Override
		public boolean canReceive() {
			return type.canReceive();
		}
	}
}