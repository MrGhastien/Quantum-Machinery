package com.mrghastien.quantum_machinery.multiblocks;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class MultiBlockTile extends TileEntity implements ITickableTileEntity{

	protected boolean isFormed = false;
	
	public MultiBlockTile(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}
	
	public void form() {
		if (!isFormed) {
			this.isFormed = true;
		}
	}
	
	public void unform() {
		if (isFormed) {
			this.isFormed = false;
		}
	}
	
	public boolean isFormed() {
		return isFormed;
	}
}
