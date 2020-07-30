package com.mrghastien.quantum_machinery.common.items;

import com.mrghastien.quantum_machinery.util.helpers.ItemHelper;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.RenderTooltipEvent;

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
