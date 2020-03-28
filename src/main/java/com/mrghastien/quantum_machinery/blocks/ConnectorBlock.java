package com.mrghastien.quantum_machinery.blocks;

import static com.mrghastien.quantum_machinery.init.ModStateProperties.DOWN;
import static com.mrghastien.quantum_machinery.init.ModStateProperties.EAST;
import static com.mrghastien.quantum_machinery.init.ModStateProperties.NORTH;
import static com.mrghastien.quantum_machinery.init.ModStateProperties.SOUTH;
import static com.mrghastien.quantum_machinery.init.ModStateProperties.UP;
import static com.mrghastien.quantum_machinery.init.ModStateProperties.WEST;

import java.util.List;

import com.mrghastien.quantum_machinery.init.ModStateProperties;
import com.mrghastien.quantum_machinery.tileentities.ConnectorTile;
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
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
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

public class ConnectorBlock extends Block implements IConnectable {

	public static final EnumProperty<EnergyTransfer> ENERGY_HANDLING_MODE = EnumProperty.create("energy_transfer_mode", EnergyTransfer.class);
	
	public static final VoxelShape CENTER_BOX = Block.makeCuboidShape(5, 5, 5, 11, 11, 11);
	public static final VoxelShape ARM_DOWN = Block.makeCuboidShape(5, 0, 5, 11, 5, 11);
	public static final VoxelShape ARM_UP = Block.makeCuboidShape(5, 11, 5, 11, 16, 11);
	public static final VoxelShape ARM_NORTH = Block.makeCuboidShape(5, 5, 0, 11, 11, 5);
	public static final VoxelShape ARM_SOUTH = Block.makeCuboidShape(5, 5, 11, 11, 11, 16);
	public static final VoxelShape ARM_EAST = Block.makeCuboidShape(11, 5, 5, 16, 11, 11);
	public static final VoxelShape ARM_WEST = Block.makeCuboidShape(0, 5, 5, 5, 11, 11);
	
	private int transferRate;
	
	public int getTransferRate() {
		return transferRate;
	}

	public ConnectorBlock(int transferRate) {
		super(Properties.create(Material.IRON)
				.sound(SoundType.METAL)
				.hardnessAndResistance(5.0f)
				.notSolid()
		);
		this.transferRate = transferRate;
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockHelper.requestAllSidesDisconnect(pos, worldIn);
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		tooltip.add(new StringTextComponent(TextFormatting.DARK_GRAY + "Used to transfer power one point to another."));
		tooltip.add(new StringTextComponent(TextFormatting.DARK_GRAY + "It has to be connected to a cable in order to send energy."));
		tooltip.add(new StringTextComponent(TextFormatting.YELLOW + "Right click with a wrench to change the transfer mode."));
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if(placer != null) {
			worldIn.setBlockState(pos, state.with(ENERGY_HANDLING_MODE, EnergyTransfer.SENDING)
					.with(NORTH, SideConnection.NONE)
					.with(SOUTH, SideConnection.NONE)
					.with(EAST, SideConnection.NONE)
					.with(WEST, SideConnection.NONE)
					.with(UP, SideConnection.NONE)
					.with(DOWN, SideConnection.NONE), 3);
			BlockHelper.requestAllSidesConnect(pos, worldIn);
			BlockState state1 = state;
			for (Direction facing : Direction.values()) {
				state1 = this.tryConnect(pos.offset(facing), worldIn.getBlockState(pos.offset(facing)), pos, state1, facing, worldIn);
			}
			worldIn.setBlockState(pos, state1);
		}
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		
		VoxelShape shape = CENTER_BOX;
		
		if (state.get(DOWN) == SideConnection.CABLE || state.get(DOWN) == SideConnection.MACHINE) {
			shape = VoxelShapes.or(shape, ARM_DOWN);
		}
		if (state.get(UP) == SideConnection.CABLE || state.get(UP) == SideConnection.MACHINE) {
			shape = VoxelShapes.or(shape, ARM_UP);
		}
		if (state.get(EAST) == SideConnection.CABLE || state.get(EAST) == SideConnection.MACHINE) {
			shape = VoxelShapes.or(shape, ARM_EAST);
		}
		if (state.get(WEST) == SideConnection.CABLE || state.get(WEST) == SideConnection.MACHINE) {
			shape = VoxelShapes.or(shape, ARM_WEST);
		}
		if (state.get(NORTH) == SideConnection.CABLE || state.get(NORTH) == SideConnection.MACHINE) {
			shape = VoxelShapes.or(shape, ARM_NORTH);
		}
		if (state.get(SOUTH) == SideConnection.CABLE || state.get(SOUTH) == SideConnection.MACHINE) {
			shape = VoxelShapes.or(shape, ARM_SOUTH);
		}
		return shape;
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		
		//worldIn.setBlockState(currentPos, stateIn, 2);
		//ATTENTION : Lors de la modification d'un BlockState, toutes les propriétés doivent être modifiées en même temps. 
		return refreshConnections(currentPos, (World)worldIn);
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(ENERGY_HANDLING_MODE, NORTH, SOUTH, WEST, EAST, UP, DOWN);
	}
	
