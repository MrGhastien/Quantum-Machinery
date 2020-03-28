package com.mrghastien.quantum_machinery.network;

import java.util.function.Supplier;

import com.mrghastien.quantum_machinery.multiblocks.fission.tileentities.FissionControllerTile;
import com.mrghastien.quantum_machinery.multiblocks.fission.tileentities.FissionControllerTile.RunningState;
import com.mrghastien.quantum_machinery.network.client.FissionReturnPacket;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

public class FissionPacket {

	private BlockPos pos;
	private FissionControllerTile.RunningState requestState;
	
	public FissionPacket(BlockPos pos, RunningState requestState) {
		this.pos = pos;
		this.requestState = requestState;
	}
	
	public static void encode(FissionPacket pkt, PacketBuffer buf) {
		buf.writeInt(pkt.pos.getX());
		buf.writeInt(pkt.pos.getY());
		buf.writeInt(pkt.pos.getZ());
		buf.writeInt(pkt.requestState.getID());
	}
	
	public static FissionPacket decode(PacketBuffer buf){
	    int x = buf.readInt();
	    int y = buf.readInt();
	    int z = buf.readInt();
	    int s = buf.readInt();
	    FissionPacket instance = new FissionPacket(new BlockPos(x, y, z), RunningState.getStateById(s));
	    return instance;
	}
	
	public static void handle(FissionPacket pkt, Supplier<NetworkEvent.Context> ctx){
	    ctx.get().enqueueWork(() -> {
	    	FissionControllerTile te = (FissionControllerTile) ctx.get().getSender().getServerWorld().getTileEntity(pkt.pos);
		    if (te == null) {
				return;
			}
		    if (!te.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
				return;
			}
		    int pct =  te.getFormPct();
			ModNetworking.MAIN_CHANNEL.send(PacketDistributor.PLAYER.with(() -> ctx.get().getSender()),
					new FissionReturnPacket(te.getPos(), te.getEnergy(), te.getMaxEnergy(), te.getFuelLevel(),
							te.maxCapacity, te.getPowerSurge(), te.maxSurge, te.getState(), pct, te.isFormed()));
			te.requestState(pkt.requestState);
		});
	    ctx.get().setPacketHandled(true);
	}
}
