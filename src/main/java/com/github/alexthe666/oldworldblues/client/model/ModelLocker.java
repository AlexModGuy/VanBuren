package com.github.alexthe666.oldworldblues.client.model;

import com.github.alexthe666.oldworldblues.block.entity.TileEntityLocker;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelLocker extends AdvancedModelBase {
    public AdvancedModelRenderer bottom;
    public AdvancedModelRenderer back;
    public AdvancedModelRenderer top;
    public AdvancedModelRenderer sideLeft;
    public AdvancedModelRenderer sideRight;
    public AdvancedModelRenderer door;

    public ModelLocker() {
        this.textureWidth = 64;
        this.textureHeight = 128;
        this.door = new AdvancedModelRenderer(this, 21, 0);
        this.door.setRotationPoint(7.0F, 8.0F, -7.0F);
        this.door.addBox(-14.0F, -15.0F, -1.0F, 15, 30, 1, 0.0F);
        this.bottom = new AdvancedModelRenderer(this, 0, 94);
        this.bottom.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.bottom.addBox(-8.0F, 0.0F, -8.0F, 16, 1, 16, 0.0F);
        this.top = new AdvancedModelRenderer(this, 0, 111);
        this.top.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.top.addBox(-8.0F, -31.0F, -8.0F, 16, 1, 16, 0.0F);
        this.sideLeft = new AdvancedModelRenderer(this, 2, 18);
        this.sideLeft.setRotationPoint(7.0F, 8.0F, 0.5F);
        this.sideLeft.addBox(0.0F, -15.0F, -7.5F, 1, 30, 15, 0.0F);
        this.sideRight = new AdvancedModelRenderer(this, 0, 17);
        this.sideRight.setRotationPoint(-7.0F, 8.0F, -0.5F);
        this.sideRight.addBox(-1.0F, -15.0F, -7.5F, 1, 30, 16, 0.0F);
        this.back = new AdvancedModelRenderer(this, 2, 63);
        this.back.setRotationPoint(0.0F, 8.0F, 7.0F);
        this.back.addBox(-7.0F, -15.0F, 0.0F, 14, 30, 1, 0.0F);
        this.updateDefaultPose();

    }

    public void render(TileEntityLocker entity, float f5) {
        this.resetToDefaultPose();
        if(entity != null){
            animate(entity);
        }
        this.door.render(f5);
        this.bottom.render(f5);
        this.top.render(f5);
        this.sideLeft.render(f5);
        this.sideRight.render(f5);
        this.back.render(f5);
    }


    private void animate(TileEntityLocker entity) {
        door.rotateAngleY = entity.openProgress / 5F * (float)Math.toRadians(-120F);
    }
}
