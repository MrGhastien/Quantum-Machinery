package com.mrghastien.quantum_machinery.blocks;

import com.mrghastien.quantum_machinery.util.helpers.BlockHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
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
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public abstract class MachineBlock extends Block {
	
	protected TileEntity tileentity;

	public MachineBlock() {
		this(Properties.create(Material.IRON)
				.sound(SoundType.METAL)
				.hardnessAndResistance(5.0f)
		);
	}
	
	public MachineBlock(Properties builder) {
		super(builder);
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		super.onBlockHarvested(worldIn, pos, state, player);
		BlockHelper.requestAllSidesDisconnect(pos, worldIn);
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
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if(placer != null) {
			worldIn.setBlockState(pos, state.with(BlockStateProperties.FACING, getFacingFromEntity(pos, placer)), 3);
			BlockHelper.requestAllSidesConnect(pos, worldIn);
		}
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		BlockHelper.requestAllSidesRefresh(currentPos, (World)worldIn);
		return stateIn;
	}
	
	public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity) {
		return Direction.getFacingFromVector((float) (entity.getPosX() - clickedBlock.getX()), (float) 0, (float) (entity.getPosZ() - clickedBlock.getZ()));
	}	

	@Override
	protected abstract void fillStateContainer(Builder<Block, BlockState> builder);
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

}
