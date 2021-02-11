package mrghastien.quantum_machinery.common.entities;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.ReturnToVillageGoal;
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
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import mrghastien.quantum_machinery.common.entities.ai.goals.GuardDefendVillageTargetGoal;
import mrghastien.quantum_machinery.common.init.ModEntities;
import mrghastien.quantum_machinery.common.init.ModItems;

public class GuardEntity extends AgeableEntity {
	
	public GuardEntity(EntityType<? extends GuardEntity>type, World worldIn) {
		super(type, worldIn);
		this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.ASTRONIUM_SWORD.get()));
		this.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
		GlobalEntityTypeAttributes.put(ModEntities.GUARD_ENTITY.get(), registerAttributes().create());
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.8D, false));
	    this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
	    this.goalSelector.addGoal(2, new ReturnToVillageGoal(this, 0.4D, false));
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
	
	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return AgeableEntity.func_233666_p_().createMutableAttribute(Attributes.ATTACK_DAMAGE, 5)
		.createMutableAttribute(Attributes.MAX_HEALTH, 20.0d)
		.createMutableAttribute(Attributes.FOLLOW_RANGE, 20.0D)
		.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.6d)
		.createMutableAttribute(Attributes.ARMOR, 15)
		.createMutableAttribute(Attributes.ARMOR_TOUGHNESS, 5)
		.createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D);
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

	@Override //createChild(AgeableEntity)
	public AgeableEntity func_241840_a(ServerWorld world, AgeableEntity entity) {
		return this;
	}

}
