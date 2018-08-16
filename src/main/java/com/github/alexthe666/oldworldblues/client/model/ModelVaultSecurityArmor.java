package com.github.alexthe666.oldworldblues.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelVaultSecurityArmor extends ModelVaultJumpsuit {
    public ModelRenderer visor;
    public ModelRenderer armorChest;
    public ModelRenderer strapL;
    public ModelRenderer strapR;
    public ModelRenderer armorBack;
    public ModelRenderer armorHanging;
    public ModelRenderer armorBackHanging;

    public ModelVaultSecurityArmor(float modelSize, boolean smallArmsIn) {
        super(modelSize, smallArmsIn);
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.armorBack = new ModelRenderer(this, 0, 32);
        this.armorBack.setRotationPoint(0.0F, 0.0F, -2.0F);
        this.armorBack.addBox(-3.0F, 0.0F, -1.0F, 6, 11, 1, 0.0F);
        this.armorChest = new ModelRenderer(this, 0, 32);
        this.armorChest.setRotationPoint(0.0F, 0.0F, 2.0F);
        this.armorChest.addBox(-3.0F, 0.0F, 0.0F, 6, 11, 1, 0.0F);
        this.armorBackHanging = new ModelRenderer(this, 0, 44);
        this.armorBackHanging.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.armorBackHanging.addBox(-2.5F, 0.0F, -1.0F, 5, 4, 1, 0.0F);
        this.armorHanging = new ModelRenderer(this, 0, 44);
        this.armorHanging.setRotationPoint(0.0F, 11.0F, 0.0F);
        this.armorHanging.addBox(-2.5F, 0.0F, 0.0F, 5, 4, 1, 0.0F);
        this.strapL = new ModelRenderer(this, 14, 32);
        this.strapL.mirror = true;
        this.strapL.setRotationPoint(-4.0F, -1.0F, 0.0F);
        this.strapL.addBox(-1.0F, 0.0F, -3.5F, 2, 2, 7, 0.0F);
        this.strapR = new ModelRenderer(this, 14, 32);
        this.strapR.setRotationPoint(4.0F, -1.0F, 0.0F);
        this.strapR.addBox(-1.0F, 0.0F, -3.5F, 2, 2, 7, 0.0F);
        this.visor = new ModelRenderer(this, 27, 33);
        this.visor.setRotationPoint(0.0F, -4.0F, 0.0F);
        this.visor.addBox(-5.0F, -3.0F, -6.0F, 10, 4, 8, 0.0F);
        this.setRotateAngle(visor, -0.2617993877991494F, 0.0F, 0.0F);
        this.bipedBody.addChild(this.armorBack);
        this.bipedBody.addChild(this.armorChest);
        this.armorBack.addChild(this.armorBackHanging);
        this.armorChest.addChild(this.armorHanging);
        this.bipedBody.addChild(this.strapL);
        this.bipedBody.addChild(this.strapR);
        this.bipedHead.addChild(this.visor);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        this.bipedBodyWear.isHidden = true;
        this.bipedLeftArmwear.isHidden = true;
        this.bipedLeftLegwear.isHidden = true;
        this.bipedRightArmwear.isHidden = true;
        this.bipedRightLegwear.isHidden = true;
        this.bipedHeadwear.isHidden = true;
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
        this.armorBackHanging.rotateAngleX = Math.min(0, Math.min(this.bipedLeftLeg.rotateAngleX, this.bipedRightLeg.rotateAngleX));
        this.armorHanging.rotateAngleX = Math.max(0, Math.max(this.bipedLeftLeg.rotateAngleX, this.bipedRightLeg.rotateAngleX));
    }
}