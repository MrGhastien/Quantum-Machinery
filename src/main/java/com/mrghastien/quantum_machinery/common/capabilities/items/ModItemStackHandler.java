package com.mrghastien.quantum_machinery.common.capabilities.items;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

public class ModItemStackHandler extends ItemStackHandler {

	private final TileEntity tile;
	private final LazyOptional<ModItemStackHandler> lazy;
	
	public ModItemStackHandler(TileEntity tile) {
		this(tile, 1);
	}

	public ModItemStackHandler(TileEntity tile, int size) {
		super(size);
		this.tile = tile;
		this.lazy = LazyOptional.of(() -> this);
	}

	public ModItemStackHandler(TileEntity tile, NonNullList<ItemStack> stacks) {
		super(stacks);
		this.tile = tile;
		this.lazy = LazyOptional.of(() -> this);
	}
	
	@Override
	protected void onContentsChanged(int slot) {
		super.onContentsChanged(slot);
		if(tile != null) tile.markDirty();
	}
	
	public LazyOptional<ModItemStackHandler> getLazy() {
		return lazy;
	}
}
