package com.github.alexthe666.oldworldblues.client.render.entity;

import com.github.alexthe666.oldworldblues.entity.EntitySeat;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderSeat extends Render<EntitySeat> {
    public RenderSeat(RenderManager renderManager) {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntitySeat entity) {
        return null;
    }
}
