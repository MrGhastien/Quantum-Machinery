package com.mrghastien.quantum_machinery.multiblocks.fission;

import java.util.HashSet;
import java.util.Set;

import com.mrghastien.quantum_machinery.init.ModBlocks;
import com.mrghastien.quantum_machinery.multiblocks.IMultiBlock;
import com.mrghastien.quantum_machinery.multiblocks.MultiBlockStruct;
import com.mrghastien.quantum_machinery.multiblocks.MultiBlockTile;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FissionMultiBlock implements IMultiBlock {
	
	public static final FissionMultiBlock INSTANCE = new FissionMultiBlock();
	
	/**
	 * The pattern of the multiblock.
	 * A null value will mean we can place any block at this relative position of the multiblock.
	 */
	private static final MultiBlockStruct STRUCT = new MultiBlockStruct(23, 7, 7, false);
	
	static {
		//
		for (int x = 0; x < STRUCT.getWidth(); x++) {
			for (int y = 0; y < STRUCT.getHeight(); y++) {
				for (int z = 0; z < STRUCT.getDepth(); z++) {
					//2 bras latéraux
					if (y >= 1 && y <= STRUCT.getHeight() - 2) {
						if (z >= 1 && z <= STRUCT.getDepth() - 2) {
							STRUCT.set(x, y, z, ModBlocks.FISSION_CASING.get());
						}
					}
					//remplissage des deux bras par de l'air
					if (y >= 2 && y <= 4) {
						if (z >= 2 && z <= STRUCT.getDepth() - 3) {
							if (x >= 1 && x <= STRUCT.getWidth() - 2) {
								STRUCT.set(x, y, z, Blocks.AIR);
							}
						}
					}
					//Centre
					if (x >= 8 && x <= 14) {
						STRUCT.set(x, y, z, ModBlocks.FISSION_CASING.get());
					}
					//Remplissage du centre par de l'eau
					if (y >= 1 && y <= STRUCT.getHeight() - 2) {
						if (z >= 1 && z <= STRUCT.getDepth() - 2) {
							if (x >= 9 && x <= 13) {
								STRUCT.set(x, y, z, Blocks.WATER);
							}
						}
					}
					//Barres de carburant
					if (y >= 1 && y <= STRUCT.getHeight() - 2) {
						if (z == 2 || z == 4) {
							if (x == 10 || x == 12) {
								STRUCT.set(x, y, z, ModBlocks.FISSION_FUEL_ROD.get());
							}
						}
					}
					
					//Arrondissement de la structure
					//Partie 1: Longueur
					if ((x >= 0 && x <= 7) || (x >= 15 && x <= STRUCT.getWidth() - 1)) {
						if (z == 1 || z == 5) {
							if (y == 1 || y == 5) {
								STRUCT.set(x, y, z, null);
							} 
						}
					}
					if (x >= 8 && x <= 14) {
						if (z == 0 || z == 6) {
							if (y == 0 || y == 6) {
								STRUCT.set(x, y, z, null);
							}
						}
					}
					//Partie 2: Hauteur 
					if (x == 8 || x == 14) {
						if (z == 0 || z == STRUCT.getDepth() - 1) {
							STRUCT.set(x, y, z, null);
						}
					}
					if (x == 0 || x == STRUCT.getWidth() - 1) {
						if (z == 1 || z == 5) {
							STRUCT.set(x, y, z, null);
						}
					}
					
					//Partie 3: Largeur 
					if (x == 8 || x == 14) {
						if (y == 0 || y == STRUCT.getHeight() - 1) {
							STRUCT.set(x, y, z, null);
						}
					}
					if (x == 0 || x == STRUCT.getWidth() - 1) {
						if (y == 1 || y == STRUCT.getHeight() - 2) {
							STRUCT.set(x, y, z, null);
						}
					}
					//Verre devant le réacteur
					if (x >= 10 && x <= 12) {
						if (y >= 2 && y <= 4) {
							STRUCT.set(x, y, 0, ModBlocks.FISSION_GLASS.get());
						}
					}
					
					//Anneaux de confinement magnétique
					if (x == 3 || x == 5 || x == 17 || x == 19) {
						if (y >= 2 && y <= 4) {
							if (z == 0 || z == STRUCT.getDepth() - 1) {
								STRUCT.set(x, y, z, ModBlocks.MAGNETIC_MODULE.get());
							}
						} else if (y == 1 || y == STRUCT.getHeight() - 2) {
							if (z == 1 || z == STRUCT.getDepth() - 2) {
								STRUCT.set(x, y, z, ModBlocks.MAGNETIC_MODULE.get());
							}
						}
					}
				}
			}
		}
		STRUCT.set(10, STRUCT.getHeight() - 1, 2, ModBlocks.FISSION_ROD_CONTROLLER.get());
		STRUCT.set(10, STRUCT.getHeight() - 1, 4, ModBlocks.FISSION_ROD_CONTROLLER.get());
		STRUCT.set(12, STRUCT.getHeight() - 1, 2, ModBlocks.FISSION_ROD_CONTROLLER.get());
		STRUCT.set(12, STRUCT.getHeight() - 1, 4, ModBlocks.FISSION_ROD_CONTROLLER.get());
		
		STRUCT.set(11, 2, 0, ModBlocks.FISSION_CONTROLLER.get());
	}

	@Override
	public Set<BlockPos> isValidUnformedMultiBlock(Direction facing, World world, BlockPos pos) {
		int cntSuper = 0;
		Set<BlockPos> erroredPos = new HashSet<BlockPos>();
		MultiBlockStruct struct1 = getStruct(facing);
		for (int x = 0; x < struct1.getWidth(); x++) {
			for (int y = 0; y < struct1.getHeight(); y++) {
				for (int z = 0; z < struct1.getDepth(); z++) {
					if (!isValidUnformedBlock(struct1, world, pos, x, y, z)) {
						erroredPos.add(pos.add(x, y, z));
					}
					if (isValidUnformedController(facing, struct1, world, pos, x, y, z)) {
						cntSuper++;
					}
				}
			}
		}
		if (cntSuper != 1) {
			erroredPos.add(pos.add(getControllerRelativePos(facing)));
		}
		return erroredPos;
	}
	
	private boolean isValidUnformedBlock(MultiBlockStruct finalShape, World world, BlockPos pos, int dx, int dy, int dz) {
		TileEntity te = world.getTileEntity(pos.add(dx, dy, dz));
		BlockState state = world.getBlockState(pos.add(dx, dy, dz));
		if (finalShape.getPattern()[dx][dy][dz] != null) {
			if (te instanceof MultiBlockTile) {
				return state.getBlock() == finalShape.getPattern()[dx][dy][dz] && !((MultiBlockTile)te).isFormed();
			} else {
				return state.getBlock() == finalShape.getPattern()[dx][dy][dz];
			}
		} else {
			return true;
		}
	}
	
	private boolean isValidUnformedController(Direction facing, MultiBlockStruct finalShape, World world, BlockPos pos, int dx, int dy, int dz) {
		if (finalShape.getPattern() != null) {
			BlockState state = world.getBlockState(pos.add(dx, dy, dz));
			if (state.getBlock() == ModBlocks.FISSION_CONTROLLER.get()) {
				return isValidUnformedBlock(finalShape, world, pos, dx, dy, dz) && state.get(BlockStateProperties.HORIZONTAL_FACING) == facing;
			}
			return false;
		} else {
			return false;
		}
	}

	@Override
	public Set<BlockPos> isValidFormedMultiBlock(Direction facing, World world, BlockPos pos) {
		int cntSuper = 0;
		Set<BlockPos> erroredPos = new HashSet<BlockPos>();
		MultiBlockStruct struct1 = getStruct(facing);
		for (int x = 0; x < struct1.getWidth(); x++) {
			for (int y = 0; y < struct1.getHeight(); y++) {
				for (int z = 0; z < struct1.getDepth(); z++) {
					if (!isValidFormedBlock(struct1, world, pos, x, y, z)) {
						erroredPos.add(pos.add(x, y, z));
					}
					if (isValidFormedController(facing, struct1, world, pos, x, y, z)) {
						cntSuper++;
					}
				}
			}
		}
		if (cntSuper != 1) {
			erroredPos.add(pos.add(getControllerRelativePos(facing)));
		}
		return erroredPos;
	}
	
	private boolean isValidFormedBlock(MultiBlockStruct finalShape, World world, BlockPos pos, int dx, int dy, int dz) {
		TileEntity te = world.getTileEntity(pos.add(dx, dy, dz));
		BlockState state = world.getBlockState(pos.add(dx, dy, dz));
		if (finalShape.getPattern()[dx][dy][dz] != null) {
			if (te instanceof MultiBlockTile) {
				return state.getBlock() == finalShape.getPattern()[dx][dy][dz] && ((MultiBlockTile)te).isFormed();
			} else {
				return state.getBlock() == finalShape.getPattern()[dx][dy][dz];
			}
		} else {
			return true;
		}
	}
	
	private boolean isValidFormedController(Direction facing, MultiBlockStruct finalShape, World world, BlockPos pos, int dx, int dy, int dz) {
		if (finalShape.getPattern() != null) {
			BlockState state = world.getBlockState(pos.add(dx, dy, dz));
			if (state.getBlock() == ModBlocks.FISSION_CONTROLLER.get()) {
				return isValidFormedBlock(finalShape, world, pos, dx, dy, dz) && state.get(BlockStateProperties.HORIZONTAL_FACING) == facing;
			}
			return false;
		} else {
			return false;
		}
	}

	@Override
	public BlockPos getControllerRelativePos(Direction facing) {
		switch (facing) {
		case NORTH:
			return new BlockPos(11, 2, 0);
		case SOUTH:
			return new BlockPos(11, 2, 6);
		case EAST:
			return new BlockPos(6, 2, 11);
		case WEST:
			return new BlockPos(0, 2, 11);	
		default:
			return null;
		}
	}
	
	public MultiBlockStruct getStruct() {
		return getStruct(Direction.NORTH);
	}

	@Override
	public MultiBlockStruct getStruct(Direction facing) {
		int angle = facing == Direction.NORTH ? 0 : facing == Direction.SOUTH ? 180 : facing == Direction.EAST ? 90 : facing == Direction.WEST ? -90 : 0;
		return STRUCT.rotate(angle);
	}

	@Override
	public BlockPos getOrigin(World world, BlockPos pos) {
		BlockPos p = pos;
		BlockState state = world.getBlockState(pos);
		Direction facing = null;
		MultiBlockStruct finalPart = getStruct();
		for (int x = 0; x < finalPart.getWidth(); x++) {
			for (int y = 0; y < finalPart.getHeight(); y++) {
				for (int z = 0; z < finalPart.getDepth(); z++) {

					// on divise en 8 le multibloc, et on regarde chaque partie
					p = pos.add(x, y, z);
					state = world.getBlockState(p);
					if (state.getBlock() == ModBlocks.FISSION_CONTROLLER.get()) {
						facing = state.get(BlockStateProperties.HORIZONTAL_FACING);
						break;
					}

					p = pos.add(-x, y, z);
					state = world.getBlockState(p);
					if (state.getBlock() == ModBlocks.FISSION_CONTROLLER.get()) {
						facing = state.get(BlockStateProperties.HORIZONTAL_FACING);
						break;
					}

					p = pos.add(x, y, -z);
					state = world.getBlockState(p);
					if (state.getBlock() == ModBlocks.FISSION_CONTROLLER.get()) {
						facing = state.get(BlockStateProperties.HORIZONTAL_FACING);
						break;
					}

					p = pos.add(x, -y, z);
					state = world.getBlockState(p);
					if (state.getBlock() == ModBlocks.FISSION_CONTROLLER.get()) {
						facing = state.get(BlockStateProperties.HORIZONTAL_FACING);
						break;
					}

					p = pos.add(-x, -y, z);
					state = world.getBlockState(p);
					if (state.getBlock() == ModBlocks.FISSION_CONTROLLER.get()) {
						facing = state.get(BlockStateProperties.HORIZONTAL_FACING);
						break;
					}

					p = pos.add(x, -y, -z);
					state = world.getBlockState(p);
					if (state.getBlock() == ModBlocks.FISSION_CONTROLLER.get()) {
						facing = state.get(BlockStateProperties.HORIZONTAL_FACING);
						break;
					}

					p = pos.add(-x, y, -z);
					state = world.getBlockState(p);
					if (state.getBlock() == ModBlocks.FISSION_CONTROLLER.get()) {
						facing = state.get(BlockStateProperties.HORIZONTAL_FACING);
						break;
					}

					p = pos.add(-x, -y, -z);
					state = world.getBlockState(p);
					if (state.getBlock() == ModBlocks.FISSION_CONTROLLER.get()) {
						facing = state.get(BlockStateProperties.HORIZONTAL_FACING);
						break;
					}
				}
			}
		}
		return facing == null ? null : p.subtract(getControllerRelativePos(facing));
	}
}
