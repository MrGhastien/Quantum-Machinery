package com.mrghastien.quantum_machinery.multiblocks.fission.tileentities;

import com.mrghastien.quantum_machinery.init.ModTileEntities;
import com.mrghastien.quantum_machinery.multiblocks.PartTile;

import net.minecraft.tileentity.ITickableTileEntity;

public class FissionPartTile extends PartTile implements ITickableTileEntity {
	
	public FissionPartTile() {
		super(ModTileEntities.FISSION_PART_TILE.get());
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
}
