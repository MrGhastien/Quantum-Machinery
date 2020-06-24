package com.mrghastien.quantum_machinery.common.blocks.blaster;

import static com.mrghastien.quantum_machinery.common.init.ModContainers.BLASTER_CONTAINER;

import com.mrghastien.quantum_machinery.common.blocks.MachineContainer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BlasterContainer extends MachineContainer<BlasterTile>{
	
	public final static int SIZE = 2;
	public static final int INPUT = 0;
	public static final int BATTERY = 1;

	public BlasterContainer(int id, World world, BlockPos pos, PlayerInventory playerinventory, PlayerEntity player) {
		super(BLASTER_CONTAINER.get(), id, world, pos, playerinventory, player, (BlasterTile) world.getTileEntity(pos), SIZE);
		
		tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
			addSlot(new SlotItemHandler(h, INPUT, 116, 35));
			addSlot(new SlotItemHandler(h, BATTERY, 77, 35));
		});
		layoutPlayerInventorySlots(8, 84);
		trackInt(new IntReferenceHolder() {
			
			@Override
			public void set(int value) {
				
			}
			
			@Override
			public int get() {
				return tileEntity.getWorkTimer();
			}
		});
	}

	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
		this.world.playSound((PlayerEntity) null, this.pos, SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, SoundCategory.BLOCKS,
				0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
	}
}
