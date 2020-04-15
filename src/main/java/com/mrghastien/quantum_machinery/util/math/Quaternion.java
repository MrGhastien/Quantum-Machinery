package com.mrghastien.quantum_machinery.util.math;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Quaternion {

	private float x;
	private float y;
	private float z;
	private float w;
	
	public Quaternion(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public float length() {
		return (float) Math.sqrt(x*x + y*y + z*z + w*w);
	}
	
	public Quaternion normalize() {
		float length = length();
		
		x /= length;
		y /= length;
		z /= length;
		w /= length;
		
		return this;
	}
	
	public Quaternion conjugate() {
		return new Quaternion(-x, -y, -z, w);
	}
	
	public Quaternion multiply(Quaternion q) {
		
		float tempW = w * q.getW() - x * q.getX() - y * q.getY() - z * q.getZ();
		float tempX = x * q.getW() + w * q.getX() + y * q.getZ() - z * q.getY();
		float tempY = y * q.getW() + w * q.getY() + z * q.getX() - x * q.getZ();
		float tempZ = z * q.getW() + w * q.getZ() + x * q.getY() - y * q.getX();

		return new Quaternion(tempX, tempY, tempZ, tempW);
	}
	
	public Quaternion multiply(Vec3d r) {
		
		float tempW = (float) (-x * r.getX() - y * r.getY() - z * r.getZ());
		float tempX = (float) (w * r.getX() + y * r.getZ() - z * r.getY());
		float tempY = (float) (w * r.getY() + z * r.getX() - x * r.getZ());
		float tempZ = (float) (w * r.getZ() + x * r.getY() - y * r.getX());
		
		return new Quaternion(tempX, tempY, tempZ, tempW);
	}
	
	public Quaternion multiply(Vec3i r) {
		
		float tempW = (float) (-x * r.getX() - y * r.getY() - z * r.getZ());
		float tempX = (float) (w * r.getX() + y * r.getZ() - z * r.getY());
		float tempY = (float) (w * r.getY() + z * r.getX() - x * r.getZ());
		float tempZ = (float) (w * r.getZ() + x * r.getY() - y * r.getX());
		
		return new Quaternion(tempX, tempY, tempZ, tempW);
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}	
}
