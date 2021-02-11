package mrghastien.quantum_machinery.common.blocks.machines;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkHooks;

import mrghastien.quantum_machinery.common.blocks.BaseBlock;
import mrghastien.quantum_machinery.util.BlockHelper;

public abstract class MachineBlock extends BaseBlock {

	protected RegistryObject<TileEntityType<?>> tile;
	
	public MachineBlock(RegistryObject<TileEntityType<?>> tile, Properties builder) {
		super(builder);
		this.tile = tile;
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.FACING, 
				BlockStateProperties.LIT);
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
		BlockHelper.dropInventoryItems(worldIn, pos);
		super.onBlockHarvested(worldIn, pos, state, player);
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return tile.get().create();
	}
}
