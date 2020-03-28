package com.mrghastien.quantum_machinery.network;

import java.util.function.Supplier;

import com.mrghastien.quantum_machinery.capabilities.temperature.CapabilityTemp;
import com.mrghastien.quantum_machinery.network.client.HeatReturnPacket;
import com.mrghastien.quantum_machinery.tileentities.MachineTile;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class HeatPacket {
		
		private BlockPos pos;
		
		public HeatPacket(BlockPos pos) {
			super();
			this.pos = pos;
		}
		
		public static void encode(HeatPacket pkt, PacketBuffer buf) {
			buf.writeInt(pkt.pos.getX());
			buf.writeInt(pkt.pos.getY());
			buf.writeInt(pkt.pos.getZ());
		}
		
		public static HeatPacket decode(PacketBuffer buf){
		    int x = buf.readInt();
		    int y = buf.readInt();
		    int z = buf.readInt();
		    HeatPacket instance = new HeatPacket(new BlockPos(x, y, z));
		    return instance;
		}
		
		public static void handle(HeatPacket pkt, Supplier<NetworkEvent.Context> ctx){
		    ctx.get().enqueueWork(() -> {
		    	MachineTile te = (MachineTile) ctx.get().getSender().getServerWorld().getTileEntity(pkt.pos);
			    if (te == null) {
					return;
				}
			    if (!te.getCapability(CapabilityTemp.HEAT).isPresent()) {
					return;
				}
			    double temp = te.getCapability(CapabilityTemp.HEAT).map(e -> e.getTemp()).orElse(0d);
			    double maxTemp = te.getCapability(CapabilityTemp.HEAT).map(e -> e.getMaxTemp()).orElse(0d);
			    ModNetworking.MAIN_CHANNEL.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()),
			    		new HeatReturnPacket(te.getPos(), temp, maxTemp));
		    });
		    ctx.get().setPacketHandled(true);
		}
	
}
