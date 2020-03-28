package com.mrghastien.quantum_machinery.items;

import net.minecraftforge.client.event.RenderTooltipEvent;

public interface ICustomTooltip {
	
	default void colorRenderTootip(RenderTooltipEvent.Color event) {

	}

	default void preRenderTootip(RenderTooltipEvent.Pre event) {

	}

	default void PostBGRenderTootip(RenderTooltipEvent.PostBackground event) {

	}

	default void PostTextRenderTootip(RenderTooltipEvent.PostText event) {

	}
}
