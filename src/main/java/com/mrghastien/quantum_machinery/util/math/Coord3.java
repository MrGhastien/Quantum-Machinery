package com.mrghastien.quantum_machinery.util.math;

import net.minecraft.util.math.Vec3i;

public class Coord3 {

	public static Coord3 NULL_COORDS = new Coord3(0, 0, 0);
	private int x;
	private int y;
	private int z;
	
	public Coord3(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3i toVec3i() {
		return new Vec3i(x, y, z);
	}
	
	public Coord3 add(int x, int y, int z) {
		return new Coord3(this.x + x, this.y + y, this.z + z);
	}
	
	public Coord3 add(Coord3 coords) {
		return add(coords.x, coords.y, coords.z);
	}
	
	public Coord3 add(Vec3i coords) {
		return add(coords.getX(), coords.getY(), coords.getZ());
	}
	
	public Coord3 sub(int x, int y, int z) {
		return new Coord3(this.x - x, this.y - y, this.z - z);
	}
	
	public Coord3 sub(Coord3 coords) {
		return sub(coords.x, coords.y, coords.z);
	}
	
	public Coord3 sub(Vec3i coords) {
		return sub(coords.getX(), coords.getY(), coords.getZ());
	}
	
	public int volume() {
		return x * y * z;
	}
	
	public Coord3 resetX() {
		return new Coord3(0, y, z);
	}
	
	public Coord3 resetY() {
		return new Coord3(x, 0, z);
	}
	
	public Coord3 resetZ() {
		return new Coord3(x, y, 0);
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}
}
