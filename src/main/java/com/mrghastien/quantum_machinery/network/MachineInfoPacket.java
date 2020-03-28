package com.mrghastien.quantum_machinery.network;

import java.util.function.Supplier;

import com.mrghastien.quantum_machinery.network.client.MachineInfoReturnPacket;
import com.mrghastien.quantum_machinery.tileentities.MachineTile;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class MachineInfoPacket {
		
		private BlockPos pos;
		
		public MachineInfoPacket(BlockPos pos) {
			super();
			this.pos = pos;
		}
		
		public static void encode(MachineInfoPacket pkt, PacketBuffer buf) {
			buf.writeInt(pkt.pos.getX());
			buf.writeInt(pkt.pos.getY());
			buf.writeInt(pkt.pos.getZ());
		}
		
		public static MachineInfoPacket decode(PacketBuffer buf){
		    int x = buf.readInt();
		    int y = buf.readInt();
		    int z = buf.readInt();
		    MachineInfoPacket instance = new MachineInfoPacket(new BlockPos(x, y, z));
		    return instance;
		}
		
		public static void handle(MachineInfoPacket pkt, Supplier<NetworkEvent.Context> ctx){
		    ctx.get().enqueueWork(() -> {
		    	MachineTile te = (MachineTile) ctx.get().getSender().getServerWorld().getTileEntity(pkt.pos);
			    if (te == null) {
					return;
				}
			    if (!te.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
					return;
				}
			    int energy = te.getCapability(CapabilityEnergy.ENERGY).map(e -> e.getEnergyStored()).orElse(0);
			    int maxEnergy = te.getCapability(CapabilityEnergy.ENERGY).map(e -> e.getMaxEnergyStored()).orElse(0);
			    ModNetworking.MAIN_CHANNEL.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()),
			    		new MachineInfoReturnPacket(te.getPos(), energy, maxEnergy, te.getWorkTimer(), te.getMaxTimer()));
		    });
		    ctx.get().setPacketHandled(true);
		}
	
}
