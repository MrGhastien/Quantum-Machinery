package com.mrghastien.quantum_machinery.util.helpers;

import com.mrghastien.quantum_machinery.util.math.Quaternion;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class MathPHelper {
	
	public static int scale(int value, int max, int scalar) {
		return value == 0 && max == 0 ? 0 : (value * scalar / max);
	}
	
	public static Vec3d rotate(Vec3d toRotate, float angle, Vec3d axis) {
		float sinHalfAngle = (float)Math.sin(Math.toRadians(angle / 2));
		float cosHalfAngle = (float)Math.cos(Math.toRadians(angle / 2));
		
		float rX = (float) (axis.getX() * sinHalfAngle);
		float rY = (float) (axis.getY() * sinHalfAngle);
		float rZ = (float) (axis.getZ() * sinHalfAngle);
		float rW = cosHalfAngle;
		
		Quaternion rotation = new Quaternion(rX, rY, rZ, rW);
		Quaternion conjugate = rotation.conjugate();
		
		Quaternion w = rotation.multiply(toRotate).multiply(conjugate);
		
		return new Vec3d(w.getX(), w.getY(), w.getZ());
	}
	
	public static Vec3i rotate(Vec3i toRotate, float angle, Vec3i axis) {
		float sinHalfAngle = (float)Math.sin(Math.toRadians(angle / 2));
		float cosHalfAngle = (float)Math.cos(Math.toRadians(angle / 2));
		
		float rX = (float) (axis.getX() * sinHalfAngle);
		float rY = (float) (axis.getY() * sinHalfAngle);
		float rZ = (float) (axis.getZ() * sinHalfAngle);
		float rW = cosHalfAngle;
		
		Quaternion rotation = new Quaternion(rX, rY, rZ, rW);
		Quaternion conjugate = rotation.conjugate();
		
		Quaternion w = rotation.multiply(toRotate).multiply(conjugate);
		
		return new Vec3i(w.getX(), w.getY(), w.getZ());
	}
	
	public static Vec3i abs(Vec3i a) {
		return new Vec3i(Math.abs(a.getX()), Math.abs(a.getY()), Math.abs(a.getZ()));
	}
	
}
