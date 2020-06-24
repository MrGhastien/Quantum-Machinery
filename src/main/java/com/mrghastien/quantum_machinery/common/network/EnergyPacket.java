package com.mrghastien.quantum_machinery.common.network;

import java.util.function.Supplier;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.client.screens.IEnergyScreen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class EnergyPacket {

	private int energy;
	private int maxEnergy;
	private int inputLastTick;
	private int outputLastTick;
	
	public EnergyPacket(int energy, int maxEnergy, int inputLastTick, int outputLastTick) {
		this.energy = energy;
		this.maxEnergy = maxEnergy;
		this.inputLastTick = inputLastTick;
		this.outputLastTick = outputLastTick;
	}
	
	public static void encode(EnergyPacket pkt, PacketBuffer buf) {
		buf.writeInt(pkt.energy);
		buf.writeInt(pkt.maxEnergy);
		buf.writeInt(pkt.inputLastTick);
		buf.writeInt(pkt.outputLastTick);
	}
	
	public static EnergyPacket decode(PacketBuffer buf){
	    int e = buf.readInt();
	    int m = buf.readInt();
	    int in = buf.readInt();
	    int out = buf.readInt();
	    EnergyPacket instance = new EnergyPacket(e, m, in, out);
	    return instance;
	}
	
	public static void handle(EnergyPacket pkt, Supplier<NetworkEvent.Context> ctx){
	    ctx.get().enqueueWork(() -> {
	    	Screen currentScreen = QuantumMachinery.proxy.getCurrentScreen();
	    	if (currentScreen instanceof IEnergyScreen) {
			    ((IEnergyScreen)currentScreen).syncEnergy(pkt.energy, pkt.maxEnergy, pkt.inputLastTick, pkt.outputLastTick);
			}
	    });
	    ctx.get().setPacketHandled(true);
	}
	
}
