package mrghastien.quantum_machinery.common.energynet;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent.ServerTickEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class EnergyNetworkHandler {
	
	private final Set<EnergyNetwork> networks = Collections.synchronizedSet(new HashSet<EnergyNetwork>());
	
	public static final EnergyNetworkHandler INSTANCE = new EnergyNetworkHandler();

	private EnergyNetworkHandler() {
		MinecraftForge.EVENT_BUS.addListener(this::onServerTick);
	}
	
	public Set<EnergyNetwork> getNetworks() {
		return Collections.unmodifiableSet(networks);
	}
	
	public LazyOptional<EnergyNetwork> getLazy(World world, BlockPos pos) {
		return get(world, pos).getLazy();
	}
	
	public synchronized EnergyNetwork get(World world, BlockPos pos) {
		for(EnergyNetwork net : networks) {
			if(net != null && net.getLazy().isPresent()) {
				if(net.contains(world, pos)) {
					return net;
				}
			}
		}
		
		EnergyNetwork newNet = EnergyNetwork.buildNetwork(world, pos);
		//QuantumMachinery.LOGGER.debug("created network " + newNet.hashCode());
		networks.add(newNet);
		return newNet;
	}
	
	public void invalidateNetwork(BlockPos pos, World world) {
		Collection<EnergyNetwork> collection = networks.stream().filter(net -> net != null && net.getLazy().isPresent() && net.contains(world, pos)).collect(Collectors.toList());
		collection.forEach(this::invalidateNetwork);
	}
	
	public synchronized void invalidateNetwork(EnergyNetwork network) {
		//QuantumMachinery.LOGGER.debug("Invalidated network " + network.hashCode());
		networks.removeIf(net -> net != null && net.equals(network));
		network.invalidate();
	}
	
	public void onServerTick(ServerTickEvent e) {
		networks.stream().filter(n -> n != null && n.getLazy().isPresent()).forEach(EnergyNetwork::sendOutEnergy);
	}
	
	public enum NetworkTier {
		BASIC(0, 256), 
		ASTRONIUM(1, 1024);
		
		private int transferRate;
		private int id;
		
		private NetworkTier(int id, int transferRate) {
			this.transferRate = transferRate;
			this.id = id;
		}
		
		public int getTransferRate() {
			return transferRate;
		}
		
		public int getId() {
			return id;
		}
	}
}
