package com.mrghastien.quantum_machinery.network;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.network.client.FissionReturnPacket;
import com.mrghastien.quantum_machinery.network.client.HeatReturnPacket;
import com.mrghastien.quantum_machinery.network.client.MachineInfoReturnPacket;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetworking {
	
	public static final String PROTOCOL_VERSION = String.valueOf(1); 
	private static int ID = 0;
	
	private static int nextID() {
		return ID++ - 1;
	}
	
	public static final SimpleChannel MAIN_CHANNEL = NetworkRegistry.ChannelBuilder
	        .named(new ResourceLocation(QuantumMachinery.MODID, "main_channel"))
	        .networkProtocolVersion(() -> PROTOCOL_VERSION)
	        .clientAcceptedVersions(PROTOCOL_VERSION::equals)
	        .serverAcceptedVersions(PROTOCOL_VERSION::equals)
	        .simpleChannel();
	
	public static void registerNetworkPackets() {
		MAIN_CHANNEL.messageBuilder(MachineInfoPacket.class, nextID())
	    .encoder(MachineInfoPacket::encode)
	    .decoder(MachineInfoPacket::decode)
	    .consumer(MachineInfoPacket::handle)
	    .add();
		
		MAIN_CHANNEL.messageBuilder(MachineInfoReturnPacket.class, nextID())
	    .encoder(MachineInfoReturnPacket::encode)
	    .decoder(MachineInfoReturnPacket::decode)
	    .consumer(MachineInfoReturnPacket::handle)
	    .add();
		
		MAIN_CHANNEL.messageBuilder(FissionPacket.class, nextID())
	    .encoder(FissionPacket::encode)
	    .decoder(FissionPacket::decode)
	    .consumer(FissionPacket::handle)
	    .add();
		
		MAIN_CHANNEL.messageBuilder(FissionReturnPacket.class, nextID())
	    .encoder(FissionReturnPacket::encode)
	    .decoder(FissionReturnPacket::decode)
	    .consumer(FissionReturnPacket::handle)
	    .add();
		
		MAIN_CHANNEL.messageBuilder(HeatPacket.class, nextID())
	    .encoder(HeatPacket::encode)
	    .decoder(HeatPacket::decode)
	    .consumer(HeatPacket::handle)
	    .add();
		
		MAIN_CHANNEL.messageBuilder(HeatReturnPacket.class, nextID())
	    .encoder(HeatReturnPacket::encode)
	    .decoder(HeatReturnPacket::decode)
	    .consumer(HeatReturnPacket::handle)
	    .add();
	}
}
