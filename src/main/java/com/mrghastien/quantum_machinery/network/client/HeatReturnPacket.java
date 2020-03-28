package com.mrghastien.quantum_machinery.network.client;

import java.util.function.Supplier;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.client.screens.ITempScreen;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class HeatReturnPacket {

	private BlockPos pos;
	private double temp;
	private double maxTemp;
	
	public HeatReturnPacket(BlockPos pos, double temp, double maxTemp) {
		this.pos = pos;
		this.temp = temp;
		this.maxTemp = maxTemp;
	}

	public static void encode(HeatReturnPacket pkt, PacketBuffer buf) {
		buf.writeInt(pkt.pos.getX());
		buf.writeInt(pkt.pos.getY());
		buf.writeInt(pkt.pos.getZ());
		buf.writeDouble(pkt.temp);
		buf.writeDouble(pkt.maxTemp);
	}
	
	public static HeatReturnPacket decode(PacketBuffer buf){
	    int x = buf.readInt();
	    int y = buf.readInt();
	    int z = buf.readInt();
	    double t = buf.readDouble();
	    double mt = buf.readDouble();
	    HeatReturnPacket instance = new HeatReturnPacket(new BlockPos(x, y, z), t, mt);
	    return instance;
	}
	
	public static void handle(HeatReturnPacket pkt, Supplier<NetworkEvent.Context> ctx){
	    ctx.get().enqueueWork(() -> {
	    	if (QuantumMachinery.proxy.getCurrentScreen() instanceof ITempScreen) {
			    ((ITempScreen)QuantumMachinery.proxy.getCurrentScreen()).syncTemp(pkt.temp, pkt.maxTemp);
			}
		    
	    });
	    ctx.get().setPacketHandled(true);
	}
	
}
