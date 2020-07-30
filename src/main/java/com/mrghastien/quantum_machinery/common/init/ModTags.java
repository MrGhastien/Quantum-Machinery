package com.mrghastien.quantum_machinery.common.init;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import static com.mrghastien.quantum_machinery.QuantumMachinery.location;

public class ModTags {
	

	public static final class Items {
		
		public static final Tag<Item> COAL_DUST = new ItemTags.Wrapper(new ResourceLocation("forge", "dusts/coal"));
		public static final Tag<Item> SHREDS = new ItemTags.Wrapper(location("chunks"));
		
	}

}
