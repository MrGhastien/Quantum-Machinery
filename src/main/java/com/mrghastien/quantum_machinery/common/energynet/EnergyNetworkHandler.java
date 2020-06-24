package com.mrghastien.quantum_machinery.common.energynet;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;

public class EnergyNetworkHandler {
	
	private static final EnergyNetworkHandler INSTANCE = new EnergyNetworkHandler();
	private final Set<EnergyNetwork> networks = new HashSet<EnergyNetwork>();
	
	public static EnergyNetworkHandler getInstance() {
		return INSTANCE;
	}
	
	public Set<EnergyNetwork> getNetworks() {
		return INSTANCE.networks;
	}
	
	private static int nextID() {
		int id = -1;
		for (int i = 0; i < INSTANCE.networks.size(); i++) {
			id++;
		}
		return id + 1;
	}
	
	public static EnergyNetwork changeNetwork(int newID, TileEntity te) {
		if (INSTANCE.networks.contains(getNetworkPerID(newID))) {
			for (EnergyNetwork net : INSTANCE.networks) {
				if (net.members.contains(te) && net.id != newID) {
					net.remove(te);
					return getNetworkPerID(newID).add(te);
				} else {
					return net;
				}
			}
		} else {
			return createNetwork(newID).add(te); 
		}
		return null;
	}
	
	private static EnergyNetwork getNetworkPerID(int id) {
		for (EnergyNetwork net : INSTANCE.networks) {
			if (net.id == id) {
				return net;
			}
		}
		return null;
	}
	
	public static EnergyNetwork createNetwork() {
		return createNetwork(nextID());
	}	
	
	public static EnergyNetwork createNetwork(int id) {
		boolean alreadyExists = false;
		EnergyNetwork net = new EnergyNetwork(id);
		for (EnergyNetwork energyNetwork : INSTANCE.networks) {
			if (energyNetwork.id == id) {
				alreadyExists = true;
				return energyNetwork;
			}
		}
		if (!alreadyExists) {
			INSTANCE.networks.add(net);
			return net;
		} else return null;
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
