package mrghastien.quantum_machinery.common.blocks.machines.alloy_smelter;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import mrghastien.quantum_machinery.common.blocks.machines.MachineBlock;
import mrghastien.quantum_machinery.common.init.ModTileEntities;
import mrghastien.quantum_machinery.util.BlockHelper;

public class AlloySmelterBlock extends MachineBlock {
	
	public AlloySmelterBlock() {
		super(ModTileEntities.ALLOY_SMELTER, BlockHelper.defaultProperties().setLightLevel(state -> state.get(BlockStateProperties.LIT) ? 15 : 0));
		this.setDefaultState(this.stateContainer.getBaseState().with(BlockStateProperties.FACING, Direction.NORTH)
				.with(BlockStateProperties.LIT, false)
		);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

}

