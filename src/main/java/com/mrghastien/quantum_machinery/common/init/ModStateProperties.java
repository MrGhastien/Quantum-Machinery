package com.mrghastien.quantum_machinery.common.init;

import com.mrghastien.quantum_machinery.common.blocks.SideConnection;

import net.minecraft.state.EnumProperty;

public class ModStateProperties {
	
	public static final EnumProperty<SideConnection> NORTH = EnumProperty.create("north", SideConnection.class);
	public static final EnumProperty<SideConnection> SOUTH = EnumProperty.create("south", SideConnection.class);
	public static final EnumProperty<SideConnection> EAST = EnumProperty.create("east", SideConnection.class);
	public static final EnumProperty<SideConnection> WEST = EnumProperty.create("west", SideConnection.class);
	public static final EnumProperty<SideConnection> UP = EnumProperty.create("up", SideConnection.class);
	public static final EnumProperty<SideConnection> DOWN = EnumProperty.create("down", SideConnection.class);
	
}