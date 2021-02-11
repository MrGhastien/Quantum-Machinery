package mrghastien.quantum_machinery.common.entities;

import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TestEntity extends CowEntity {
	
	public TestEntity(EntityType<? extends CowEntity> type, World worldIn) {
		super(type, worldIn);
	}
	
	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return CowEntity.func_234188_eI_().createMutableAttribute(Attributes.MAX_HEALTH, 15)
		.createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.7d)
		.createMutableAttribute(Attributes.ARMOR_TOUGHNESS, 0);
	}

	@Override
	public CowEntity func_241840_a(ServerWorld world, AgeableEntity entity) {
		return null;
	}
}