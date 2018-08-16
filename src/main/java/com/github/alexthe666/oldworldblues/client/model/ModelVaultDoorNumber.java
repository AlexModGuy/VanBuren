package com.github.alexthe666.oldworldblues.client.model;

import com.github.alexthe666.oldworldblues.block.entity.TileEntityVaultDoor;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelVaultDoorNumber extends AdvancedModelBase {
    public AdvancedModelRenderer center;
    public AdvancedModelRenderer center2;

    public ModelVaultDoorNumber() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.center = new AdvancedModelRenderer(this, 0, 0);
        this.center.setRotationPoint(0.0F, -20.0F, 0.0F);
        this.center.addBox(-10.0F, -10.0F, -4.01F, 20, 20, 0, 0.0F);
        this.center2 = new AdvancedModelRenderer(this, 0, 0);
        this.center2.mirror = true;
        this.center2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.center2.addBox(-10.0F, -10.0F, 4.01F, 20, 20, 0, 0.0F);
        this.center.addChild(this.center2);
        this.updateDefaultPose();
    }

    public void render(TileEntityVaultDoor te, float f5) {
        this.resetToDefaultPose();
        if(te != null){
            animate(te);
        }else{
            this.center.rotateAngleZ = (float)Math.toRadians(-180);
        }
        this.center.render(f5);
    }

    private void animate(TileEntityVaultDoor te) {
        this.center.rotationPointX = te.openProgress < 10 ? 0 : Math.max(((te.openProgress - 16) * 3F), 0);
        this.center.rotationPointZ = te.openProgress < 10 ? 0 : Math.min(te.openProgress - 10, 15);
        this.center.rotateAngleZ = te.openProgress < 10 ? (float)Math.toRadians(180) : (float)Math.toRadians(Math.max((te.openProgress - 16) * 18, 0)) + (float)Math.toRadians(180);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
