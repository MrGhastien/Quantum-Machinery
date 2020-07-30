package com.mrghastien.quantum_machinery;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

import com.mrghastien.quantum_machinery.setup.Setup;

/**
 * @author MrGhastien
 */

@Mod(QuantumMachinery.MODID)
public class QuantumMachinery {
	
	public static QuantumMachinery instance;
	public static final String MODID = "quantum_machinery";
		
	public QuantumMachinery() {
		instance = this;
		Setup.init();
	}
	
	public static ResourceLocation location(String name) {
		return new ResourceLocation(MODID, name);
	}
}