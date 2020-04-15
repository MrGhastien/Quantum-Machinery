package com.mrghastien.quantum_machinery.items;

import com.mrghastien.quantum_machinery.util.ItemUtils;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.RenderTooltipEvent;

public class ModItem extends Item {

	public ModItem() {
		this(ItemUtils.DEFAULT_PROPERTIES);
	}
	
	public ModItem(Properties properties) {
		super(properties);
	}

	public void colorRenderTootip(RenderTooltipEvent.Color event) {

	}

	public void preRenderTootip(RenderTooltipEvent.Pre event) {

	}

	public void PostBGRenderTootip(RenderTooltipEvent.PostBackground event) {

	}

	public void PostTextRenderTootip(RenderTooltipEvent.PostText event) {

	}
}
