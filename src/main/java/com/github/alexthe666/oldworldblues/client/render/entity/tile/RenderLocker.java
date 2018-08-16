package com.github.alexthe666.oldworldblues.client.render.entity.tile;

import com.github.alexthe666.oldworldblues.block.BlockLockerBottom;
import com.github.alexthe666.oldworldblues.block.entity.TileEntityInteriorVaultDoor;
import com.github.alexthe666.oldworldblues.block.entity.TileEntityLocker;
import com.github.alexthe666.oldworldblues.client.model.ModelLocker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class RenderLocker extends TileEntitySpecialRenderer<TileEntityLocker> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("oldworldblues:textures/models/entity/locker/locker.png");
    private static final ModelLocker LOCKER_MODEL = new ModelLocker();

    @Override
    public void render(TileEntityLocker entity, double x, double y, double z, float f, int yee, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5, (float) y + 4.5, (float) z + 0.5);
        GlStateManager.rotate(getRotation(entity), 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180F, 1F, 0F, 0.0F);
        GlStateManager.translate(0, 3, 0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        LOCKER_MODEL.render(entity, 0.0625F);
        GlStateManager.popMatrix();
    }


    private float getRotation(TileEntityLocker door) {
        if(door == null){
            return 0;
        }
        IBlockState state = door.getWorld().getBlockState(door.getPos());
        if(state.getBlock() instanceof BlockLockerBottom){
            EnumFacing facing = state.getValue(BlockLockerBottom.FACING);
            switch (facing) {
                default:
                    return 180;
                case SOUTH:
                    return 0;
                case EAST:
                    return 90;
                case WEST:
                    return -90;

            }
        }
        return 0;
    }
}
