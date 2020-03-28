package com.mrghastien.quantum_machinery.containers;

import com.mrghastien.quantum_machinery.capabilities.energy.ModEnergyStorage;
import com.mrghastien.quantum_machinery.tileentities.MachineTile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class MachineContainer<T extends MachineTile> extends Container{

	protected T tileEntity;
	protected PlayerEntity player;
	protected IItemHandler playerInventory;
	protected BlockPos pos;
	protected World world;
	protected int size;

	protected MachineContainer(ContainerType<?> type, int id, World world, BlockPos pos, PlayerInventory playerinventory, PlayerEntity player, T tileEntity, int size) {
		super(type, id);
		this.tileEntity = tileEntity;
		this.player = player;
		this.playerInventory = new InvWrapper(playerinventory);
		this.pos = pos;
		this.world = world;
		this.size = size;
	}
	
	@Override
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

			if (index < this.size) {
				if (!this.mergeItemStack(itemstack1, this.size, this.inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, this.size, false)) {
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
