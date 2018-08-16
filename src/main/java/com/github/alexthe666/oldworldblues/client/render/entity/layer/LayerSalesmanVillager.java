package com.github.alexthe666.oldworldblues.client.render.entity.layer;

import com.github.alexthe666.oldworldblues.client.model.ModelSalesmanVillager;
import com.github.alexthe666.oldworldblues.client.render.entity.RenderSalesmanVillager;
import com.github.alexthe666.oldworldblues.entity.EntitySalesmanVillager;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class LayerSalesmanVillager implements LayerRenderer<EntitySalesmanVillager> {
    private static final ResourceLocation VILLAGER_TEXTURES = new ResourceLocation("oldworldblues:textures/models/entity/villager_salesman.png");
    private final ModelSalesmanVillager salesmanModel = new ModelSalesmanVillager();
    private RenderSalesmanVillager renderer;
    public LayerSalesmanVillager(RenderSalesmanVillager renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntitySalesmanVillager entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.renderer.bindTexture(VILLAGER_TEXTURES);
        salesmanModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
}
