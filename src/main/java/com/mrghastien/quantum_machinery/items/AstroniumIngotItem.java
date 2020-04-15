package com.mrghastien.quantum_machinery.items;

import java.awt.Color;
import java.util.List;

import com.mrghastien.quantum_machinery.events.TooltipRenderingEventHandler;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;

public class AstroniumIngotItem extends ModItem {

	public AstroniumIngotItem(Properties properties) {
		super(properties);
	}
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(new StringTextComponent("An ingot made completly from astronium"));
		tooltip.add(new StringTextComponent(TextFormatting.GOLD + "" + TextFormatting.ITALIC + "\"Shiny.\""));
	}
	
	@Override
	public void colorRenderTootip(RenderTooltipEvent.Color event) {
		Color c1 = new Color(0, 255 / 4, 125 / 4);
		Color c2 = new Color(0, 255 / 8, 125 / 8);
		TooltipRenderingEventHandler.setBorderColor(event, c1, c2);
	}

}
