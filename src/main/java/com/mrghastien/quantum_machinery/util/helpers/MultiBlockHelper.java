package com.mrghastien.quantum_machinery.util.helpers;

import java.util.Set;

import com.mrghastien.quantum_machinery.common.multiblocks.ControllerTile;
import com.mrghastien.quantum_machinery.common.multiblocks.IMultiBlock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class MultiBlockHelper {

	public static Set<BlockPos> breakMultiBlock(Direction facing, IMultiBlock type, World world, BlockPos pos, PlayerEntity player) {
		BlockPos cPos = type.getControllerRelativePos(facing);
		if (cPos != null) {
			BlockPos p = pos.subtract(cPos);
			Set<BlockPos> erroredPos = type.isValidFormedMultiBlock(facing, world, p);
			if (erroredPos.isEmpty()) {
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof ControllerTile) {
					((ControllerTile)te).unformMultiBlock(player, erroredPos);
				}
				return erroredPos;
			} else {
				player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "" + erroredPos.size()
						+ " block(s) aren't in the right place :" + erroredPos), false);
				return erroredPos;
			}
		} else {
			return null;
		}
	}
	
	public static Set<BlockPos> formMultiBlock(Direction facing, IMultiBlock type, World world, BlockPos pos, PlayerEntity player) {
		BlockPos cPos = type.getControllerRelativePos(facing);
		if (cPos != null) {
			BlockPos p = pos.subtract(cPos);
			Set<BlockPos> erroredPos = type.isValidUnformedMultiBlock(facing, world, p);
			if (erroredPos.isEmpty()) {
				TileEntity te = world.getTileEntity(pos);
				if (te instanceof ControllerTile) {
					((ControllerTile)te).formMultiBlock(player, erroredPos);
				}
				return erroredPos;
			} else {
				player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "" + erroredPos.size() + " block(s) aren't in the right place :" + erroredPos), false);
				return erroredPos;
			}
		} else {
			return null;
		}
	}
}
