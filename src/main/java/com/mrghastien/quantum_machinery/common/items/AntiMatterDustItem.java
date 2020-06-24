package com.mrghastien.quantum_machinery.common.items;

import java.util.List;

import com.mrghastien.quantum_machinery.common.init.ModDamageSources;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AntiMatterDustItem extends Item {

	public AntiMatterDustItem(Properties properties) {
		super(properties);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(new StringTextComponent("A glittering powder that seems to evaporate slowly..."));
		tooltip.add(new StringTextComponent(
				TextFormatting.RED + "" + TextFormatting.ITALIC + "Might be bad to eat this..."));
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {

		if (!worldIn.isRemote()) {
			worldIn.createExplosion(null, ModDamageSources.desintegrated, entityLiving.getPosX(),
					entityLiving.getPosY(), entityLiving.getPosZ(), 5.0f, false, Mode.DESTROY);
			// Entité, Source des dommages, posX, posY, posZ, rayon de l'explosion, détruire
			// les blocs ?, Mode de destruction.
		}
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}
}
