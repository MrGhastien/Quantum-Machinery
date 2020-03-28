package com.mrghastien.quantum_machinery.events;

import static java.awt.Color.ORANGE;
import static java.awt.Color.YELLOW;

import java.awt.Color;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.client.screens.ITempScreen;
import com.mrghastien.quantum_machinery.items.ICustomTooltip;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = QuantumMachinery.MODID, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class TooltipRenderingEventHandler {
	
	public static int counter = 0;
	public static boolean decreasing = false;
	
	@SubscribeEvent
	public static void onColorRendered(RenderTooltipEvent.Color event) {
		Screen currentScreen = QuantumMachinery.proxy.getCurrentScreen();
		Item item = event.getStack().getItem();
		if (item instanceof ICustomTooltip) {
			((ICustomTooltip)item).colorRenderTootip(event);
		}
		if (event.getStack().isEmpty()) {
			if (currentScreen instanceof ITempScreen) {
				if (event.getLines().get(0).contains("Heat :") && event.getLines().size() <= 1) {
					Color c1 = ORANGE;
					Color c2 = YELLOW;
					int t1 = (int) ((((ITempScreen) currentScreen).getClientTemp()
							/ ((ITempScreen) currentScreen).getClientMaxTemp()) * 32);
					int speed = t1 < 1 ? 1 : t1;
					if (!decreasing) {
						counter += speed;
					} else {
						counter -= speed;
					}
					if (counter <= 0) {
						counter = 0;
						decreasing = false;
					} else if(counter >= 255) {
						counter = 255;
						decreasing = true;
					}
					c1 = new Color(255, 200 - (counter * 200) / 255, 0);
					c2 = new Color(255, (counter * 200) / 255, 0);
					setBorderColor(event, c1, c2 );
				}
			}
		}
	}
	
	 public static void setBorderColor(RenderTooltipEvent.Color event, Color color) {
	 	event.setBorderStart(color.getRGB());
	 	event.setBorderEnd(color.getRGB());
	 }
	 
	 public static void setBorderColor(RenderTooltipEvent.Color event, Color start, Color end) {
		 	event.setBorderStart(start.getRGB());
		 	event.setBorderEnd(end.getRGB());
	 }
}
