package com.mrghastien.quantum_machinery.blocks;

import net.minecraft.util.IStringSerializable;

public enum SideConnection implements IStringSerializable {
	
	NONE("none"), CABLE("cable"), MACHINE("machine");
	
	private String name;
	
	private SideConnection(String name) {
		this.name= name;
	}

	@Override
	public String getName() {
		return this.name;
	}
}
