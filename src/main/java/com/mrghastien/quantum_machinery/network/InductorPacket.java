package com.mrghastien.quantum_machinery.network;

import java.util.function.Supplier;

import com.mrghastien.quantum_machinery.energynet.EnergyNetworkHandler;
import com.mrghastien.quantum_machinery.tileentities.InductorTile;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class InductorPacket {
	
	private BlockPos pos;
	private boolean send;
	private boolean recieve;
	private int networkID;
	
	public InductorPacket(BlockPos pos, boolean send, boolean recieve, int networkID) {
		super();
		this.pos = pos;
		this.send = send;
		this.recieve = recieve;
		this.networkID = networkID;
	}
	
	public static void encode(InductorPacket pkt, PacketBuffer buf) {
		buf.writeInt(pkt.pos.getX());
		buf.writeInt(pkt.pos.getY());
		buf.writeInt(pkt.pos.getZ());
		buf.writeBoolean(pkt.send);
		buf.writeBoolean(pkt.recieve);
		buf.writeInt(pkt.networkID);
	}
	
	public static InductorPacket decode(PacketBuffer buf){
	    int x = buf.readInt();
	    int y = buf.readInt();
	    int z = buf.readInt();
	    boolean send = buf.readBoolean();
	    boolean recieve = buf.readBoolean();
	    int netId = buf.readInt();
	    InductorPacket instance = new InductorPacket(new BlockPos(x, y, z), send, recieve, netId);
	    return instance;
	}
	
	public static void handle(InductorPacket pkt, Supplier<NetworkEvent.Context> ctx){
	    ctx.get().enqueueWork(() -> {
		    InductorTile wptTe = (InductorTile) ctx.get().getSender().getEntityWorld().getTileEntity(pkt.pos);
	    	wptTe.setSend(pkt.send);
	    	wptTe.setRecieve(pkt.recieve);
	    	EnergyNetworkHandler.changeNetwork(pkt.networkID, wptTe);
	    });
	    ctx.get().setPacketHandled(true);
	}
}
