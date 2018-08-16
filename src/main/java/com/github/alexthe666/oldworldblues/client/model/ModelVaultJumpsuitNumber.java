package com.github.alexthe666.oldworldblues.client.model;

import net.minecraft.client.model.ModelArmorStand;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelVaultJumpsuitNumber extends ModelBase {
    public ModelRenderer numberDecal;

    public ModelVaultJumpsuitNumber() {
        this.textureWidth = 16;
        this.textureHeight = 8;
        this.numberDecal = new ModelRenderer(this, 0, 0);
        this.numberDecal.mirror = true;
        this.numberDecal.setRotationPoint(0.0F, 0, 0);
        this.numberDecal.addBox(-3.0F, 1.0F, 2.201F, 6, 6, 0, 0.0F);
    }

    public void render(ModelBase model, float f, float f1, float f2, float f3, float f4, float f5) {
        if(model instanceof ModelArmorStand){
            copyModelAngles(((ModelArmorStand) model).bipedBody, this.numberDecal);
        }else if(model instanceof ModelBiped){
            ModelBiped biped = (ModelBiped)model;
            if (biped.isSneak) {
                biped.bipedBody.rotateAngleX = 0.5F;
                biped.bipedRightArm.rotateAngleX += 0.4F;
                biped.bipedLeftArm.rotateAngleX += 0.4F;
                biped.bipedRightLeg.rotationPointZ = 4.0F;
                biped.bipedLeftLeg.rotationPointZ = 4.0F;
                biped.bipedRightLeg.rotationPointY = 9.0F;
                biped.bipedLeftLeg.rotationPointY = 9.0F;
                biped.bipedHead.rotationPointY = 1.0F;
            }
            this.setModelAttributes(biped);
            copyModelAngles(biped.bipedBody, this.numberDecal);
        }
        this.numberDecal.render(f5);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.numberDecal.render(f5);
    }
}
