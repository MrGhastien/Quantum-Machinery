package mrghastien.quantum_machinery.common.init;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag.INamedTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import static mrghastien.quantum_machinery.QuantumMachinery.location;

public class ModTags {
	

	public static final class Items {
		
		public static final INamedTag<Item> COAL_DUST = ItemTags.createOptional(new ResourceLocation("forge", "dusts/coal"));
		public static final INamedTag<Item> SHREDS = ItemTags.createOptional(location("chunks"));
		
	}

}
