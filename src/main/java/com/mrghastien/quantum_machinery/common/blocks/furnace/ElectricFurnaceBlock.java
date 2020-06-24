package com.mrghastien.quantum_machinery.common.blocks.furnace;

import com.mrghastien.quantum_machinery.common.blocks.MachineBlock;
import com.mrghastien.quantum_machinery.common.init.ModTileEntities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class ElectricFurnaceBlock extends MachineBlock {

	public ElectricFurnaceBlock() {
		super(ModTileEntities.ELECTRIC_FURNACE, Properties.create(Material.IRON)
				.sound(SoundType.METAL)
				.hardnessAndResistance(5.0f)
				.lightValue(15)
		);
		this.setDefaultState(this.stateContainer.getBaseState().with(BlockStateProperties.FACING, Direction.NORTH)
				.with(BlockStateProperties.POWERED, false)
		);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int getLightValue(BlockState state) {
		return state.get(BlockStateProperties.POWERED) ? super.getLightValue(state) : 0;
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		ElectricFurnaceTile tileentity = (ElectricFurnaceTile)worldIn.getTileEntity(pos);
		if(tileentity instanceof ElectricFurnaceTile && tileentity != null) {
			tileentity.getHandler().ifPresent(h -> {
				for(int s = 0; s < h.getSlots(); s++) {
				ItemStack stack = h.getStackInSlot(s);
				NonNullList<ItemStack> itemsToDrop = NonNullList.withSize(1, stack); 
				InventoryHelper.dropItems(worldIn, pos, itemsToDrop);
				}
			});
			super.onBlockHarvested(worldIn, pos, state, player);
		}
	}
	
	@Override
	protected void fillStateContainer(Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.FACING, 
				BlockStateProperties.POWERED);	
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new ElectricFurnaceTile();
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
}
