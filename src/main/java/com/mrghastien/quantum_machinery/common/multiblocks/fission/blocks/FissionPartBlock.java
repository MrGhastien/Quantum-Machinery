package com.mrghastien.quantum_machinery.common.multiblocks.fission.blocks;

import com.mrghastien.quantum_machinery.common.multiblocks.fission.FissionMultiBlock;
import com.mrghastien.quantum_machinery.common.multiblocks.fission.tileentities.FissionPartTile;
import com.mrghastien.quantum_machinery.util.helpers.MultiBlockHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class FissionPartBlock extends Block {

	public FissionPartBlock() {
		super(Properties.create(Material.IRON)
				.hardnessAndResistance(5.0f)
				.sound(SoundType.METAL)
		);
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new FissionPartTile();
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	/*@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		if (player.getHeldItem(handIn).getItem() == Items.STICK) {
            FissionReactorBlock.toggleMultiBlock(worldIn, pos, state, player);
            return true;
        }
        // Only work if the block is formed
        if (state.getBlock() instanceof FissionReactorPartBlock && state.get(FORMATION) != FissionReactorPartIndex.UNFORMED) {
            // Find the controller
            BlockPos controllerPos = FissionReactorBlock.getControllerPos(worldIn, pos);
            if (controllerPos != null) {
                BlockState controllerState = worldIn.getBlockState(controllerPos);
                return controllerState.getBlock().onBlockActivated(state, worldIn, pos, player, handIn, hit);
            }
        }
        return false;
	}*/
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!worldIn.isRemote) {
			if (((FissionPartTile)worldIn.getTileEntity(pos)).isFormed()) {
				BlockPos p = ((FissionPartTile)worldIn.getTileEntity(pos)).getMaster().getPos();
				if (p != null) {
					MultiBlockHelper.breakMultiBlock(worldIn.getBlockState(p).get(BlockStateProperties.HORIZONTAL_FACING), FissionMultiBlock.INSTANCE, worldIn, pos, player);
				}
			}
		}
		super.onBlockHarvested(worldIn, pos, state, player);
	}
}