package mrghastien.quantum_machinery.common.multiblocks.fission.tileentities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import mrghastien.quantum_machinery.common.capabilities.energy.ModEnergyStorage;
import mrghastien.quantum_machinery.common.init.ModTileEntities;
import mrghastien.quantum_machinery.common.multiblocks.MultiBlockTile;

public class FissionEOTile extends MultiBlockTile implements ITickableTileEntity{

	private BlockPos controllerPos;
	protected LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergyHandler);
	
	public FissionEOTile() {
		super(ModTileEntities.FISSION_OUTLET.get());
	}
	
	private IEnergyStorage createEnergyHandler() {
		return new ModEnergyStorage(Integer.MAX_VALUE);
	}

	@Override
	public void tick() {
		if (!isFormed) {
			return;
		};
		//OBlockHelper.sendOutPower(world.getTileEntity(controllerPos));
		SnowballEntity ent = new SnowballEntity(world, pos.getX(), pos.getY(), pos.getZ());
		ent.setMotion(-5, 0, 8);
		world.addEntity(ent);
	}

	public boolean isUsableByPlayer(PlayerEntity player) {
		return this.world.getTileEntity(pos) != this ? false
				: player.getDistanceSq(this.pos.getX() + 0.5d, this.pos.getY() + 0.5d,
						this.pos.getZ() + 0.5d) <= 64d;
	}

	public int getEnergy() {
		return this.getCapability(CapabilityEnergy.ENERGY).map(e -> e.getEnergyStored()).orElse(0);
	}

	public int getMaxEnergy() {
		return this.getCapability(CapabilityEnergy.ENERGY).map(e -> e.getMaxEnergyStored()).orElse(0);
	}
	
	@Override
	public boolean isFormed() {
		return isFormed;
	}
	
	public void setFormed(boolean formed) {
		this.isFormed = formed;
	}
	
	public BlockPos getControllerPos() {
		return controllerPos;
	}

}
