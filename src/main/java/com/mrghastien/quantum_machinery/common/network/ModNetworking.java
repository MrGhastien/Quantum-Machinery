package com.mrghastien.quantum_machinery.common.network;

import com.mrghastien.quantum_machinery.QuantumMachinery;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
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
		
		MAIN_CHANNEL.messageBuilder(EnergyPacket.class, nextID())
	    .encoder(EnergyPacket::encode)
	    .decoder(EnergyPacket::decode)
	    .consumer(EnergyPacket::handle)
	    .add();
		
		MAIN_CHANNEL.messageBuilder(HeatPacket.class, nextID())
	    .encoder(HeatPacket::encode)
	    .decoder(HeatPacket::decode)
	    .consumer(HeatPacket::handle)
	    .add();
		
	}

	public static void sendToPlayer(Object message, ServerPlayerEntity listener) {
		MAIN_CHANNEL.send(PacketDistributor.PLAYER.with(() -> listener), message);
	}
}
