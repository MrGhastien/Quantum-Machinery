package mrghastien.quantum_machinery.common.events;

import java.awt.Color;

import mrghastien.quantum_machinery.QuantumMachinery;
import mrghastien.quantum_machinery.common.items.ModItem;

import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = QuantumMachinery.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
	
	public static int counter = 0;
	public static boolean decreasing = false;
	
	@SubscribeEvent
	public static void onColorRendered(RenderTooltipEvent.Color event) {
		Item item = event.getStack().getItem();
		if (item instanceof ModItem) {
			((ModItem)item).colorRenderTootip(event);
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
