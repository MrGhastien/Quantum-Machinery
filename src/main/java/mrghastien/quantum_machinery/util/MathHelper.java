package mrghastien.quantum_machinery.util;

import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;

import mrghastien.quantum_machinery.util.math.Quaternion;

public class MathHelper {
	
	public static int scale(int value, int max, int scalar) {
		return value == 0 || max == 0 ? 0 : (value * scalar / max);
	}
	
	public static Vector3d rotate(Vector3d toRotate, float angle, Vector3d axis) {
		float sinHalfAngle = (float)Math.sin(Math.toRadians(angle / 2));
		float cosHalfAngle = (float)Math.cos(Math.toRadians(angle / 2));
		
		float rX = (float) (axis.getX() * sinHalfAngle);
		float rY = (float) (axis.getY() * sinHalfAngle);
		float rZ = (float) (axis.getZ() * sinHalfAngle);
		float rW = cosHalfAngle;
		
		Quaternion rotation = new Quaternion(rX, rY, rZ, rW);
		Quaternion conjugate = rotation.conjugate();
		
		Quaternion w = rotation.multiply(toRotate).multiply(conjugate);
		
		return new Vector3d(w.getX(), w.getY(), w.getZ());
	}
	
	public static Vector3i rotate(Vector3i toRotate, float angle, Vector3i axis) {
		float sinHalfAngle = (float)Math.sin(Math.toRadians(angle / 2));
		float cosHalfAngle = (float)Math.cos(Math.toRadians(angle / 2));
		
		float rX = axis.getX() * sinHalfAngle;
		float rY = axis.getY() * sinHalfAngle;
		float rZ = axis.getZ() * sinHalfAngle;
		float rW = cosHalfAngle;
		
		Quaternion rotation = new Quaternion(rX, rY, rZ, rW);
		Quaternion conjugate = rotation.conjugate();
		
		Quaternion w = rotation.multiply(toRotate).multiply(conjugate);
		
		return new Vector3i(w.getX(), w.getY(), w.getZ());
	}
	
	public static Vector3i abs(Vector3i a) {
		return new Vector3i(Math.abs(a.getX()), Math.abs(a.getY()), Math.abs(a.getZ()));
	}
	
	public static int lerp(int start, int end, float t) {
		int diff = end - start;
		return (int) (start + (diff * t));
	}

	public static float inverseLerp(int start, int end, int value) {
		int diff = end - start;
		if(diff == 0)
			return start;
		return ((float) value - (float) start) / diff;
	}
}
