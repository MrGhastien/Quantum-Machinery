package com.mrghastien.quantum_machinery.containers;

import static com.mrghastien.quantum_machinery.init.ModContainers.ELECTRIC_FURNACE_CONTAINER;

import com.mrghastien.quantum_machinery.tileentities.ElectricFurnaceTile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ElectricFurnaceContainer extends MachineContainer<ElectricFurnaceTile> {
	
	private static final int SIZE = 2;
	private static final int INPUT = 0;
	private static final int OUTPUT = 1;
	
	public ElectricFurnaceContainer(int id, World world, BlockPos pos, PlayerInventory playerinventory, PlayerEntity player) {
		super(ELECTRIC_FURNACE_CONTAINER.get(), id, world, pos, playerinventory, player, (ElectricFurnaceTile) world.getTileEntity(pos), SIZE);
		
		tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
			addSlot(new SlotItemHandler(h, INPUT, 62, 40));
			addSlot(new SlotItemHandler(h, OUTPUT, 124, 40));
		});
		layoutPlayerInventorySlots(8, 94);
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
		return tileEntity.isUsableByPlayer(playerIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < this.size) {
				if (!this.mergeItemStack(itemstack1, this.size, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}
		}
		return itemstack;
	}

}
