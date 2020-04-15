package com.mrghastien.quantum_machinery.tileentities;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public abstract class PowerTransmitterTile extends TileEntity implements ITickableTileEntity {

	public PowerTransmitterTile(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}
	
}
