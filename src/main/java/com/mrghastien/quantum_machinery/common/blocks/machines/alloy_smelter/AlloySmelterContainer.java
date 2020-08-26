package com.mrghastien.quantum_machinery.common.blocks.machines.alloy_smelter;

import static com.mrghastien.quantum_machinery.common.init.ModContainers.ALLOY_SMELTER_CONTAINER;

import com.mrghastien.quantum_machinery.api.client.EnergyBar;
import com.mrghastien.quantum_machinery.api.client.EnergyBar.Type;
import com.mrghastien.quantum_machinery.common.blocks.BaseContainer;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AlloySmelterContainer extends BaseContainer<AlloySmelterTile> {

	public AlloySmelterContainer(int id, World world, BlockPos pos, PlayerInventory playerinventory) {
		super(ALLOY_SMELTER_CONTAINER.get(), id, world, pos, playerinventory, (AlloySmelterTile) world.getTileEntity(pos),
				5);
		addEnergyBar(new EnergyBar(() -> tileEntity.getEnergyStorage(), 26, 17, 3, 70, Type.CONSUMER));
		tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
			addSlot(new SlotItemHandler(h, 0, 62, 17));
			addSlot(new SlotItemHandler(h, 1, 42, 33));
			addSlot(new SlotItemHandler(h, 2, 42, 55));
			addSlot(new SlotItemHandler(h, 3, 62, 71));
			addSlot(new SlotItemHandler(h, 4, 124, 45));
		});
		layoutPlayerInventorySlots(8, 102);
	}

	/*
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 2) {
				if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(itemstack1, itemstack);
			} else if (index < this.size && index != 2 && index != 0 && index != 1) {
				if (this.hasRecipe(itemstack1) && !this.mergeItemStack(itemstack1, INPUT, INPUT + 1, false)) {
					return ItemStack.EMPTY;

				} else if (!this.mergeItemStack(itemstack1, INPUT_2, INPUT_2 + 1, false)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, this.size, this.inventorySlots.size(), true)) {
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
	*/

}
