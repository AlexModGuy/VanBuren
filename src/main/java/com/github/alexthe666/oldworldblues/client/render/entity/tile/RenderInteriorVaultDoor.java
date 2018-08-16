package com.github.alexthe666.oldworldblues.client.render.entity.tile;

import com.github.alexthe666.oldworldblues.block.entity.TileEntityInteriorVaultDoor;
import com.github.alexthe666.oldworldblues.block.entity.TileEntityVaultDoor;
import com.github.alexthe666.oldworldblues.client.model.ModelInteriorVaultDoor;
import com.github.alexthe666.oldworldblues.client.model.ModelVaultDoor;
import com.github.alexthe666.oldworldblues.client.model.ModelVaultDoorNumber;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class RenderInteriorVaultDoor extends TileEntitySpecialRenderer<TileEntityInteriorVaultDoor> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("oldworldblues:textures/models/entity/vault_door/interior_vault_door.png");
    private static final ModelInteriorVaultDoor VAULT_DOOR_MODEL = new ModelInteriorVaultDoor();

    @Override
    public void render(TileEntityInteriorVaultDoor entity, double x, double y, double z, float f, int yee, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5, (float) y + 4.5, (float) z + 0.5);
        GlStateManager.rotate(getRotation(entity), 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180F, 1F, 0F, 0.0F);
        GlStateManager.translate(-2, 3, 0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        VAULT_DOOR_MODEL.render(entity, 0.0625F);
        GlStateManager.popMatrix();
    }


    private float getRotation(TileEntityInteriorVaultDoor door) {
        if(door == null){
            return 180;
        }
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
