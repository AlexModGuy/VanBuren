package com.github.alexthe666.oldworldblues.client.model;

import com.github.alexthe666.oldworldblues.block.entity.TileEntityInteriorVaultDoor;
import com.github.alexthe666.oldworldblues.block.entity.TileEntityVaultDoor;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

public class ModelInteriorVaultDoor extends AdvancedModelBase {
    public AdvancedModelRenderer doorCenter;
    public AdvancedModelRenderer frame;
    public AdvancedModelRenderer doorBack;
    public AdvancedModelRenderer doorBottom;
    public AdvancedModelRenderer frameRight;
    public AdvancedModelRenderer frameLeft;
    public AdvancedModelRenderer frameTop;
    public AdvancedModelRenderer frameCornerRight;
    public AdvancedModelRenderer frameCornerLeft;

    public ModelInteriorVaultDoor() {
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.frame = new AdvancedModelRenderer(this, 0, 0);
        this.frame.setRotationPoint(16.0F, 0.0F, 0.0F);
        this.frame.addBox(0.0F, 0.0F, 0.0F, 0, 0, 0, 0.0F);
        this.setRotateAngle(frame, 0.0F, -1.5707963267948966F, 0.0F);
        this.doorBack = new AdvancedModelRenderer(this, 32, 0);
        this.doorBack.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.doorBack.addBox(-3.0F, 0.0F, -19.0F, 6, 42, 38, 0.0F);
        this.frameRight = new AdvancedModelRenderer(this, 120, 43);
        this.frameRight.mirror = true;
        this.frameRight.setRotationPoint(0.0F, 24.0F, 19.0F);
        this.frameRight.addBox(-8.0F, -48.0F, -5.0F, 16, 48, 5, 0.0F);
        this.setRotateAngle(frameRight, 0.0F, 3.141592653589793F, 0.0F);
        this.doorBottom = new AdvancedModelRenderer(this, 120, 0);
        this.doorBottom.setRotationPoint(0.0F, 38.0F, 0.0F);
        this.doorBottom.addBox(-5.0F, 0.0F, -19.0F, 10, 5, 38, 0.0F);
        this.frameTop = new AdvancedModelRenderer(this, 124, 58);
        this.frameTop.setRotationPoint(0.0F, -21.0F, 0.0F);
        this.frameTop.addBox(-8.0F, -3.0F, -19.0F, 16, 5, 38, 0.0F);
        this.frameCornerRight = new AdvancedModelRenderer(this, 32, 0);
        this.frameCornerRight.setRotationPoint(0.0F, -19.0F, 19.0F);
        this.frameCornerRight.addBox(-7.0F, -4.0F, -4.0F, 14, 8, 5, 0.0F);
        this.setRotateAngle(frameCornerRight, 0.7853981633974483F, 0.0F, 0.0F);
        this.doorCenter = new AdvancedModelRenderer(this, 0, 0);
        this.doorCenter.setRotationPoint(16.0F, -19.0F, 0.0F);
        this.doorCenter.addBox(-4.0F, 0.0F, -4.0F, 8, 42, 8, 0.0F);
        this.setRotateAngle(doorCenter, 0.0F, -1.5707963267948966F, 0.0F);
        this.frameCornerLeft = new AdvancedModelRenderer(this, 32, 0);
        this.frameCornerLeft.setRotationPoint(0.0F, -19.0F, -19.0F);
        this.frameCornerLeft.addBox(-7.0F, -4.0F, -1.0F, 14, 8, 5, 0.0F);
        this.setRotateAngle(frameCornerLeft, -0.7853981633974483F, 0.0F, 0.0F);
        this.frameLeft = new AdvancedModelRenderer(this, 120, 43);
        this.frameLeft.setRotationPoint(0.0F, 24.0F, -19.0F);
        this.frameLeft.addBox(-8.0F, -48.0F, -5.0F, 16, 48, 5, 0.0F);
        this.doorCenter.addChild(this.doorBack);
        this.frame.addChild(this.frameRight);
        this.doorCenter.addChild(this.doorBottom);
        this.frame.addChild(this.frameTop);
        this.frame.addChild(this.frameCornerRight);
        this.frame.addChild(this.frameCornerLeft);
        this.frame.addChild(this.frameLeft);
        this.updateDefaultPose();
    }

    public void render(TileEntityInteriorVaultDoor te, float f5) {
        this.resetToDefaultPose();
        if(te != null){
            animate(te);
        }else{
            this.doorCenter.setScale(1, 1, 1);
            this.doorBack.setScale(1, 1, 1);
        }
        this.frame.render(f5);
        this.doorCenter.render(f5);
    }

    private void animate(TileEntityInteriorVaultDoor te) {
        this.doorCenter.rotationPointY -= te.openProgress * 4;
        float mod_prog = te == null ? 0 : (te.openProgress) / 10;
        this.doorCenter.setScale(1, 1 - mod_prog, 1);
        this.doorBack.setScale(1, 1 - mod_prog, 1);
        this.doorCenter.offsetY += mod_prog * 2.3F ;
        this.doorBottom.offsetY -= mod_prog * 2.3F ;
    }

    public void setRotateAngle(AdvancedModelRenderer AdvancedModelRenderer, float x, float y, float z) {
        AdvancedModelRenderer.rotateAngleX = x;
        AdvancedModelRenderer.rotateAngleY = y;
        AdvancedModelRenderer.rotateAngleZ = z;
    }
}
