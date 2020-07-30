package com.mrghastien.quantum_machinery.common.multiblocks.fission.blocks;

import com.mrghastien.quantum_machinery.common.init.ModItems;
import com.mrghastien.quantum_machinery.common.multiblocks.ControllerTile;
import com.mrghastien.quantum_machinery.common.multiblocks.fission.FissionMultiBlock;
import com.mrghastien.quantum_machinery.common.multiblocks.fission.tileentities.FissionControllerTile;
import com.mrghastien.quantum_machinery.util.helpers.MultiBlockHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class FissionControllerBlock extends Block{

	public FissionControllerBlock() {
		super(Properties.create(Material.IRON)
				.hardnessAndResistance(5.0f)
				.sound(SoundType.METAL));
		this.setDefaultState(this.stateContainer.getBaseState().with(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if (placer != null) {
			worldIn.setBlockState(pos, state.with(BlockStateProperties.HORIZONTAL_FACING, getFacingFromEntity(pos, placer)));
		}
	}
	
	public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity) {
		return Direction.getFacingFromVector((float) (entity.getPosX() - clickedBlock.getX()), 0, (float) (entity.getPosZ() - clickedBlock.getZ()));
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
		  if (player.getHeldItem(handIn).getItem() == ModItems.WRENCH.get()) {
	            toggleMultiBlock(worldIn, pos, state, player);
	            return ActionResultType.SUCCESS;
	        }
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
		if (!worldIn.isRemote && ((FissionControllerTile)worldIn.getTileEntity(pos)).isFormed()) {
			MultiBlockHelper.breakMultiBlock(state.get(BlockStateProperties.HORIZONTAL_FACING), FissionMultiBlock.INSTANCE, worldIn, pos, player);
		}
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	public static void toggleMultiBlock(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isRemote) {
			boolean form = ((ControllerTile)world.getTileEntity(pos)).isFormed();
			if (!form) {
					MultiBlockHelper.formMultiBlock(state.get(BlockStateProperties.HORIZONTAL_FACING), FissionMultiBlock.INSTANCE, world, pos, player);
				} else {
					MultiBlockHelper.breakMultiBlock(state.get(BlockStateProperties.HORIZONTAL_FACING), FissionMultiBlock.INSTANCE, world, pos, player);
				}
			}
	}


	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new FissionControllerTile();
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
}
