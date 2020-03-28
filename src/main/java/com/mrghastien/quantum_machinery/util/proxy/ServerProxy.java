package com.mrghastien.quantum_machinery.util.proxy;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ServerProxy implements IProxy {

	@Override
    public World getClientWorld() {
        throw new IllegalStateException("Only run this on the client!");
    }

    @Override
    public PlayerEntity getClientPlayer() {
        throw new IllegalStateException("Only run this on the client!");
    }
    
    @Override
	public Screen getCurrentScreen() {
    	throw new IllegalStateException("Only run this on the client!");
	}

}
