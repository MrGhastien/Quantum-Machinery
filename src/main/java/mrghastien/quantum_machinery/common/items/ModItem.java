package mrghastien.quantum_machinery.common.items;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.RenderTooltipEvent;

import mrghastien.quantum_machinery.util.ItemHelper;

public class ModItem extends Item {

	public ModItem() {
		this(ItemHelper.defaultProperties());
	}
	
	public ModItem(Properties properties) {
		super(properties);
	}

	public void colorRenderTootip(RenderTooltipEvent.Color event) {

	}
}
