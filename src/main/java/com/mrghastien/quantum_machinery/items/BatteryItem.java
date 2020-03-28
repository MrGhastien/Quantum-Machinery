package com.mrghastien.quantum_machinery.items;

import java.util.List;

import com.mrghastien.quantum_machinery.capabilities.energy.ModEnergyStorage;
import com.mrghastien.quantum_machinery.util.helpers.Units;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class BatteryItem extends EnergyItem {

	public BatteryItem(Properties properties) {
		super(properties, 8192, 128);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new StringTextComponent(TextFormatting.GRAY + "Used to store energy."));
		LazyOptional<IEnergyStorage> ec = stack.getCapability(CapabilityEnergy.ENERGY, null);
		if(ec.isPresent()) {
			tooltip.add(new StringTextComponent("Energy : " + ec.map(e -> ((ModEnergyStorage)e).getEnergyStored()).orElse(0) + "/" + ec.map(e -> ((ModEnergyStorage)e).getMaxEnergyStored()).orElse(0) + Units.ENERGY));
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

}
