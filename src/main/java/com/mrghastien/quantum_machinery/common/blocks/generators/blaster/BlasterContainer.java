package com.mrghastien.quantum_machinery.common.blocks.generators.blaster;

import static com.mrghastien.quantum_machinery.common.init.ModContainers.BLASTER_CONTAINER;

import com.mrghastien.quantum_machinery.api.client.EnergyBar;
import com.mrghastien.quantum_machinery.api.client.EnergyBar.Type;
import com.mrghastien.quantum_machinery.common.blocks.BaseContainer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BlasterContainer extends BaseContainer<BlasterTile>{
	
	public final static int SIZE = 2;
	public static final int INPUT = 0;
	public static final int BATTERY = 1;

	public BlasterContainer(int id, World world, BlockPos pos, PlayerInventory playerinventory) {
		super(BLASTER_CONTAINER.get(), id, world, pos, playerinventory, (BlasterTile) world.getTileEntity(pos), SIZE);
		addEnergyBar(new EnergyBar(() -> tileEntity.getEnergyStorage(), 152, 8, 9, 70, Type.GENERATOR));
		tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
			addSlot(new SlotItemHandler(h, INPUT, 80, 35));
			addSlot(new SlotItemHandler(h, BATTERY, 41, 35));
		});
		layoutPlayerInventorySlots(8, 84);
	}

	@Override
	public void onContainerClosed(PlayerEntity playerIn) {
		super.onContainerClosed(playerIn);
		this.world.playSound((PlayerEntity) null, this.pos, SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, SoundCategory.BLOCKS,
				0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
	}
}
