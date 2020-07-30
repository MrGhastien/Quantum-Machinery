package com.mrghastien.quantum_machinery.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class ModOreBlock extends Block {

	public ModOreBlock() {
		super(Properties.create(Material.ROCK).hardnessAndResistance(3));
	}
}
