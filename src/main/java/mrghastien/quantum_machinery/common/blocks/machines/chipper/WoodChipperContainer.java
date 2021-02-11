package mrghastien.quantum_machinery.common.blocks.machines.chipper;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static mrghastien.quantum_machinery.common.init.ModContainers.WOOD_CHIPPER;

import mrghastien.quantum_machinery.common.blocks.BaseContainer;

public class WoodChipperContainer extends BaseContainer<WoodChipperTile> {

	public WoodChipperContainer(int id, World world, BlockPos pos, PlayerInventory playerinventory) {
		super(WOOD_CHIPPER.get(), id, world, pos, playerinventory, (WoodChipperTile) world.getTileEntity(pos), 0);
	}

	
	
}
