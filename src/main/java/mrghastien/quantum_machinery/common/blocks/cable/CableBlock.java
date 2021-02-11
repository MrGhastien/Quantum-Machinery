package mrghastien.quantum_machinery.common.blocks.cable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
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
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.EnumMap;
import java.util.List;

import mrghastien.quantum_machinery.api.common.ConnectionType;
import mrghastien.quantum_machinery.common.energynet.EnergyNetworkHandler;
import mrghastien.quantum_machinery.common.init.ModTileEntities;

public class CableBlock extends Block {
	
	   public static final EnumProperty<ConnectionType> NORTH = EnumProperty.create("north", ConnectionType.class);
	   public static final EnumProperty<ConnectionType> EAST = EnumProperty.create("east", ConnectionType.class);
	   public static final EnumProperty<ConnectionType> SOUTH = EnumProperty.create("south", ConnectionType.class);
	   public static final EnumProperty<ConnectionType> WEST = EnumProperty.create("west", ConnectionType.class);
	   public static final EnumProperty<ConnectionType> UP = EnumProperty.create("up", ConnectionType.class);
	   public static final EnumProperty<ConnectionType> DOWN = EnumProperty.create("down", ConnectionType.class);
	   public static final EnumMap<Direction, EnumProperty<ConnectionType>> PROPERTY_MAP = Util.make(new EnumMap<>(Direction.class), map -> {
		   map.put(Direction.NORTH, NORTH);
		   map.put(Direction.EAST, EAST);
		   map.put(Direction.SOUTH, SOUTH);
		   map.put(Direction.WEST, WEST);
		   map.put(Direction.UP, UP);
		   map.put(Direction.DOWN, DOWN);
	   });
	   
	   public static final VoxelShape CENTER_BOX = Block.makeCuboidShape(5, 5, 5, 11, 11, 11);
	   public static final VoxelShape ARM_DOWN = Block.makeCuboidShape(5, 0, 5, 11, 5, 11);
	   public static final VoxelShape ARM_UP = Block.makeCuboidShape(5, 11, 5, 11, 16, 11);
	   public static final VoxelShape ARM_NORTH = Block.makeCuboidShape(5, 5, 0, 11, 11, 5);
	   public static final VoxelShape ARM_SOUTH = Block.makeCuboidShape(5, 5, 11, 11, 11, 16);
	   public static final VoxelShape ARM_EAST = Block.makeCuboidShape(11, 5, 5, 16, 11, 11);
	   public static final VoxelShape ARM_WEST = Block.makeCuboidShape(0, 5, 5, 5, 11, 11);

	   public final int transferRate;
	   
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

	// Fonction pour déterminer la boîte de collison d'un bloc
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {

		VoxelShape shape = CENTER_BOX;

		if (state.get(DOWN) != ConnectionType.NONE) {
			shape = VoxelShapes.or(shape, ARM_DOWN);
		}
		if (state.get(UP) != ConnectionType.NONE) {
			shape = VoxelShapes.or(shape, ARM_UP);
		}
		if (state.get(EAST) != ConnectionType.NONE) {
			shape = VoxelShapes.or(shape, ARM_EAST);
		}
		if (state.get(WEST) != ConnectionType.NONE) {
			shape = VoxelShapes.or(shape, ARM_WEST);
		}
		if (state.get(NORTH) != ConnectionType.NONE) {
			shape = VoxelShapes.or(shape, ARM_NORTH);
		}
		if (state.get(SOUTH) != ConnectionType.NONE) {
			shape = VoxelShapes.or(shape, ARM_SOUTH);
		}
		return shape;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		if(placer != null && !worldIn.isRemote) {
			BlockState state1 = state.with(NORTH, createConnection(worldIn, pos, Direction.NORTH, ConnectionType.NONE))
					.with(SOUTH, createConnection(worldIn, pos, Direction.SOUTH, ConnectionType.NONE))
					.with(EAST, createConnection(worldIn, pos, Direction.EAST, ConnectionType.NONE))
					.with(WEST, createConnection(worldIn, pos, Direction.WEST, ConnectionType.NONE))
					.with(UP, createConnection(worldIn, pos, Direction.UP, ConnectionType.NONE))
					.with(DOWN, createConnection(worldIn, pos, Direction.DOWN, ConnectionType.NONE));
			worldIn.setBlockState(pos, state1);
		}
	}
	
	private static ConnectionType createConnection(IBlockReader worldIn, BlockPos pos, Direction side, ConnectionType current) {
        TileEntity tileEntity = worldIn.getTileEntity(pos.offset(side));
        if (tileEntity instanceof CableTile) {
            return ConnectionType.BOTH;
        } else if (tileEntity != null) {
            IEnergyStorage energy = tileEntity.getCapability(CapabilityEnergy.ENERGY, side.getOpposite()).orElse(null);
            if (energy != null) {
                if (energy.canExtract()) {
                    return current == ConnectionType.NONE ? ConnectionType.IN : current;
                } else if (energy.canReceive()) {
                    return current == ConnectionType.NONE ? ConnectionType.OUT : current;
                }
            }
        }
        return ConnectionType.NONE;
    }
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		if(worldIn.isRemote())
			return stateIn;
		if (worldIn.getTileEntity(facingPos) instanceof CableTile)
            EnergyNetworkHandler.INSTANCE.invalidateNetwork(currentPos, (World)worldIn);
		
		EnumProperty<ConnectionType> property = PROPERTY_MAP.get(facing);
		ConnectionType type = stateIn.get(property);
		return stateIn.with(property, createConnection(worldIn, currentPos, facing, type));
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
	
	public static ConnectionType getConnection(BlockState state, Direction side) {
        return state.get(PROPERTY_MAP.get(side));
    }
}
