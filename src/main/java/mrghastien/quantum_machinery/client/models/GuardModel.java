package mrghastien.quantum_machinery.client.models;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import mrghastien.quantum_machinery.common.entities.GuardEntity;
import mrghastien.quantum_machinery.common.entities.GuardEntity.ArmPose;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.entity.model.IHasHead;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuardModel extends EntityModel<GuardEntity> implements IHasArm, IHasHead{
    public ModelRenderer GuardHead;
    public ModelRenderer GuardRightArm;
    public ModelRenderer GuardRightLeg;
    public ModelRenderer GuardLeftLeg;
    public ModelRenderer GuardLeftArm;
    public ModelRenderer GuardTorso;
    public ModelRenderer GuardJacket;
    public ModelRenderer GuardCrossedRightArm;
    public ModelRenderer GuardCrossedHands;
    public ModelRenderer GuardCrossedLeftArm;
    public ModelRenderer GuardNose;
    
    private ArmPose currentPose;

    public GuardModel() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.GuardRightArm = new ModelRenderer(this, 40, 46);
        this.GuardRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.GuardRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.GuardCrossedHands = new ModelRenderer(this, 40, 38);
        this.GuardCrossedHands.setRotationPoint(0.0F, 3.0F, -1.0F);
        this.GuardCrossedHands.addBox(-4.0F, 2.0F, -2.0F, 8, 4, 4, 0.0F);
        this.setRotateAngle(GuardCrossedHands, -0.7499679795819634F, 0.0F, 0.0F);
        this.GuardNose = new ModelRenderer(this, 24, 0);
        this.GuardNose.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.GuardNose.addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, 0.0F);
        this.GuardHead = new ModelRenderer(this, 0, 0);
        this.GuardHead.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.GuardHead.addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F);
        this.GuardCrossedRightArm = new ModelRenderer(this, 44, 22);
        this.GuardCrossedRightArm.setRotationPoint(0.0F, 3.0F, -1.0F);
        this.GuardCrossedRightArm.addBox(-8.0F, -2.0F, -2.0F, 4, 8, 4, 0.0F);
        this.setRotateAngle(GuardCrossedRightArm, -0.7499679795819634F, 0.0F, 0.0F);
        this.GuardLeftArm = new ModelRenderer(this, 40, 46);
        this.GuardLeftArm.mirror = true;
        this.GuardLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
        this.GuardLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, 0.0F);
        this.GuardTorso = new ModelRenderer(this, 16, 20);
        this.GuardTorso.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.GuardTorso.addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, 0.0F);
        this.GuardCrossedLeftArm = new ModelRenderer(this, 44, 22);
        this.GuardCrossedLeftArm.mirror = true;
        this.GuardCrossedLeftArm.setRotationPoint(0.0F, 3.0F, -1.0F);
        this.GuardCrossedLeftArm.addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, 0.0F);
        this.setRotateAngle(GuardCrossedLeftArm, -0.7499679795819634F, 0.0F, 0.0F);
        this.GuardRightLeg = new ModelRenderer(this, 0, 22);
        this.GuardRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.GuardRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.GuardLeftLeg = new ModelRenderer(this, 0, 22);
        this.GuardLeftLeg.mirror = true;
        this.GuardLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.GuardLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.GuardJacket = new ModelRenderer(this, 0, 38);
        this.GuardJacket.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.GuardJacket.addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, 0.5F);
        this.GuardHead.addChild(this.GuardNose);
    }
    
    @Override
    public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn,
    		float red, float green, float blue, float alpha) {
    	this.GuardHead.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.GuardTorso.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.GuardRightLeg.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.GuardLeftLeg.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        this.GuardJacket.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        if(currentPose == GuardEntity.ArmPose.CROSSED) {
        	this.GuardCrossedLeftArm.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        	this.GuardCrossedRightArm.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        	this.GuardCrossedHands.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        } else {
            this.GuardRightArm.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
            this.GuardLeftArm.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
        }
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    @Override
	public void setLivingAnimations(GuardEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        super.setLivingAnimations(entityIn, limbSwing, limbSwingAmount, partialTick);
     }
	
	private ModelRenderer getArm(HandSide p_191216_1_) {
	      return p_191216_1_ == HandSide.LEFT ? this.GuardLeftArm : this.GuardRightArm;
	   }

	@Override
	public ModelRenderer getModelHead() {
		return this.GuardHead;
	}

	@Override
	public void translateHand(HandSide sideIn, MatrixStack matrixStackIn) {
		this.getArm(sideIn).translateRotate(matrixStackIn);;
	}
	
	

	@Override
	public void setRotationAngles(GuardEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks,
			float netHeadYaw, float headPitch) {
    	currentPose = entityIn.getArmPose();
    	this.GuardRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + (float)Math.PI) * 1.4f * limbSwingAmount* 0.5f;
    	this.GuardRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + (float)Math.PI) * 1.4f * limbSwingAmount * 0.5f;
    	this.GuardLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount * 0.5f;
    	this.GuardLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 1.4f * limbSwingAmount * 0.5f;
    	//MathHelper.cos(limbSwing * 0.6662f + (float)Math.PI) * 1.4f * limbSwingAmount * 0.5f
    	
    	this.GuardHead.rotateAngleY = netHeadYaw * 0.017453292f;
    	this.GuardHead.rotateAngleX = headPitch * 0.017453292f;
    	GuardEntity.ArmPose armPose = entityIn.getArmPose();
    	if (armPose == GuardEntity.ArmPose.ATTACKING) {
            float f = MathHelper.sin(this.swingProgress * (float)Math.PI);
            float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float)Math.PI);
            this.GuardRightArm.rotateAngleZ = 0.0F;
            this.GuardLeftArm.rotateAngleZ = 0.0F;
            this.GuardRightArm.rotateAngleY = 0.15707964F;
            this.GuardLeftArm.rotateAngleY = -0.15707964F;
            if (entityIn.getPrimaryHand() == HandSide.RIGHT) {
               this.GuardRightArm.rotateAngleX = -1.8849558F + MathHelper.cos(ageInTicks * 0.09F) * 0.15F;
               this.GuardLeftArm.rotateAngleX = -0.0F + MathHelper.cos(ageInTicks * 0.19F) * 0.5F;
               this.GuardRightArm.rotateAngleX += f * 2.2F - f1 * 0.4F;
               this.GuardLeftArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
            } else {
               this.GuardRightArm.rotateAngleX = -0.0F + MathHelper.cos(ageInTicks * 0.19F) * 0.5F;
               this.GuardLeftArm.rotateAngleX = -1.8849558F + MathHelper.cos(ageInTicks * 0.09F) * 0.15F;
               this.GuardLeftArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
               this.GuardLeftArm.rotateAngleX += f * 2.2F - f1 * 0.4F;
            }

            this.GuardLeftArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.GuardLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
            this.GuardLeftArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
            this.GuardLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
         }
	}

}
