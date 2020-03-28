package com.mrghastien.quantum_machinery.network.client;

import java.util.function.Supplier;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.client.multiblocks.fission.screens.FissionScreen;
import com.mrghastien.quantum_machinery.multiblocks.fission.tileentities.FissionControllerTile;
import com.mrghastien.quantum_machinery.multiblocks.fission.tileentities.FissionControllerTile.RunningState;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

public class FissionReturnPacket {

	private BlockPos pos;
	private int energy;
	private int maxEnergy;
	private int powerSurge, maxSurge;
	private int fuelLevel, maxCapacity;
	private FissionControllerTile.RunningState state;
	private int formPct;
	private boolean isFormed;
	
	public FissionReturnPacket(BlockPos pos, int energy, int maxEnergy, int fuelLevel, int maxCapacity, int powerSurge,
			int maxSurge, RunningState runningState, int formPct, boolean isFormed) {
		this.pos = pos;
		this.energy = energy;
		this.maxEnergy = maxEnergy;
		this.fuelLevel = fuelLevel;
		this.maxCapacity = maxCapacity;
		this.powerSurge = powerSurge;
		this.maxSurge = maxSurge;
		this.state = runningState;
		this.formPct = formPct;
		this.isFormed = isFormed;
	}

	public static void encode(FissionReturnPacket pkt, PacketBuffer buf) {
		buf.writeInt(pkt.pos.getX());
		buf.writeInt(pkt.pos.getY());
		buf.writeInt(pkt.pos.getZ());
		buf.writeInt(pkt.energy);
		buf.writeInt(pkt.maxEnergy);
		buf.writeInt(pkt.fuelLevel);
		buf.writeInt(pkt.maxCapacity);
		buf.writeInt(pkt.powerSurge);
		buf.writeInt(pkt.maxSurge);
		buf.writeInt(pkt.state.getID());
		buf.writeInt(pkt.formPct);
		buf.writeBoolean(pkt.isFormed);
	}
	
	public static FissionReturnPacket decode(PacketBuffer buf){
	    int x = buf.readInt();
	    int y = buf.readInt();
	    int z = buf.readInt();
	    int e = buf.readInt();
	    int maxe = buf.readInt();
	    int f = buf.readInt();
	    int maxf = buf.readInt();
	    int ps = buf.readInt();
	    int maxps = buf.readInt();
	    int i = buf.readInt();
	    int pct = buf.readInt();
	    boolean isF = buf.readBoolean();
	    FissionReturnPacket instance = new FissionReturnPacket(new BlockPos(x, y, z), e, maxe, f, maxf, ps, maxps, RunningState.getStateById(i), pct, isF);
	    return instance;
	}
	
	public static void handle(FissionReturnPacket pkt, Supplier<NetworkEvent.Context> ctx){
	    ctx.get().enqueueWork(() -> {
	    	if (QuantumMachinery.proxy.getCurrentScreen() instanceof FissionScreen) {
	    		FissionScreen fScreen = ((FissionScreen)QuantumMachinery.proxy.getCurrentScreen()); 
	    		fScreen.sync(pkt.energy, pkt.maxEnergy, pkt.fuelLevel, pkt.maxCapacity, pkt.powerSurge, pkt.maxSurge, pkt.state, pkt.formPct, pkt.isFormed);
			} 
	    });
	    ctx.get().setPacketHandled(true);
	}
	
}
