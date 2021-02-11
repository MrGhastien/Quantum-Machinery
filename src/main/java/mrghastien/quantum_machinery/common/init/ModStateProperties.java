package mrghastien.quantum_machinery.common.init;

import net.minecraft.state.EnumProperty;

import mrghastien.quantum_machinery.common.blocks.SideConnection;

public class ModStateProperties {
	
	public static final EnumProperty<SideConnection> NORTH = EnumProperty.create("north", SideConnection.class);
	public static final EnumProperty<SideConnection> SOUTH = EnumProperty.create("south", SideConnection.class);
	public static final EnumProperty<SideConnection> EAST = EnumProperty.create("east", SideConnection.class);
	public static final EnumProperty<SideConnection> WEST = EnumProperty.create("west", SideConnection.class);
	public static final EnumProperty<SideConnection> UP = EnumProperty.create("up", SideConnection.class);
	public static final EnumProperty<SideConnection> DOWN = EnumProperty.create("down", SideConnection.class);
	
}