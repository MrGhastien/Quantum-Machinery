package com.mrghastien.quantum_machinery.util;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public final class RenderingUtils {

	public static int color(int r, int g, int b, int a) {
		return new Color(r, g, b, a).getRGB();
	}

	public static void fillGradient(int xPos, int yPos, int width, int height, int offset, Color start, Color end,
			GradientMode mode) {
		int lastXPos = xPos + width;
		int lastYPos = yPos + height;
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.disableAlphaTest();
		RenderSystem.defaultBlendFunc();
		RenderSystem.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
		switch(mode) {
			case VERTICAL:
				bufferbuilder.pos(lastXPos, yPos, offset).color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha()).endVertex();
				bufferbuilder.pos(xPos, yPos, offset).color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha()).endVertex();
				bufferbuilder.pos(xPos, lastYPos, offset).color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha()).endVertex();
				bufferbuilder.pos(lastXPos, lastYPos, offset).color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha()).endVertex();
			break;
			
			case HORIZONTAL:
				bufferbuilder.pos(lastXPos, yPos, offset).color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha()).endVertex();
				bufferbuilder.pos(xPos, yPos, offset).color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha()).endVertex();
				bufferbuilder.pos(xPos, lastYPos, offset).color(end.getRed(), end.getGreen(), end.getBlue(), end.getAlpha()).endVertex();
				bufferbuilder.pos(lastXPos, lastYPos, offset).color(start.getRed(), start.getGreen(), start.getBlue(), start.getAlpha()).endVertex();
				break;
		}
		tessellator.draw();
		RenderSystem.shadeModel(7424);
		RenderSystem.disableBlend();
		RenderSystem.enableAlphaTest();
		RenderSystem.enableTexture();
	}
	
	public static void fillGradientLerpColor(int xPos, int yPos, int width, int height, int maxWidth, int maxtHeight, int offset, Color start, Color end,
			GradientMode mode) {
		float lerpHeight = MathHelper.inverseLerp(0, maxtHeight, height);
		fillGradient(xPos, yPos, width, height, offset, lerpColor(start, end, lerpHeight), end, mode);
		
	}
	
	public static Color lerpColor(Color start, Color end, float t) {
		int lerpRed = MathHelper.lerp(end.getRed(), start.getRed(), t);
		int lerpGreen = MathHelper.lerp(end.getGreen(), start.getGreen(), t);
		int lerpBlue = MathHelper.lerp(end.getBlue(), start.getBlue(), t);
		int lerpAlpha = MathHelper.lerp(end.getAlpha(), start.getAlpha(), t);
		return new Color(lerpRed, lerpGreen, lerpBlue, lerpAlpha);
	}

	public enum GradientMode {
		VERTICAL, HORIZONTAL;
	}

}
