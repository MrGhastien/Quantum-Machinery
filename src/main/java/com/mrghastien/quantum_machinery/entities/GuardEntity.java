package com.mrghastien.quantum_machinery.entities;

import com.mrghastien.quantum_machinery.entities.ai.goals.GuardDefendVillageTargetGoal;
import com.mrghastien.quantum_machinery.init.ModItems;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.entity.ai.goal.MoveTowardsVillageGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class GuardEntity extends AgeableEntity{
	
	public GuardEntity(EntityType<? extends GuardEntity>type, World worldIn) {
		super(type, worldIn);
		this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.ASTRONIUM_SWORD.get()));
		this.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
		
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.8D, false));
	    this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
	    this.goalSelector.addGoal(2, new MoveTowardsVillageGoal(this, 0.4D));
	    this.goalSelector.addGoal(3, new MoveThroughVillageGoal(this, 0.4D, false, 4, () -> {
	       return false;
	    }));
	    this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 0.4D));
	    this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
	    this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
	    this.targetSelector.addGoal(1, new GuardDefendVillageTargetGoal(this));
	    this.targetSelector.addGoal(2, new HurtByTargetGoal(this).setCallsForHelp());
	    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, false, false, (p_213619_0_) -> {
	       return p_213619_0_ instanceof IMob && !(p_213619_0_ instanceof CreeperEntity);
	    }));
	    this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, VindicatorEntity.class, false));
	}
	
	@Override
	public void registerAttributes() {
		super.registerAttributes();
		this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0d);
		this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
		this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6d);
		this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(15);
		this.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(5);
		this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0D);
	}	  

	
	@OnlyIn(Dist.CLIENT)
	   public static enum ArmPose {
	      CROSSED,
	      ATTACKING,
	}
	
	@OnlyIn(Dist.CLIENT)
	   public ArmPose getArmPose() {
	      if (this.isAggressive()) {
	         return ArmPose.ATTACKING;
	      } else {
	    	  return ArmPose.CROSSED;
	      }
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_VILLAGER_AMBIENT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_VILLAGER_DEATH;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_VILLAGER_HURT;
	}
	
	@Override
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
		this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.ASTRONIUM_SWORD.get()));
	}

	@Override
	public AgeableEntity createChild(AgeableEntity ageable) {
		return this;
	}

}
