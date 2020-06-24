package com.mrghastien.quantum_machinery.common.blocks.cable;

import java.util.List;

import com.mrghastien.quantum_machinery.common.init.ModTileEntities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CableBlock extends Block {
	
	   public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
	   public static final BooleanProperty EAST = BlockStateProperties.EAST;
	   public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
	   public static final BooleanProperty WEST = BlockStateProperties.WEST;
	   public static final BooleanProperty UP = BlockStateProperties.UP;
	   public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
	   
	   public static final VoxelShape CENTER_BOX = Block.makeCuboidShape(5, 5, 5, 11, 11, 11);
	   public static final VoxelShape ARM_DOWN = Block.makeCuboidShape(5, 0, 5, 11, 5, 11);
	   public static final VoxelShape ARM_UP = Block.makeCuboidShape(5, 11, 5, 11, 16, 11);
	   public static final VoxelShape ARM_NORTH = Block.makeCuboidShape(5, 5, 0, 11, 11, 5);
	   public static final VoxelShape ARM_SOUTH = Block.makeCuboidShape(5, 5, 11, 11, 11, 16);
	   public static final VoxelShape ARM_EAST = Block.makeCuboidShape(11, 5, 5, 16, 11, 11);
	   public static final VoxelShape ARM_WEST = Block.makeCuboidShape(0, 5, 5, 5, 11, 11);

	   public int transferRate;
	   
	public CableBlock(int transferRate) {
		super(Properties.create(Material.IRON)
				.sound(SoundType.METAL)
				.hardnessAndResistance(5.0f)
				.notSolid());
		this.transferRate = transferRate;
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		tooltip.add(new StringTextComponent(TextFormatting.DARK_GRAY + "Used to transfer power with connectors."));
	}
	    //Fonction pour déterminer la boîte de collison d'un bloc
		@Override
		public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
			
			VoxelShape shape = CENTER_BOX;
			
			if (state.get(DOWN)) {
				shape = VoxelShapes.or(shape, ARM_DOWN);
			}
			if (state.get(UP)) {
				shape = VoxelShapes.or(shape, ARM_UP);
			}
			if (state.get(EAST)) {
				shape = VoxelShapes.or(shape, ARM_EAST);
			}
			if (state.get(WEST)) {
				shape = VoxelShapes.or(shape, ARM_WEST);
			}
			if (state.get(NORTH)) {
				shape = VoxelShapes.or(shape, ARM_NORTH);
			}
			if (state.get(SOUTH)) {
				shape = VoxelShapes.or(shape, ARM_SOUTH);
			}
			return shape;
		}
	
	@Override
	public boolean causesSuffocation(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if(placer != null) {
			BlockState state1 = state.with(NORTH, false)
					.with(SOUTH, false)
					.with(EAST, false)
					.with(WEST, false)
					.with(UP, false)
					.with(DOWN, false);
			worldIn.setBlockState(pos, state1);
		}
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return ModTileEntities.CABLE.get().create();
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(NORTH, SOUTH, WEST, EAST, UP, DOWN);
	}
}
