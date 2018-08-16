package com.github.alexthe666.oldworldblues.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelSalesmanVillager extends ModelBase {
    public ModelRenderer head;
    public ModelRenderer armRight;
    public ModelRenderer armLeft;
    public ModelRenderer armMiddle;
    public ModelRenderer legLeft;
    public ModelRenderer torso;
    public ModelRenderer clothes;
    public ModelRenderer rightLeg;
    public ModelRenderer nose;
    public ModelRenderer tribly1;
    public ModelRenderer tribly2;

    public ModelSalesmanVillager() {
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.armLeft = new ModelRenderer(this, 44, 23);
        this.armLeft.setRotationPoint(0.0F, 3.0F, -1.0F);
        this.armLeft.addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, 0.0F);
        this.setRotateAngle(armLeft, -0.7499679795819634F, 0.0F, 0.0F);
        this.armMiddle = new ModelRenderer(this, 40, 39);
        this.armMiddle.setRotationPoint(0.0F, 3.0F, -1.0F);
        this.armMiddle.addBox(-4.0F, 2.0F, -2.0F, 8, 4, 4, 0.0F);
        this.setRotateAngle(armMiddle, -0.7499679795819634F, 0.0F, 0.0F);
        this.torso = new ModelRenderer(this, 16, 21);
        this.torso.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.torso.addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, 0.0F);
        this.nose = new ModelRenderer(this, 24, 1);
        this.nose.setRotationPoint(0.0F, -2.0F, 0.0F);
        this.nose.addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, 0.0F);
        this.head = new ModelRenderer(this, 0, 1);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F);
        this.legLeft = new ModelRenderer(this, 0, 23);
        this.legLeft.mirror = true;
        this.legLeft.setRotationPoint(2.0F, 12.0F, 0.0F);
        this.legLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.rightLeg = new ModelRenderer(this, 0, 23);
        this.rightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
        this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
        this.tribly1 = new ModelRenderer(this, 0, 65);
        this.tribly1.setRotationPoint(0.0F, -10.0F, 0.0F);
        this.tribly1.addBox(-6.0F, 1.0F, -6.0F, 12, 2, 12, 0.0F);
        this.armRight = new ModelRenderer(this, 44, 23);
        this.armRight.setRotationPoint(0.0F, 3.0F, -1.0F);
        this.armRight.addBox(-8.0F, -2.0F, -2.0F, 4, 8, 4, 0.0F);
        this.setRotateAngle(armRight, -0.7499679795819634F, 0.0F, 0.0F);
        this.tribly2 = new ModelRenderer(this, 0, 83);
        this.tribly2.setRotationPoint(0.0F, 2.0F, 0.0F);
        this.tribly2.addBox(-4.5F, -5.0F, -4.5F, 9, 5, 9, 0.0F);
        this.clothes = new ModelRenderer(this, 0, 39);
        this.clothes.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.clothes.addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, 0.5F);
        this.head.addChild(this.nose);
        this.head.addChild(this.tribly1);
        this.tribly1.addChild(this.tribly2);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.armLeft.render(f5);
        this.armMiddle.render(f5);
        this.torso.render(f5);
        this.head.render(f5);
        this.legLeft.render(f5);
        this.rightLeg.render(f5);
        this.armRight.render(f5);
        this.clothes.render(f5);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        this.head.rotateAngleY = netHeadYaw * 0.017453292F;
        this.head.rotateAngleX = headPitch * 0.017453292F;
        this.rightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.legLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
