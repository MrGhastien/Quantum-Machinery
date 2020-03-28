package com.mrghastien.quantum_machinery.client.renders;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrghastien.quantum_machinery.QuantumMachinery;
import com.mrghastien.quantum_machinery.client.models.GuardModel;
import com.mrghastien.quantum_machinery.entities.GuardEntity;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;

@OnlyIn(Dist.CLIENT)
public class GuardEntityRenderer extends MobRenderer<GuardEntity, GuardModel>{

	public GuardEntityRenderer(EntityRendererManager manager) {
		super(manager, new GuardModel(), 0f);
		this.addLayer(new HeldItemLayer<GuardEntity, GuardModel>(this) {
			
			@Override
			public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn,
					GuardEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
					float ageInTicks, float netHeadYaw, float headPitch) {
				
				if (entitylivingbaseIn.isAggressive()) {
					super.render(matrixStackIn, bufferIn, packedLightIn, entitylivingbaseIn, limbSwing, limbSwingAmount,
							partialTicks, ageInTicks, netHeadYaw, headPitch);
				}
			}
		});
	}

	@Override
	public ResourceLocation getEntityTexture(GuardEntity arg0) {
		return QuantumMachinery.location("textures/entity/villager_guard.png");
	}
	
	public static class RenderFactory implements IRenderFactory<GuardEntity> {

		@Override
		public EntityRenderer<? super GuardEntity> createRenderFor(EntityRendererManager manager) {
			return new GuardEntityRenderer(manager);
		}
		
	}

}