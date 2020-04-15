package com.mrghastien.quantum_machinery.blocks;

import java.util.List;

import com.mrghastien.quantum_machinery.tileentities.CableTile;
import com.mrghastien.quantum_machinery.util.helpers.BlockHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CableBlock extends Block implements IConnectable{
	
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
	public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return 0;
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		
		//worldIn.setBlockState(currentPos, stateIn, 2);
		//ATTENTION : Lors de la modification d'un BlockState, toutes les propriétés doivent être modifiées en même temps. 
		return refreshConnections(currentPos, (World)worldIn);
	}
	
	@Override
	public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
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
			worldIn.setBlockState(pos, state1, 3);
			BlockHelper.requestAllSidesConnect(pos, worldIn);
			for (Direction facing : Direction.values()) {
				state1 = this.tryConnect(pos.offset(facing), worldIn.getBlockState(pos.offset(facing)), pos, state1, facing, worldIn);
			}
			worldIn.setBlockState(pos, state1);
		}
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockHelper.requestAllSidesDisconnect(pos, worldIn);
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@Override
	public boolean hasTileEntity() {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new CableTile();
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(NORTH, SOUTH, WEST, EAST, UP, DOWN);
	}
	
	@Override
	public BlockState tryConnect(BlockPos requestingPos, BlockState requestingState, BlockPos pos, BlockState state, Direction facing, World worldIn) {

		boolean north = false;
		boolean south = false;
		boolean west = false;
		boolean east = false;
		boolean up = false;
		boolean down = false;

		if (state.getBlock() instanceof IConnectable) {

			north = state.get(NORTH);
			south = state.get(SOUTH);
			west = state.get(WEST);
			east = state.get(EAST);
			up = state.get(UP);
			down = state.get(DOWN);

			if (requestingState.getBlock() instanceof IConnectable) {

				switch (facing) {
				case NORTH:
					if (!north) {
						north = true;
					}
					break;

				case SOUTH:
					if (!south) {
						south = true;
					}
					break;

				case EAST:
					if (!east) {
						east = true;
					}
					break;

				case WEST:
					if (!west) {
						west = true;
					}
					break;

				case UP:
					if (!up) {
						up = true;
					}
					break;

				case DOWN:
					if (!down) {
						down = true;
					}
					break;
				default:
					break;
				}
			}
		}
		state = state.with(NORTH, north).with(SOUTH, south).with(EAST, east).with(WEST, west).with(UP, up)
				.with(DOWN, down);
		return state;
	}

	@Override
	public BlockState tryDisconnect(BlockPos requestingPos, BlockState requestingState, BlockPos pos, BlockState state, Direction facing, World worldIn) {

		boolean north = false;
		boolean south = false;
		boolean west = false;
		boolean east = false;
		boolean up = false;
		boolean down = false;

		if (state.getBlock() instanceof IConnectable) {

			north = state.get(NORTH);
			south = state.get(SOUTH);
			west = state.get(WEST);
			east = state.get(EAST);
			up = state.get(UP);
			down = state.get(DOWN);

			switch (facing) {
			case NORTH:
				if (north) {
					north = false;
				}
				break;

			case SOUTH:
				if (south) {
					south = false;
				}
				break;

			case EAST:
				if (east) {
					east = false;
				}
				break;

			case WEST:
				if (west) {
					west = false;
				}
				break;

			case UP:
				if (up) {
					up = false;
				}
				break;

			case DOWN:
				if (down) {
					down = false;
				}
				break;
			default:
				break;
			}
		}
		state = state.with(NORTH, north).with(SOUTH, south).with(EAST, east).with(WEST, west).with(UP, up)
				.with(DOWN, down);
		return state;
	}

	@Override
	public boolean hasConnections(BlockPos pos, World world) {
		BlockState state = world.getBlockState(pos);
		return state.get(NORTH) || state.get(SOUTH) || state.get(EAST) || state.get(WEST) || state.get(UP) || state.get(DOWN);
	}

	@Override
	public boolean isConnected(Direction facing, BlockPos pos, World world) {
		BlockState state = world.getBlockState(pos);
		switch (facing) {
		case NORTH:
			return state.get(BlockStateProperties.NORTH);
			
		case SOUTH:
			return state.get(BlockStateProperties.SOUTH);
			
		case EAST:
			return state.get(BlockStateProperties.EAST);
			
		case WEST:
			return state.get(BlockStateProperties.WEST);
			
		case UP:
			return state.get(BlockStateProperties.UP);
			
		case DOWN:
			return state.get(BlockStateProperties.DOWN);

		default:
			return false;
		}
	}
	
	@Override
	public BlockState refreshConnections(BlockPos pos, World worldIn) {
		BlockState state = worldIn.getBlockState(pos);
		for (Direction direction : Direction.values()) {
			BlockPos requestingPos = pos.offset(direction);
			state = this.tryDisconnect(requestingPos, worldIn.getBlockState(requestingPos), pos, state, direction, worldIn);
			state = this.tryConnect(requestingPos, worldIn.getBlockState(requestingPos), pos, state, direction, worldIn);
			
		}
		return state;
	}
}
