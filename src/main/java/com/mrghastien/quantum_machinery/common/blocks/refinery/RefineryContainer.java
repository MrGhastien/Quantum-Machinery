package com.mrghastien.quantum_machinery.common.blocks.refinery;

import static com.mrghastien.quantum_machinery.common.init.ModContainers.REFINERY_CONTAINER;

import com.mrghastien.quantum_machinery.common.blocks.MachineContainer;
import com.mrghastien.quantum_machinery.common.init.ModRecipes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class RefineryContainer extends MachineContainer<RefineryTile> {

	public final static int SIZE = 3;
	public static final int INPUT = 0;
	public static final int INPUT_2 = 1;
	public static final int OUTPUT = 2;

	public RefineryContainer(int id, World world, BlockPos pos, PlayerInventory playerinventory,
			PlayerEntity player) {
		super(REFINERY_CONTAINER.get(), id, world, pos, playerinventory, player,
				(RefineryTile) world.getTileEntity(pos), SIZE);
		tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
			addSlot(new SlotItemHandler(h, INPUT, 62, 17));
			addSlot(new SlotItemHandler(h, INPUT_2, 62, 62));
			addSlot(new SlotItemHandler(h, OUTPUT, 124, 40));
		});
		layoutPlayerInventorySlots(8, 94);
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

	protected boolean hasRecipe(ItemStack itemStack) {
		return this.world.getRecipeManager()
				.getRecipe(ModRecipes.REFINING_TYPE, new Inventory(itemStack), this.world).isPresent();
	}

}
