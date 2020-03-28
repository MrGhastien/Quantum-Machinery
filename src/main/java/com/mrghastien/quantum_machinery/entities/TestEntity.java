package com.mrghastien.quantum_machinery.entities;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.world.World;

public class TestEntity extends CowEntity {
	
	public TestEntity(EntityType<? extends CowEntity> type, World worldIn) {
		super(type, worldIn);
	}
	
	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0d);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.7d);
		this.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(0);
	}

	@Override
	public CowEntity createChild(AgeableEntity ageable) {
		return null;
	}



	

	

}
