	package com.mrghastien.quantum_machinery.blocks;

import net.minecraft.util.IStringSerializable;

public enum EnergyTransfer implements IStringSerializable{
	SENDING("sending"), RECIEVE("recieving");

	String name;
	private EnergyTransfer(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

}
