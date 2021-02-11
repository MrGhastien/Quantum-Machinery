package mrghastien.quantum_machinery.common.multiblocks.fission.tileentities;

import net.minecraft.tileentity.ITickableTileEntity;

import mrghastien.quantum_machinery.common.init.ModTileEntities;
import mrghastien.quantum_machinery.common.multiblocks.PartTile;

public class FissionPartTile extends PartTile implements ITickableTileEntity {
	
	public FissionPartTile() {
		super(ModTileEntities.FISSION_PART.get());
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
}
