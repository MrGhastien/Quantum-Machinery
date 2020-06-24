package com.mrghastien.quantum_machinery.common.multiblocks.fission.blocks;

import com.mrghastien.quantum_machinery.common.multiblocks.fission.FissionMultiBlock;
import com.mrghastien.quantum_machinery.common.multiblocks.fission.tileentities.FissionEOTile;
import com.mrghastien.quantum_machinery.util.helpers.MultiBlockHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class FissionEOBlock extends FissionPartBlock {

	public FissionEOBlock() {
		super();
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		if(!worldIn.isRemote) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if(tileentity instanceof INamedContainerProvider) {
				NetworkHooks.openGui((ServerPlayerEntity)player, (INamedContainerProvider) tileentity, tileentity.getPos());
			}
		}
		return ActionResultType.SUCCESS;
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!worldIn.isRemote) {
			MultiBlockHelper.breakMultiBlock(worldIn.getBlockState(((FissionEOTile)worldIn.getTileEntity(pos)).getControllerPos()).get(BlockStateProperties.HORIZONTAL_FACING), FissionMultiBlock.INSTANCE, worldIn, pos, null);
		}
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new FissionEOTile();
	}

}
