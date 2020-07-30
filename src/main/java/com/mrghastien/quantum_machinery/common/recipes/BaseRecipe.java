/**
 * 
 */
package com.mrghastien.quantum_machinery.common.recipes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/** Base class for all machine recipes
 * @author MrGhastien
 *	
 */
public abstract class BaseRecipe implements IRecipe<BaseRecipe.DummyInventory> {

	private final ResourceLocation id;
	
	public BaseRecipe(ResourceLocation id) {
		this.id = id;
	}
	
	/**
	 * Use this method to write a recipe to a PacketBuffer.
	 * @param buf The buffer to write to.
	 */
	public abstract void write(PacketBuffer buf);
	
	
	//Vanilla methods
	
	@Override
	public boolean matches(DummyInventory inv, World worldIn) {
		return true;	
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getCraftingResult(DummyInventory inv) {
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}

	public static class DummyInventory implements IInventory {

		private static final DummyInventory INSTANCE = new DummyInventory();
		
		public static DummyInventory getInstance() {
			return INSTANCE;
		}
		
		@Override
		public void clear() {

		}

		@Override
		public int getSizeInventory() {
			return 0;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public ItemStack getStackInSlot(int index) {
			return ItemStack.EMPTY;
		}

		@Override
		public ItemStack decrStackSize(int index, int count) {
			return ItemStack.EMPTY;
		}

		@Override
		public ItemStack removeStackFromSlot(int index) {
			return ItemStack.EMPTY;
		}

		@Override
		public void setInventorySlotContents(int index, ItemStack stack) {

		}

		@Override
		public void markDirty() {

		}

		@Override
		public boolean isUsableByPlayer(PlayerEntity player) {
			return false;
		}
	}
}
