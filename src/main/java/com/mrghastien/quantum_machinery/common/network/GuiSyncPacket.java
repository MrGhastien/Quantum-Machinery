package com.mrghastien.quantum_machinery.common.network;

import java.util.function.Supplier;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.common.blocks.MachineContainer;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class GuiSyncPacket {

	private int id;
	private Object value;
	private byte type;
	
	public GuiSyncPacket(SyncedField<?> field, int id) {
		this.id = id;
		this.value = field.getValue();
		this.type = field.getType();
	}
	
	public void encode(PacketBuffer buf) {
		buf.writeByte(type);
		buf.writeInt(id);
		SyncedField.encode(buf, value, type);
	}
	
	public GuiSyncPacket(PacketBuffer buf) {
		type = buf.readByte();
		id = buf.readInt();
		SyncedField.decode(buf, type);
	}
	
	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Screen screen = QuantumMachinery.proxy.getCurrentScreen();
			if(screen instanceof ContainerScreen<?>) {
				Container container = ((ContainerScreen<?>) screen).getContainer();
				if(container instanceof MachineContainer<?>) {
					((MachineContainer<?>) container).updateField(value, id);
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
