package com.mrghastien.quantum_machinery.common.multiblocks.fission.tileentities;

import com.mrghastien.quantum_machinery.common.init.ModTileEntities;
import com.mrghastien.quantum_machinery.common.multiblocks.PartTile;

import net.minecraft.tileentity.ITickableTileEntity;

public class FissionPartTile extends PartTile implements ITickableTileEntity {
	
	public FissionPartTile() {
		super(ModTileEntities.FISSION_PART.get());
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
}
