package mrghastien.quantum_machinery.common.blocks.cable;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import static mrghastien.quantum_machinery.common.init.ModTileEntities.CABLE;

import mrghastien.quantum_machinery.common.energynet.EnergyNetwork;
import mrghastien.quantum_machinery.common.energynet.EnergyNetworkHandler;

public class CableTile extends TileEntity {

	public int energy = 0;

	public CableTile() {
		super(CABLE.get());
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == CapabilityEnergy.ENERGY) {
			EnergyNetwork net = EnergyNetworkHandler.INSTANCE.get(world, pos);
			if(net.getLazy().isPresent())
				return net.getConnection(pos, side).getLazy().cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void remove() {
		EnergyNetworkHandler.INSTANCE.invalidateNetwork(pos, world);
		super.remove();
	}
}
