package com.github.alexthe666.oldworldblues.client.render.entity.tile;

import com.github.alexthe666.oldworldblues.block.entity.TileEntityVaultDoor;
import com.github.alexthe666.oldworldblues.client.model.ModelVaultDoor;
import com.github.alexthe666.oldworldblues.client.model.ModelVaultDoorNumber;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderVaultDoor extends TileEntitySpecialRenderer<TileEntityVaultDoor> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("oldworldblues:textures/models/entity/vault_door/vault_door.png");
    private static final ModelVaultDoor VAULT_DOOR_MODEL = new ModelVaultDoor();
    private static final ModelVaultDoorNumber VAULT_DOOR_NUMBER_OVERLAY_MODEL = new ModelVaultDoorNumber();

    @Override
    public void render(TileEntityVaultDoor entity, double x, double y, double z, float f, int yee, float alpha) {
        renderVaultDoor(entity, getRotation(entity), x, y, z, true);
        renderVaultDoorNumber(entity, entity.number, getRotation(entity), x, y, z);
    }

    public void renderVaultDoor(TileEntityVaultDoor tileEntityVaultDoor, float rotation, double x, double y, double z, boolean renderFrame){
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5, (float) y + 4.5, (float) z + 0.5);
        GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-2.5, 0, 0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        VAULT_DOOR_MODEL.render(tileEntityVaultDoor, renderFrame, 0.0625F);
        GlStateManager.popMatrix();
    }

    public void renderVaultDoorNumber(TileEntityVaultDoor tileEntityVaultDoor, int number, float rotation, double x, double y, double z){
        if(number >= 0 && number < 1000) {
            String numberStr = "" + MathHelper.clamp(number, 0, 999);
            for(int i = 0; i < numberStr.length(); i++) {
                GlStateManager.pushMatrix();
                GlStateManager.disableCull();
                GlStateManager.translate((float) x + 0.5, (float) y + 4.5, (float) z + 0.5);
                GlStateManager.rotate(rotation, 0.0F, 1.0F, 0.0F);
                GlStateManager.translate(-2.5, 0, 0);
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                Minecraft.getMinecraft().getTextureManager().bindTexture(getNumberTexture(number, i, number < 100 && number > 9));
                VAULT_DOOR_NUMBER_OVERLAY_MODEL.render(tileEntityVaultDoor, 0.0625F);
                GlStateManager.disableBlend();
                GlStateManager.enableCull();
                GlStateManager.popMatrix();
                GlStateManager.popMatrix();
            }
        }
    }

    private ResourceLocation getNumberTexture(int number, int index, boolean tens){
        String numberStr = "" + MathHelper.clamp(number, 0, 999);
        String indexName = index == 0 ? "first" : index == 1 ? "second" : "third";
        if(numberStr.length() > index){
            int i = Integer.parseInt(numberStr.charAt(index) + "");
            if(tens){
                return new ResourceLocation("oldworldblues:textures/models/entity/vault_door/numbers/tens/" + indexName + "_" + i + ".png");
            }else{
                if(numberStr.length() == 1){
                    return new ResourceLocation("oldworldblues:textures/models/entity/vault_door/numbers/second" + "_" + i + ".png");
                }
                return new ResourceLocation("oldworldblues:textures/models/entity/vault_door/numbers/" + indexName + "_" + i + ".png");
            }
        }
        return TEXTURE;
    }

    private float getRotation(TileEntityVaultDoor door) {
        switch (door.getBlockMetadata()) {
            default:
                return 180;
            case 1:
                return 90;
            case 2:
                return 0;
            case 3:
                return -90;

        }
    }

}
