package com.github.alexthe666.oldworldblues.client.render.entity;

import com.github.alexthe666.oldworldblues.client.render.entity.layer.LayerSalesmanVillager;
import com.github.alexthe666.oldworldblues.entity.EntitySalesmanVillager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ResourceLocation;

public class RenderSalesmanVillager extends RenderLiving<EntitySalesmanVillager> {

    private static final ResourceLocation EMPTY_TEXTURE = new ResourceLocation("oldworldblues:textures/models/entity/empty.png");


    public RenderSalesmanVillager(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelVillager(0.0F), 0.5F);
        this.addLayer(new LayerCustomHead(this.getMainModel().villagerHead));
        this.addLayer(new LayerSalesmanVillager(this));
    }

    public ModelVillager getMainModel() {
        return (ModelVillager) super.getMainModel();
    }

    protected ResourceLocation getEntityTexture(EntitySalesmanVillager entity) {
        return EMPTY_TEXTURE;
    }

    protected void preRenderCallback(EntitySalesmanVillager entitylivingbaseIn, float partialTickTime) {
        float f = 0.9375F;

        if (entitylivingbaseIn.getGrowingAge() < 0) {
            f = (float) ((double) f * 0.5D);
            this.shadowSize = 0.25F;
        } else {
            this.shadowSize = 0.5F;
        }

        GlStateManager.scale(f, f, f);
    }
}
