package mrghastien.quantum_machinery.common.blocks;

import net.minecraft.util.IStringSerializable;

public enum SideConnection implements IStringSerializable {
	
	NONE("none"), CABLE("cable"), MACHINE("machine");
	
	private String name;
	
	private SideConnection(String name) {
		this.name= name;
	}

	@Override
	public String getString() {
		return this.name;
	}
}
