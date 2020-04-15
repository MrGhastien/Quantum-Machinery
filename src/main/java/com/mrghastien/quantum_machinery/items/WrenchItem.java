package com.mrghastien.quantum_machinery.items;

import static java.awt.Color.CYAN;

import java.awt.Color;
import java.util.List;

import com.mrghastien.quantum_machinery.blocks.MachineBlock;
import com.mrghastien.quantum_machinery.blocks.CableBlock;
import com.mrghastien.quantum_machinery.blocks.ConnectorBlock;
import com.mrghastien.quantum_machinery.blocks.EnergyTransfer;
import com.mrghastien.quantum_machinery.events.TooltipRenderingEventHandler;
import com.mrghastien.quantum_machinery.init.ModBlocks;
import com.mrghastien.quantum_machinery.setup.ModSetup;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;

public class WrenchItem extends ModItem {

	public WrenchItem() {
		super(new Item.Properties().group(ModSetup.MAIN_TAB).rarity(Rarity.RARE).maxStackSize(1));
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		if(!context.getWorld().isRemote) {
			Block block = context.getWorld().getBlockState(context.getPos()).getBlock();
			if(context.getPlayer().isShiftKeyDown() && (block instanceof MachineBlock || block instanceof ConnectorBlock || block instanceof CableBlock)) {
				context.getWorld().destroyBlock(context.getPos(), true);
			} else if(block == ModBlocks.CONNECTOR.get()) {
				if(context.getWorld().getBlockState(context.getPos()).get(ConnectorBlock.ENERGY_HANDLING_MODE) == EnergyTransfer.SENDING) {
					context.getWorld().setBlockState(context.getPos(), context.getWorld().getBlockState(context.getPos()).with(ConnectorBlock.ENERGY_HANDLING_MODE, EnergyTransfer.RECIEVE));
				} else if(context.getWorld().getBlockState(context.getPos()).get(ConnectorBlock.ENERGY_HANDLING_MODE) == EnergyTransfer.RECIEVE) {
					context.getWorld().setBlockState(context.getPos(), context.getWorld().getBlockState(context.getPos()).with(ConnectorBlock.ENERGY_HANDLING_MODE, EnergyTransfer.SENDING));
				}
			}
		}
		return super.onItemUse(context);
	}
	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(new StringTextComponent(TextFormatting.DARK_GRAY + "Used to pickup machines."));
		tooltip.add(new StringTextComponent(TextFormatting.DARK_GRAY + "" + TextFormatting.ITALIC + "Looks familiar..."));
	}
	
	@Override
	public void colorRenderTootip(RenderTooltipEvent.Color event) {
		Color c1 = CYAN;
		Color c2 = CYAN;
		int speed = 2;
		if (!TooltipRenderingEventHandler.decreasing) {
			TooltipRenderingEventHandler.counter += speed;
		} else {
			TooltipRenderingEventHandler.counter -= speed;
		}
		if (TooltipRenderingEventHandler.counter <= 0) {
			TooltipRenderingEventHandler.counter = 0;
			TooltipRenderingEventHandler.decreasing = false;
		} else if(TooltipRenderingEventHandler.counter >= 255) {
			TooltipRenderingEventHandler.counter = 255;
			TooltipRenderingEventHandler.decreasing = true;
		}
		c1 = new Color(0, 255 - TooltipRenderingEventHandler.counter, 255 - TooltipRenderingEventHandler.counter);
		c2 = new Color(0, TooltipRenderingEventHandler.counter, TooltipRenderingEventHandler.counter);
		TooltipRenderingEventHandler.setBorderColor(event, c1, c2);
	}
	
}
