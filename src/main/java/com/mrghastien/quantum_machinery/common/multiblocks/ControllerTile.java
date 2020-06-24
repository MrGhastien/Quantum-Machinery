package com.mrghastien.quantum_machinery.common.multiblocks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mrghastien.quantum_machinery.util.math.Coord3;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public abstract class ControllerTile extends MultiBlockTile {

	protected Coord3 formPos;
	protected FormMode mode;
	protected Set<PlayerEntity> listeners;
	protected List<PartTile> parts = new ArrayList<PartTile>();
	
	public ControllerTile(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}
	
	protected void formBlock(BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof MultiBlockTile) {
			((MultiBlockTile) te).form();
			if(te instanceof PartTile) {
				parts.add((PartTile) te);
			}
		}
	}
	
	protected void unformBlock(BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof MultiBlockTile) {
			((MultiBlockTile) te).unform();
		}
	}
	
	protected abstract Coord3 getMultiBlockSize();

	public void formMultiBlock(PlayerEntity player, Set<BlockPos> erroredPos) {
		if (this.listeners == null) {
			this.listeners = new HashSet<PlayerEntity>();
		}
		this.listeners.add(player);
		if (!erroredPos.isEmpty()) {
			for (PlayerEntity playerEntity : listeners) {
				playerEntity.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "" + erroredPos.size() + " block(s) aren't in the right place : \n" + erroredPos), false);
			}
			return;
		} else {
			for (PlayerEntity playerEntity : listeners) {
				playerEntity.sendStatusMessage(new StringTextComponent(TextFormatting.YELLOW + "No errors were detected, assembling the reactor..."), false);
			}
		}
		if(this.mode != FormMode.UNFORM) {
			this.mode = FormMode.FORM;
			this.formPos = Coord3.NULL_COORDS;
		}
	}
	
	public void unformMultiBlock(PlayerEntity player, Set<BlockPos> erroredPos) {
		if (this.listeners == null) {
			this.listeners = new HashSet<PlayerEntity>();
		}
		this.listeners.add(player);
		if (!erroredPos.isEmpty()) {
			for (PlayerEntity playerEntity : listeners) {
				playerEntity.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "" + erroredPos.size() + " block(s) aren't in the right place : \n" + erroredPos), false);
			}
			return;
		} else {
			for (PlayerEntity playerEntity : listeners) {
				playerEntity.sendStatusMessage(new StringTextComponent(TextFormatting.YELLOW + "No errors were detected, disassembling the reactor..."), false);
			}
		}
		if(this.mode != FormMode.FORM) {
			this.mode = FormMode.UNFORM;
			this.formPos = Coord3.NULL_COORDS;
		}
	}
	
	protected abstract boolean hasFinishedFormation();
	
	public abstract int getFormPct();
	
	public enum FormMode {
		FORM,
		UNFORM,
		NONE;
	}
	
}
