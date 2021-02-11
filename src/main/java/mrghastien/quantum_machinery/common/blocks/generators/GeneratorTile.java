package mrghastien.quantum_machinery.common.blocks.generators;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntityType;

import mrghastien.quantum_machinery.common.blocks.BaseTile;
import mrghastien.quantum_machinery.common.network.GuiSynced;

public abstract class GeneratorTile extends BaseTile {

	@GuiSynced
	protected int burnTime;
	@GuiSynced
	protected int totalBurnTime;
	@GuiSynced
	protected boolean burning = false;

	public GeneratorTile(TileEntityType<?> tileEntityTypeIn, int capacity,
			int maxOutput) {
		super(tileEntityTypeIn, capacity, 0, maxOutput);
	}

	protected BlockState getInactiveState(BlockState state) {
		return state.with(BlockStateProperties.LIT, false);
	}

	protected BlockState getActiveState(BlockState state) {
		return state.with(BlockStateProperties.LIT, true);
	}

	protected void setInactiveState() {
		updateBlockState(getInactiveState(world.getBlockState(pos)));
		burning = false;
	}

	protected void setActiveState() {
		updateBlockState(getActiveState(world.getBlockState(pos)));
		burning = true;
	}

	protected boolean canRun() {
		return energy.getAvailableSpace() > 0;
	}

	protected abstract void consumeFuel();

	protected abstract boolean containsFuel();

	protected abstract int getEnergyProduction();
	
	@Override
	protected void behavior() {
		if (canRun()) {
			if (burnTime <= 0 && containsFuel()) {
				consumeFuel();
				setActiveState();
			}
		}

		if (burnTime > 0) {
			burnTime--;
			energy.generateEnergy(getEnergyProduction(), false);
		} else {
			setInactiveState();
		}
	}

	public int getBurnTime() {
		return burnTime;
	}

	public int getTotalBurnTime() {
		return totalBurnTime;
	}
	
	@Override
	public void read(BlockState state, CompoundNBT compound) {
		super.read(state, compound);
		burnTime = compound.getInt("BurnTime");
		totalBurnTime = compound.getInt("TotalBurnTime");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putInt("BurnTime", burnTime);
		compound.putInt("TotalBurnTime", totalBurnTime);
		return compound;
	}
	
	public boolean isBurning() {
		return burning;
	}
	
	public int getCurrentEnergyProduction() {
		return burning ? getEnergyProduction() : 0;
	}
}
