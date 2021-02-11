package mrghastien.quantum_machinery.common.blocks.generators.blaster;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import mrghastien.quantum_machinery.common.blocks.generators.GeneratorBlock;
import mrghastien.quantum_machinery.common.init.ModTileEntities;

public class BlasterBlock extends GeneratorBlock {

	public BlasterBlock() {
		super(ModTileEntities.BLASTER, Properties.create(Material.IRON)
				.sound(SoundType.METAL)
				.hardnessAndResistance(5.0f)
				.setLightLevel(b -> b.get(BlockStateProperties.LIT) ? 15 : 0)
		);
		this.setDefaultState(this.stateContainer.getBaseState().with(BlockStateProperties.FACING, Direction.NORTH)
				.with(BlockStateProperties.LIT, false)
				);
	}
	
	@Override
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn,
			BlockRayTraceResult hit) {
		if(!worldIn.isRemote) {
			worldIn.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
		}
		return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
	}

}