	@Override
	public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return false;
	}
	
	@Override
	public boolean hasTileEntity() {
		return true;
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
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ConnectorTile();
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
	
	public BlockState tryConnect(BlockPos requestingPos, BlockState requestingState, BlockPos pos, BlockState state, Direction facing, World worldIn) {

		BlockState stateIn = worldIn.getBlockState(pos);

		SideConnection north = SideConnection.NONE;
		SideConnection south = SideConnection.NONE;
		SideConnection west = SideConnection.NONE;
		SideConnection east = SideConnection.NONE;
		SideConnection up = SideConnection.NONE;
		SideConnection down = SideConnection.NONE;

		if (stateIn.getBlock() instanceof IConnectable) {

			north = stateIn.get(NORTH);
			south = stateIn.get(SOUTH);
			west = stateIn.get(WEST);
			east = stateIn.get(EAST);
			up = stateIn.get(UP);
			down = stateIn.get(DOWN);

			if (BlockHelper.canBlockHandleEnergy(requestingPos, facing.getOpposite(), worldIn) && !(requestingState.getBlock() instanceof ConnectorBlock)
					|| requestingState.getBlock() instanceof CableBlock) {
				// Les constantes d'énumération après le mot clé "case" proviennent de l'enum net.minecraft.util.Direction
				// Les autres proviennent de com.mrghastien.astralmanipulation.blocks.ConnectorBlock.SideConnection.
				switch (facing) {
				case NORTH: // <==== net.minecraft.util.Direction.NORTH
					if (worldIn.getBlockState(pos).get(NORTH) == SideConnection.NONE) {// <==== com.mrghastien.astralmanipulation.blocks.ConnectorBlock.SideConnection.NORTH
						if (BlockHelper.canBlockHandleEnergy(pos, null, worldIn)) {
							north = SideConnection.MACHINE;
						} else if (requestingState.getBlock() instanceof CableBlock) {
							north = SideConnection.CABLE;
						}
					}
					break;

				case SOUTH:
					if (worldIn.getBlockState(pos).get(SOUTH) == SideConnection.NONE) {
						if (BlockHelper.canBlockHandleEnergy(pos, null, worldIn)) {
							south = SideConnection.MACHINE;
						} else if (requestingState.getBlock() instanceof CableBlock) {
							south = SideConnection.CABLE;
						}
					}
					break;

				case EAST:
					if (worldIn.getBlockState(pos).get(EAST) == SideConnection.NONE) {
						if (BlockHelper.canBlockHandleEnergy(pos, null, worldIn)) {
							east = SideConnection.MACHINE;
						} else if (requestingState.getBlock() instanceof CableBlock) {
							east = SideConnection.CABLE;
						}
					}
					break;

				case WEST:
					if (worldIn.getBlockState(pos).get(WEST) == SideConnection.NONE) {
						if (BlockHelper.canBlockHandleEnergy(pos, null, worldIn)) {
							west = SideConnection.MACHINE;
						} else if (requestingState.getBlock() instanceof CableBlock) {
							west = SideConnection.CABLE;
						}
					}
					break;

				case UP:
					if (worldIn.getBlockState(pos).get(UP) == SideConnection.NONE) {
						if (BlockHelper.canBlockHandleEnergy(pos, null, worldIn)) {
							up = SideConnection.MACHINE;
						} else if (requestingState.getBlock() instanceof CableBlock) {
							up = SideConnection.CABLE;
						}
					}
					break;

				case DOWN:
					if (worldIn.getBlockState(pos).get(DOWN) == SideConnection.NONE) {
						if (BlockHelper.canBlockHandleEnergy(pos, null, worldIn)) {
							down = SideConnection.MACHINE;
						} else if (requestingState.getBlock() instanceof CableBlock) {
							down = SideConnection.CABLE;
						}
					}
					break;
				default:
					break;
				}
			}
		}
		stateIn = stateIn.with(NORTH, north).with(SOUTH, south).with(EAST, east).with(WEST, west).with(UP, up)
				.with(DOWN, down);
		return stateIn;
	}

	public BlockState tryDisconnect(BlockPos requestingPos, BlockState requestingState, BlockPos pos, BlockState state, Direction facing, World worldIn) {
		BlockState stateIn = worldIn.getBlockState(pos);

		SideConnection north = SideConnection.NONE;
		SideConnection south = SideConnection.NONE;
		SideConnection west = SideConnection.NONE;
		SideConnection east = SideConnection.NONE;
		SideConnection up = SideConnection.NONE;
		SideConnection down = SideConnection.NONE;

		if (stateIn.getBlock() instanceof IConnectable) {

			north = stateIn.get(NORTH);
			south = stateIn.get(SOUTH);
			west = stateIn.get(WEST);
			east = stateIn.get(EAST);
			up = stateIn.get(UP);
			down = stateIn.get(DOWN);
			// Les constantes d'énumération à pres le mot clé "case" proviennent de l'enum net.minecraft.util.Direction
			// Les autres proviennent de com.mrghastien.astralmanipulation.blocks.ConnectorBlock.SideConnection.
			if (!BlockHelper.canBlockHandleEnergy(requestingPos, facing.getOpposite(), worldIn)  && !(requestingState.getBlock() instanceof ConnectorBlock)
					&& !(requestingState.getBlock() instanceof CableBlock)) {
				switch (facing) {
				case NORTH: // <==== net.minecraft.util.Direction.NORTH
					if (worldIn.getBlockState(pos).get(NORTH) != SideConnection.NONE) {// <==== com.mrghastien.astralmanipulation.blocks.ConnectorBlock.SideConnection.NORTH
						north = SideConnection.NONE;
					}
					break;

				case SOUTH:
					if (worldIn.getBlockState(pos).get(SOUTH) != SideConnection.NONE) {
						south = SideConnection.NONE;
					}
					break;

				case EAST:
					if (worldIn.getBlockState(pos).get(EAST) != SideConnection.NONE) {
						east = SideConnection.NONE;
					}
					break;

				case WEST:
					if (worldIn.getBlockState(pos).get(WEST) != SideConnection.NONE) {
						west = SideConnection.NONE;
					}
					break;

				case UP:
					if (worldIn.getBlockState(pos).get(UP) != SideConnection.NONE) {
						up = SideConnection.NONE;
					}
					break;

				case DOWN:
					if (worldIn.getBlockState(pos).get(DOWN) != SideConnection.NONE) {
						down = SideConnection.NONE;
					}
					break;
				default:
					break;
				}
			}
		}
		stateIn = stateIn.with(NORTH, north).with(SOUTH, south).with(EAST, east).with(WEST, west).with(UP, up)
				.with(DOWN, down);
		return stateIn;
	}

	@Override
	public boolean hasConnections(BlockPos pos, World world) {
		BlockState state = world.getBlockState(pos);
		return state.get(NORTH) != SideConnection.NONE 
				|| state.get(SOUTH) != SideConnection.NONE 
				|| state.get(EAST) != SideConnection.NONE 
				|| state.get(WEST) != SideConnection.NONE
				|| state.get(UP) != SideConnection.NONE 
				|| state.get(DOWN) != SideConnection.NONE;
	}

	@Override
	public boolean isConnected(Direction facing, BlockPos pos, World world) {
		BlockState state = world.getBlockState(pos);

		switch (facing) {
		case NORTH:
			if (state.get(ModStateProperties.NORTH) != SideConnection.NONE) {
				return true;
			}
			return false;
		case SOUTH:
			if (state.get(ModStateProperties.SOUTH) != SideConnection.NONE) {
				return true;
			}
			return false;
		case EAST:
			if (state.get(ModStateProperties.EAST) != SideConnection.NONE) {
				return true;
			}
			return false;
		case WEST:
			if (state.get(ModStateProperties.WEST) != SideConnection.NONE) {
				return true;
			}
			return false;
		case UP:
			if (state.get(ModStateProperties.UP) != SideConnection.NONE) {
				return true;
			}
			return false;
		case DOWN:
			if (state.get(ModStateProperties.DOWN) != SideConnection.NONE) {
				return true;
			}
			return false;

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
