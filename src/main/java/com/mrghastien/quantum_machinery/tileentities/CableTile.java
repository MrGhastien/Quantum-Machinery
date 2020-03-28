package com.mrghastien.quantum_machinery.tileentities;

import static com.mrghastien.quantum_machinery.init.ModTileEntities.CABLE_TILE;

import java.util.HashSet;
import java.util.Set;

import com.mrghastien.quantum_machinery.blocks.EnergyTransfer;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class CableTile extends TileEntity implements ITickableTileEntity{

	public final int TRANSFER_RATE = 1024;
	private Set<BlockPos> recieverPosList = new HashSet<>();
	
	public CableTile() {
		super(CABLE_TILE.get());
	}

	@Override
	public void tick() {
		if(!world.isRemote) {
			for (Direction direction : Direction.values()) {
				TileEntity te = world.getTileEntity(pos.offset(direction));
				if(te instanceof CableTile) {
					for (BlockPos blockPos : ((CableTile)te).getRecieverPosList()) {
						recieverPosList.add(blockPos);
					}
				} else if(te instanceof ConnectorTile) {
					if(((ConnectorTile)te).getState() == EnergyTransfer.RECIEVE) {
						recieverPosList.add(te.getPos());
					}
				}
			}
		}
	}
	
	public Set<BlockPos> getRecieverPosList() {
		return recieverPosList;
		
	}	
}
