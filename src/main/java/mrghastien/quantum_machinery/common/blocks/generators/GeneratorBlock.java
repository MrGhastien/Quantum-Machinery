package mrghastien.quantum_machinery.common.blocks.generators;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import mrghastien.quantum_machinery.common.blocks.BaseBlock;

public abstract class GeneratorBlock extends BaseBlock {

	protected RegistryObject<TileEntityType<?>> tile;
	
	public GeneratorBlock(RegistryObject<TileEntityType<?>> tile) {
		super();
		this.tile = tile;
	}

	public GeneratorBlock(RegistryObject<TileEntityType<?>> tile, Properties builder) {
		super(builder);
		this.tile = tile;
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.FACING, 
				BlockStateProperties.LIT);
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		if(tileentity != null) {
			LazyOptional<IItemHandler> handler = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
			handler.ifPresent(h -> {
				for (int s = 0; s < h.getSlots(); s++) {
					ItemStack stack = h.getStackInSlot(s);
					NonNullList<ItemStack> itemsToDrop = NonNullList.withSize(1, stack);
					InventoryHelper.dropItems(worldIn, pos, itemsToDrop);
				}
			});
			super.onBlockHarvested(worldIn, pos, state, player);
		}
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
