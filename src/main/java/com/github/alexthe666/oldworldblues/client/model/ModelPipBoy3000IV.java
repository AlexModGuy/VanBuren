package com.github.alexthe666.oldworldblues.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPipBoy3000IV extends ModelBase {
    public ModelRenderer wristTop1;
    public ModelRenderer screen1;
    public ModelRenderer wristTopFront;
    public ModelRenderer wristTopBack;
    public ModelRenderer screenCover;
    public ModelRenderer screenBit1;
    public ModelRenderer screenBit3;
    public ModelRenderer screenbit4;
    public ModelRenderer screenBit2;
    public ModelRenderer wristBottomFront;
    public ModelRenderer wristBit;
    public ModelRenderer wristBottom;
    public ModelRenderer wristBottomBack;
    public ModelRenderer chargeBit;

    public ModelPipBoy3000IV() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.wristTopFront = new ModelRenderer(this, 28, 0);
        this.wristTopFront.setRotationPoint(-2.5F, -2.5F, 0.0F);
        this.wristTopFront.addBox(-3.0F, 0.0F, -4.0F, 2, 1, 8, 0.0F);
        this.setRotateAngle(wristTopFront, 0.0F, 0.0F, -1.5707963267948966F);
        this.screen1 = new ModelRenderer(this, 18, 0);
        this.screen1.setRotationPoint(-0.2F, -1.0F, -1.0F);
        this.screen1.addBox(-2.5F, -2.0F, -2.5F, 4, 1, 5, 0.0F);
        this.setRotateAngle(screen1, 0.0F, -3.141592653589793F, 0.0F);
        this.screenBit3 = new ModelRenderer(this, 11, 9);
        this.screenBit3.setRotationPoint(0.4F, 0.2F, 0.0F);
        this.screenBit3.addBox(-3.0F, -1.0F, -4.2F, 5, 1, 7, 0.0F);
        this.screenbit4 = new ModelRenderer(this, 40, 0);
        this.screenbit4.setRotationPoint(0.9F, -0.8F, -3.0F);
        this.screenbit4.addBox(-0.5F, -1.0F, -1.0F, 1, 1, 2, 0.0F);
        this.wristBottom = new ModelRenderer(this, 37, 10);
        this.wristBottom.setRotationPoint(-1.0F, 0.0F, 0.0F);
        this.wristBottom.addBox(-1.0F, 0.0F, -4.0F, 1, 5, 8, 0.0F);
        this.wristBit = new ModelRenderer(this, 52, 3);
        this.wristBit.setRotationPoint(0.4F, 4.8F, -0.1F);
        this.wristBit.addBox(-3.0F, -0.8F, -1.0F, 3, 1, 2, 0.0F);
        this.setRotateAngle(wristBit, 0.0F, 0.0F, -0.091106186954104F);
        this.screenBit1 = new ModelRenderer(this, 0, 0);
        this.screenBit1.setRotationPoint(1.0F, -1.5F, 0.0F);
        this.screenBit1.addBox(0.0F, -1.0F, -1.0F, 1, 1, 2, 0.0F);
        this.wristBottomFront = new ModelRenderer(this, 27, 9);
        this.wristBottomFront.setRotationPoint(-3.0F, 0.0F, 0.0F);
        this.wristBottomFront.addBox(-1.0F, 0.0F, -4.0F, 1, 1, 8, 0.0F);
        this.wristTopBack = new ModelRenderer(this, 40, 1);
        this.wristTopBack.setRotationPoint(2.5F, -2.5F, 0.0F);
        this.wristTopBack.addBox(-3.0F, -1.0F, -4.0F, 2, 1, 8, 0.0F);
        this.setRotateAngle(wristTopBack, 0.0F, 0.0F, -1.5707963267948966F);
        this.screenCover = new ModelRenderer(this, 0, 9);
        this.screenCover.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.screenCover.addBox(-2.5F, -2.0F, -2.5F, 4, 1, 5, 0.0F);
        this.wristTop1 = new ModelRenderer(this, 0, 0);
        this.wristTop1.setRotationPoint(0.0F, 21.0F, 0.0F);
        this.wristTop1.addBox(-2.5F, -2.5F, -4.0F, 5, 1, 8, 0.0F);
        this.screenBit2 = new ModelRenderer(this, 52, 0);
        this.screenBit2.setRotationPoint(0.0F, -1.0F, 0.0F);
        this.screenBit2.addBox(0.0F, -1.0F, -1.0F, 1, 1, 2, 0.0F);
        this.wristBottomBack = new ModelRenderer(this, 0, 15);
        this.wristBottomBack.setRotationPoint(0.0F, 4.0F, 0.0F);
        this.wristBottomBack.addBox(0.0F, 0.0F, -4.0F, 1, 1, 8, 0.0F);
        this.chargeBit = new ModelRenderer(this, 0, 3);
        this.chargeBit.setRotationPoint(-1.0F, -0.5F, 1.0F);
        this.chargeBit.addBox(-0.5F, 0.0F, 0.0F, 1, 1, 3, 0.0F);
        this.wristTop1.addChild(this.wristTopFront);
        this.wristTop1.addChild(this.screen1);
        this.screenCover.addChild(this.screenBit3);
        this.screenBit3.addChild(this.screenbit4);
        this.wristBottomFront.addChild(this.wristBottom);
        this.wristTopFront.addChild(this.wristBit);
        this.screen1.addChild(this.screenBit1);
        this.wristTopFront.addChild(this.wristBottomFront);
        this.wristTop1.addChild(this.wristTopBack);
        this.screen1.addChild(this.screenCover);
        this.screenBit1.addChild(this.screenBit2);
        this.wristBottom.addChild(this.wristBottomBack);
        this.wristBottom.addChild(this.chargeBit);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.wristTop1.render(f5);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
