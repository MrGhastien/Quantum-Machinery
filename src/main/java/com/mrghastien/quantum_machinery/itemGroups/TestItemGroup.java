package com.mrghastien.quantum_machinery.itemGroups;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TestItemGroup extends ItemGroup {
	
	private ItemStack iconStack; 
	
	public TestItemGroup(String name, ItemStack iconStack) {
		super(name);
		this.iconStack = iconStack;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public ItemStack createIcon() {
		return iconStack;
	}
}
