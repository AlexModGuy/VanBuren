package com.github.alexthe666.oldworldblues.client.render.entity.layer;

import com.github.alexthe666.oldworldblues.entity.VATSProperties;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class LayerVATSRender implements LayerRenderer {

    private RenderLivingBase renderer;
    private final ModelBase slimeModel = new ModelSlime(0);

    public LayerVATSRender(RenderLivingBase renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float f, float f1, float i, float f2, float f3, float f4, float f5) {
        if (entitylivingbaseIn instanceof EntityLiving) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
            if(properties != null && properties.isVatsTriggered() && properties.currentTarget != null && properties.currentTarget == entitylivingbaseIn){
                boolean flag = entitylivingbaseIn.isInvisible();
                GlStateManager.depthMask(!flag);
                this.renderer.bindTexture(new ResourceLocation(getVatsTexture(this.renderer.getMainModel(), 1)));
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                float translate = (float)entitylivingbaseIn.ticksExisted + i;
                GlStateManager.translate(translate * 0.005F, translate * 0.005F, 0.0F);
                GlStateManager.matrixMode(5888);
                GlStateManager.enableBlend();
                GlStateManager.color(0.5F, 0.5F, 0.5F, 0.75F);
                GlStateManager.disableLighting();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
                Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
                this.renderer.getMainModel().render(entitylivingbaseIn, f, f1, f2, f3, f4, f5);
                Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
                GlStateManager.matrixMode(5890);
                GlStateManager.loadIdentity();
                GlStateManager.matrixMode(5888);
                GlStateManager.enableLighting();
                GlStateManager.disableBlend();
                GlStateManager.depthMask(flag);

            }
        }
    }

    public String getVatsTexture(ModelBase model, int size) {
        int x = model.textureWidth;
        int y = model.textureHeight;
        int sizeX = Math.min(128, x * size);
        int sizeY = Math.min(128, y * size);
        String str = "oldworldblues:textures/models/entity/vats_overlay/vats_" + sizeX + "_" + sizeY + ".png";
        if (sizeX <= 16 && sizeY <= 16) {
            return "oldworldblues:textures/models/entity/vats_overlay/vats_16_16.png";
        } else {
            return str;
        }
    }


    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
