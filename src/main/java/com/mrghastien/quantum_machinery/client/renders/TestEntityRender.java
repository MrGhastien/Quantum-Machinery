package com.mrghastien.quantum_machinery.client.renders;

import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.client.models.TestEntityModel;
import com.mrghastien.quantum_machinery.common.entities.TestEntity;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;

@OnlyIn(Dist.CLIENT)
public class TestEntityRender extends LivingRenderer<TestEntity, TestEntityModel>{

	public TestEntityRender(EntityRendererManager manager) {
		super(manager, new TestEntityModel(), 0f);
	}

	@Override
	public ResourceLocation getEntityTexture(TestEntity arg0) {
		return QuantumMachinery.location("textures/entity/test_entity.png");
	}
	
	public static class RenderFactory implements IRenderFactory<TestEntity> {

		@Override
		public EntityRenderer<? super TestEntity> createRenderFor(EntityRendererManager manager) {
			return new TestEntityRender(manager);
		}
		
	}

}
