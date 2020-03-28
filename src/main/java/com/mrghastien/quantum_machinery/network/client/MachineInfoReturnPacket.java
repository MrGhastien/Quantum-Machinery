package com.mrghastien.quantum_machinery.network.client;

import java.util.function.Supplier;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.client.screens.MachineScreen;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class MachineInfoReturnPacket {

	private BlockPos pos;
	private int energy;
	private int maxEnergy;
	private int workTimer;
	private int maxTimer;
	
	public MachineInfoReturnPacket(BlockPos pos, int energy, int maxEnergy, int workTimer, int maxTimer) {
		super();
		this.pos = pos;
		this.energy = energy;
		this.maxEnergy = maxEnergy;
		this.workTimer = workTimer;
		this.maxTimer = maxTimer;
	}
	
	public static void encode(MachineInfoReturnPacket pkt, PacketBuffer buf) {
		buf.writeInt(pkt.pos.getX());
		buf.writeInt(pkt.pos.getY());
		buf.writeInt(pkt.pos.getZ());
		buf.writeInt(pkt.energy);
		buf.writeInt(pkt.maxEnergy);
		buf.writeInt(pkt.workTimer);
		buf.writeInt(pkt.maxTimer);
	}
	
	public static MachineInfoReturnPacket decode(PacketBuffer buf){
	    int x = buf.readInt();
	    int y = buf.readInt();
	    int z = buf.readInt();
	    int e = buf.readInt();
	    int m = buf.readInt();
	    int w = buf.readInt();
	    int mw = buf.readInt();
	    MachineInfoReturnPacket instance = new MachineInfoReturnPacket(new BlockPos(x, y, z), e, m, w, mw);
	    return instance;
	}
	
	public static void handle(MachineInfoReturnPacket pkt, Supplier<NetworkEvent.Context> ctx){
	    ctx.get().enqueueWork(() -> {
	    	if (QuantumMachinery.proxy.getCurrentScreen() instanceof MachineScreen<?, ?>) {
			    ((MachineScreen<?, ?>)QuantumMachinery.proxy.getCurrentScreen()).sync(pkt.energy, pkt.maxEnergy, pkt.workTimer, pkt.maxTimer);
			}
		    
	    });
	    ctx.get().setPacketHandled(true);
	}
	
}
