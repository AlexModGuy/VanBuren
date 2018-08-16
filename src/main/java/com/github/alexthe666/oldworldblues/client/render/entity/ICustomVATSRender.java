package com.github.alexthe666.oldworldblues.client.render.entity;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public interface ICustomVATSRender {
    LayerRenderer getVATSLayer(RenderLivingBase living);
}
