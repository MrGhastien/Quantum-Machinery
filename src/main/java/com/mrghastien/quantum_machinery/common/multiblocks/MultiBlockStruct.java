package com.mrghastien.quantum_machinery.common.multiblocks;

import java.util.HashMap;
import java.util.Map;

import com.mrghastien.quantum_machinery.util.helpers.MathHelper;
import com.mrghastien.quantum_machinery.util.math.Coord3;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class MultiBlockStruct {

	private int width, height, depth;
	private String name;
	private Block[][][] pattern;

	private boolean optional;
  	
	public MultiBlockStruct(int width, int height, int depth, boolean optional) {
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.pattern = new Block[width][height][depth];
		for (int x = 0; x < this.width -1; x++) {
			for (int y = 0; y < this.height -1; y++) {
				for (int z = 0; z < this.depth -1; z++) {
					this.set(x, y, z, null);
				}
			}
		}
		this.optional = optional;
	}
	
	public MultiBlockStruct(Vec3i size, boolean optional) {
		this(size.getX(), size.getY(), size.getZ(), optional);
	}
	
	public BlockPos middle() {
		return new BlockPos(width / 2, height / 2, depth / 2);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getDepth() {
		return depth;
	}
	
	public Coord3 getSize() {
		return new Coord3(width, height, depth);
	}

	public String getName() {
		return name;
	}
	
	public boolean isOptional() {
		return optional;
	}
	
	public Block[][][] getPattern() {
		return pattern;
	}
	
	public Block get(int x, int y, int z) {
		return pattern[x][y][z];
	}
	
	public Block get(Vec3i relPos) {
		return this.get(relPos.getX(), relPos.getY(), relPos.getZ());
	}
	
	/**
	 * This method is used to rotate a part-.
	 * @param reversed Tells if the rotation is counter clockwise
	 * 
	 * @return The rotated part.
	 * 
	 */
	public MultiBlockStruct rotate(int angle) {
		MultiBlockStruct tempPart = getRotatedSize(angle);
		Vec3i tempSize = new Vec3i(tempPart.width, tempPart.height, tempPart.depth);
		Vec3i translator = null;
		Vec3i newTranslator = null;
		Vec3i newOrigin = new Vec3i(-(Math.abs((tempSize.getX() - 1) / 2)),
				-(Math.abs((tempSize.getY() - 1) / 2)), -(Math.abs((tempSize.getZ() - 1) / 2)));
		
		MultiBlockStruct part = new MultiBlockStruct(Math.abs(tempSize.getX()), Math.abs(tempSize.getY()), Math.abs(tempSize.getZ()), optional);
		for (int x = newOrigin.getX(); x < newOrigin.getX() + tempSize.getX(); x++) {
			for (int y = newOrigin.getY(); y < newOrigin.getY() + tempSize.getY(); y++) {
				for (int z = newOrigin.getZ(); z < newOrigin.getZ() + tempSize.getZ(); z++) {
					
					BlockPos newPos = new BlockPos(x, y, z);
					BlockPos patternPos = new BlockPos(MathHelper.rotate(newPos, angle / 90 * -90, new Vec3i(0, 1, 0)));
					if (newTranslator == null) {
						newTranslator = new Vec3i(-newOrigin.getX(), -newOrigin.getY(), -newOrigin.getZ());
					}
					if (translator == null) {
						translator = new Vec3i((Math.abs((width) / 2)),
								(Math.abs((height - 1) / 2)), (Math.abs((depth - 1) / 2)));
					}
					part.set(newPos.add(newTranslator), this.get(patternPos.add(translator)));
				}
			}
		} 
		return part;
	}
	
	public static Vec3i singleRotate(Vec3i index, MultiBlockStruct part, int angle) {
		MultiBlockStruct tempPart = part.getRotatedSize(angle);
		Vec3i tempSize = new Vec3i(tempPart.width, tempPart.height, tempPart.depth);
		Vec3i newTranslator = null;
		Vec3i newOrigin = new Vec3i(-(Math.abs((tempSize.getX() - 1) / 2)), -(Math.abs((tempSize.getY() - 1) / 2)), -(Math.abs((tempSize.getZ() - 1) / 2)));
		Vec3i translator = new Vec3i(-(Math.abs((part.width - 1) / 2)), -(Math.abs((part.height - 1) / 2)), -(Math.abs((part.depth - 1) / 2)));
		
		BlockPos patternPos = new BlockPos(index).add(translator);
		BlockPos newPos = new BlockPos(MathHelper.rotate(patternPos, angle / 90 * -90, new Vec3i(0, 1, 0)));
		if (newTranslator == null) {
			newTranslator = new Vec3i(-newOrigin.getX(), -newOrigin.getY(), -newOrigin.getZ());
		}
		return newPos.add(newTranslator);
	}
	
	public MultiBlockStruct getRotatedSize(int angle) {
		return new MultiBlockStruct(MathHelper.abs(MathHelper.rotate(new Vec3i(width, height, depth), angle / 90 * 90, new Vec3i(0, 1, 0))), this.optional);
	}
	
	public MultiBlockStruct getMergedSize(MultiBlockStruct part, int offsetX, int offsetY, int offsetZ) {
		int totalWidth = this.width > part.width + offsetX ? this.width : part.width + offsetX ;
		int totalHeight = this.height > part.height + offsetY ? this.height : part.height + offsetY;
		int totalDepth = this.depth > part.depth + offsetZ ? this.depth : part.depth + offsetZ;
		
		if (offsetX < 0) {
			totalWidth += Math.abs(offsetX);
		}
		if (offsetY < 0) {
			totalHeight += Math.abs(offsetY);
		}
		if (offsetZ < 0) {
			totalDepth += Math.abs(offsetZ);
		}
		return new MultiBlockStruct(new Vec3i(totalWidth, totalHeight, totalDepth), this.optional);
	}
	
	public static  Map<MultiBlockStruct, Vec3i> singleMerge(Vec3i index,MultiBlockStruct part1, MultiBlockStruct part2, int offsetX, int offsetY, int offsetZ) {
		Map<MultiBlockStruct, Vec3i> result = new HashMap<>();
		boolean[] canBePart2 = new boolean[3];
		boolean[] canBeNull = new boolean[3];
		
		if (index.getX() < 0 || index.getY() < 0 || index.getZ() < 0) {
			return result;
		}
		
		if (offsetX >= 0) {
			if (index.getX() >= offsetX && index.getX() < offsetX + part2.width) {
				canBePart2[0] = true;
			} else {
				if (index.getX() < part1.width) {
					canBePart2[0] = false;
				} else {
					canBeNull[0] = true;
				}
			}
		} else {
			if (index.getX() >= 0 && index.getX() < part2.width) {
				canBePart2[0] = true;
			} else {
				if (index.getX() >= part2.width && index.getX() < part1.width + part2.width) {
					canBePart2[0] = false;
				} else {
					canBeNull[0] = true;
				}
			}
		}
		
		if (offsetY >= 0) {
			if (index.getY() >= offsetY && index.getY() < offsetY + part2.height) {
				canBePart2[1] = true;
			} else {
				if (index.getY() < part1.height) {
					canBePart2[1] = false;
				} else {
					canBeNull[1] = true;
				}
			}
		} else {
			if (index.getY() >= 0 && index.getY() < part2.height) {
				canBePart2[1] = true;
			} else {
				if (index.getY() >= part2.height && index.getY() < part1.height + part2.height) {
					canBePart2[1] = false;
				} else {
					canBeNull[1] = true;
				}
			}
		}
		
		if (offsetZ >= 0) {
			if (index.getZ() >= offsetZ && index.getZ() < offsetZ + part2.depth) {
				canBePart2[2] = true;
			} else {
				if (index.getZ() < part1.depth) {
					canBePart2[2] = false;
				} else {
					canBeNull[2] = true;
				}
			}
		} else {
			if (index.getZ() >= 0 && index.getZ() < part2.depth) {
				canBePart2[2] = true;
			} else {
				if (index.getZ() >= part2.depth && index.getZ() < part1.depth + part2.depth) {
					canBePart2[2] = false;
				} else {
					canBeNull[2] = true;
				}
			}
		}
		if (canBeNull[0] && canBeNull[1] && canBeNull[2]) {
			return result;
		}
		if (canBePart2[0] && canBePart2[1] && canBePart2[2]) {
			result.put(part2, new Vec3i(index.getX() - offsetX, index.getX() - offsetX, index.getX() - offsetX));
		} else {
			result.put(part1, new Vec3i(index.getX() - offsetX, index.getX() - offsetX, index.getX() - offsetX));
		}
		return result;
	}
	
	public MultiBlockStruct merge(MultiBlockStruct part, int offsetX, int offsetY, int offsetZ) {
		int totalWidth = this.width > part.width + offsetX ? this.width : part.width + offsetX ;
		int totalHeight = this.height > part.height + offsetY ? this.height : part.height + offsetY;
		int totalDepth = this.depth > part.depth + offsetZ ? this.depth : part.depth + offsetZ;
		
		if (offsetX < 0) {
			totalWidth += Math.abs(offsetX);
		}
		if (offsetY < 0) {
			totalHeight += Math.abs(offsetY);
		}
		if (offsetZ < 0) {
			totalDepth += Math.abs(offsetZ);
		}
		MultiBlockStruct mergedPart = new MultiBlockStruct(totalWidth, totalHeight, totalDepth, false);
		boolean canStop = true;
		for (int x = 0; x < mergedPart.getWidth(); x++) {
			for (int y = 0; y < mergedPart.getHeight(); y++) {
				for (int z = 0; z < mergedPart.getDepth(); z++) {
					if (x < this.width && y < this.height && z < this.depth) {
						mergedPart.set(offsetX < 0 ? -offsetX + x : x, offsetY < 0 ? -offsetY + y : y, offsetZ < 0 ? -offsetZ + z : z, this.get(x, y, z));
						canStop = false;
					}
					if (x < part.width && y < part.height && z < part.depth) {
						mergedPart.set(offsetX < 0 ? x : x + offsetX, offsetY < 0 ? y : y + offsetY, offsetZ < 0 ? z : z + offsetZ, part.get(x, y, z));
						canStop = false;
					}
					if (canStop) {
						break;
					} else {
						canStop = true;
					}
				}
			}
		}
		return mergedPart;
	}
	
	public void set(int x, int y, int z, Block block) {
		this.pattern[x][y][z] = block;
	}
	
	public void set(BlockPos relPos, Block block) {
		this.set(relPos.getX(), relPos.getY(), relPos.getZ(), block);
	}
	
	public void set(Block[][][] pattern) {
		if (pattern.length >= this.pattern.length) {
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					for (int z = 0; z < depth; z++) {
						this.pattern[x][y][z] = pattern[x][y][z];
					}
				}
			}
		}
	}
}
