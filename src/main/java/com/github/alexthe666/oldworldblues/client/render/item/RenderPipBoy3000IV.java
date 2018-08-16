package com.github.alexthe666.oldworldblues.client.render.item;

import com.github.alexthe666.oldworldblues.client.model.ModelPipBoy3000IV;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderPipBoy3000IV {
    private final static ResourceLocation TEXTURE = new ResourceLocation("oldworldblues:textures/models/entity/item/pipboy3000iv.png");
    private final static ResourceLocation TEXTURE_GLOW = new ResourceLocation("oldworldblues:textures/models/entity/item/pipboy3000iv_glow.png");
    private final static ModelPipBoy3000IV MODEL = new ModelPipBoy3000IV();
    public void renderItem(ItemStack itemStackIn) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)0.5F, (float)0.5F, (float)0.5F);
        GL11.glPushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        GL11.glPushMatrix();
        MODEL.render(null, 0, 0, 0, 0, 0, 0.0625F);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef((float)0.5F, (float)0.5F, (float)0.5F);
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_GLOW);
        MODEL.render(null, 0, 0, 0, 0, 0, 0.0625F);
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 0F, 0F);
        GlStateManager.enableLighting();
        GL11.glPopMatrix();
    }
}
