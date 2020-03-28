package com.mrghastien.quantum_machinery.util.proxy;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public interface IProxy {
	
	World getClientWorld();

    PlayerEntity getClientPlayer();
    
    Screen getCurrentScreen();
}
