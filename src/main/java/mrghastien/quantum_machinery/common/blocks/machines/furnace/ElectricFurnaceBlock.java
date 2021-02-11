package mrghastien.quantum_machinery.common.blocks.machines.furnace;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import mrghastien.quantum_machinery.common.blocks.machines.MachineBlock;
import mrghastien.quantum_machinery.common.init.ModTileEntities;

public class ElectricFurnaceBlock extends MachineBlock {

	public ElectricFurnaceBlock() {
		super(ModTileEntities.ELECTRIC_FURNACE, Properties.create(Material.IRON)
				.sound(SoundType.METAL)
				.hardnessAndResistance(5.0f)
				.setLightLevel(state -> state.get(BlockStateProperties.LIT) ? 15 : 0)
		);
		this.setDefaultState(this.stateContainer.getBaseState().with(BlockStateProperties.FACING, Direction.NORTH)
				.with(BlockStateProperties.LIT, false)
		);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
}
