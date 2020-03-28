package com.mrghastien.quantum_machinery.multiblocks.fission.containers;

import com.mrghastien.quantum_machinery.capabilities.energy.ModEnergyStorage;
import com.mrghastien.quantum_machinery.init.ModContainers;
import com.mrghastien.quantum_machinery.multiblocks.fission.tileentities.FissionControllerTile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class FissionContainer extends Container{

	public static final int SIZE = 2;
	private static final int FUEL_INPUT = 0;
	private static final int WASTE_OUTPUT = 1;
	
	protected FissionControllerTile tileEntity;
	protected PlayerEntity player;
	protected IItemHandler playerInventory;
	protected BlockPos pos;
	protected World world;

	public FissionContainer(int id, World world, BlockPos pos, PlayerInventory playerinventory, PlayerEntity player) {
		super(ModContainers.FISSION_CONTROLLER_CONTAINER.get(), id);
		this.tileEntity = (FissionControllerTile) world.getTileEntity(pos);
		this.player = player;
		this.playerInventory = new InvWrapper(playerinventory);
		this.pos = pos;
		this.world = world;
		tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
				addSlot(new SlotItemHandler(h, FUEL_INPUT, 6, 123));
				addSlot(new SlotItemHandler(h, WASTE_OUTPUT, 38, 123));
		});
		layoutPlayerInventorySlots(24, 150);
	}
	
	public boolean canInteractWith(PlayerEntity playerIn) {
		return tileEntity.isUsableByPlayer(playerIn);
	}

	public int getEnergy() {
		return tileEntity.getEnergy();
	}
    
    public void setEnergy(int value) {
    	tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(e -> ((ModEnergyStorage)e).setEnergyStored(value));
    }
    
	public int getMaxEnergy() {
		return tileEntity.getMaxEnergy();
	}
	
	protected int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
        	addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }
	
	protected int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
		for (int j = 0 ; j < verAmount ; j++) {
			index = addSlotRange(handler, index, x, y, horAmount, dx);
			y += dy;
		}
		return index;
	}

	protected void layoutPlayerInventorySlots(int leftCol, int topRow) {
		// Player inventory
		addSlotBox(playerInventory, 9, leftCol, topRow, 9,  18, 3, 18);

		// Hotbar
		topRow += 58;
		addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
	}
    
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < SIZE) {
				if (!this.mergeItemStack(itemstack1, SIZE, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, SIZE, false)) {
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
	    
	public BlockPos getPos() {
		return pos;
	}

}
