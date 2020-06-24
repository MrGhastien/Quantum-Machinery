package com.mrghastien.quantum_machinery.common.multiblocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public abstract class PartTile extends MultiBlockTile {

	protected ControllerTile master;
	
	public PartTile(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}
	
	@Override
	public void tick() {
		if (!world.isRemote) {
			if (master == null) {
				for (Direction direction : Direction.values()) {
					BlockPos p = pos.offset(direction);
					TileEntity te = world.getTileEntity(p);
					if (te instanceof ControllerTile) {
						this.master = (ControllerTile) te;
					} else if (te instanceof PartTile) {
						ControllerTile master = ((PartTile)te).getMaster();
						if(master != null) {
							this.master = master;
						}
					}
				}
			}
		}
	}

	public ControllerTile getMaster() {
		return master;
	}
	
}
