package com.mrghastien.quantum_machinery.api.common;

import net.minecraft.util.IStringSerializable;

public enum ConnectionType implements IStringSerializable {
	NONE,
	IN,
	OUT,
	BOTH;
	
	public boolean canReceive() {
		return this == IN || this == BOTH;
	}
	
	public boolean canExtract() {
		return this == OUT || this == BOTH;
	}

	@Override
	public String getName() {
		return name().toLowerCase();
	}
}