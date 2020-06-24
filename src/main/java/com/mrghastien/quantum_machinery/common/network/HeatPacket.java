package com.mrghastien.quantum_machinery.common.network;

import java.util.function.Supplier;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.client.screens.IHeatScreen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class HeatPacket {
	
	private float temp;
	private float maxTemp;
	
	public HeatPacket(float temp, float maxTemp) {
		this.temp = temp;
		this.maxTemp = maxTemp;
	}
	
	public static void encode(HeatPacket pkt, PacketBuffer buf) {
		buf.writeFloat(pkt.temp);
		buf.writeFloat(pkt.maxTemp);
	}
	
	public static HeatPacket decode(PacketBuffer buf) {
		return new HeatPacket(buf.readFloat(), buf.readFloat());
	}
	
	public static void handle(HeatPacket pkt, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Screen currentScreen = QuantumMachinery.proxy.getCurrentScreen();
			if (currentScreen instanceof IHeatScreen) {
				((IHeatScreen) currentScreen).syncTemperature(pkt.temp, pkt.maxTemp);
			}
		});
	}
}
