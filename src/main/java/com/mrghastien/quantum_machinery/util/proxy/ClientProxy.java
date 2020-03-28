package com.mrghastien.quantum_machinery.util.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World; 

public class ClientProxy implements IProxy{

	@Override
	public World getClientWorld() {
		return Minecraft.getInstance().world;
	}

	@Override
	public PlayerEntity getClientPlayer() {
		return Minecraft.getInstance().player;
	}

	@Override
	public Screen getCurrentScreen() {
		return Minecraft.getInstance().currentScreen;
	}

}
