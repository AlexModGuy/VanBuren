package com.github.alexthe666.oldworldblues.client.render.item;

import com.github.alexthe666.oldworldblues.client.ClientProxy;
import com.github.alexthe666.oldworldblues.client.render.entity.tile.RenderInteriorVaultDoor;
import com.github.alexthe666.oldworldblues.client.render.entity.tile.RenderLocker;
import com.github.alexthe666.oldworldblues.client.render.entity.tile.RenderVaultDoor;
import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import com.github.alexthe666.oldworldblues.init.OWBItems;
import com.github.alexthe666.oldworldblues.item.ItemVaultMap;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.util.ItemTESRContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.MapData;
import org.lwjgl.opengl.GL11;

public class OWBTEISR extends TileEntityItemStackRenderer {

    public static final RenderPipBoy3000IV RENDER_PIP_BOY_3000_IV = new RenderPipBoy3000IV();
    public static final RenderVaultDoor RENDER_VAULT_DOOR = new RenderVaultDoor();
    public static final RenderInteriorVaultDoor RENDER_INTERIOR_VAULT_DOOR = new RenderInteriorVaultDoor();
    public static final RenderLocker RENDER_LOCKER = new RenderLocker();
    public static Minecraft mc = Minecraft.getMinecraft();
    private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("oldworldblues:textures/map/vault_map.png");

    public void renderByItem(ItemStack itemStackIn) {

        if (itemStackIn.getItem() == OWBItems.FILLED_VAULT_MAP){
            boolean isRenderingOnTheScreen = ItemTESRContext.INSTANCE.getCurrentTransform() == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || ItemTESRContext.INSTANCE.getCurrentTransform() == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND;
            if(isRenderingOnTheScreen && this.mc.gameSettings.thirdPersonView == 0 && !(this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping()) && !this.mc.gameSettings.hideGUI && !this.mc.playerController.isSpectator()) {
                float f1 = mc.player.prevRotationPitch + (mc.player.rotationPitch - mc.player.prevRotationPitch) * LLibrary.PROXY.getPartialTicks();
                float rot = MathHelper.clamp(f1 - 90, -75, 0);
                GlStateManager.rotate(rot, 1.0F, 0.0F, 0.0F);
                GlStateManager.scale(1 - rot * 0.0133, 1 - rot * 0.0133, 1 - rot * 0.0133);
                GlStateManager.translate(0, 0, rot * 0.01);
                renderArms();
                renderMapFirstPerson(itemStackIn);
            }else{
                GlStateManager.translate(0.5, 0.5, 0.5);
                mc.getItemRenderer().renderItem(mc.player, new ItemStack(OWBItems.VAULT_MAP), ItemCameraTransforms.TransformType.NONE);
            }
        }
        if (itemStackIn.getItem() == OWBItems.PIPBOY3000IV) {
            RENDER_PIP_BOY_3000_IV.renderItem(itemStackIn);
        }
        if (itemStackIn.getItem() == Item.getItemFromBlock(OWBBlocks.VAULT_DOOR)) {
            GL11.glScalef(0.325F, 0.325F, 0.325F);
            RENDER_VAULT_DOOR.renderVaultDoor(null, 180, -2.3, -0.8, 2, false);
            if (itemStackIn.getTagCompound() != null && itemStackIn.getTagCompound().getInteger("Number") < 1000) {
                RENDER_VAULT_DOOR.renderVaultDoorNumber(null, itemStackIn.getTagCompound().getInteger("Number"), 180, -2.3, -0.8, 2);
            }
        }
        if (itemStackIn.getItem() == Item.getItemFromBlock(OWBBlocks.INTERIOR_VAULT_DOOR)) {
            GL11.glScalef(0.325F, 0.325F, 0.325F);
            RENDER_INTERIOR_VAULT_DOOR.render(null, -1D, 1, 2, 0, 0, 0);
        }
        if (itemStackIn.getItem() == Item.getItemFromBlock(OWBBlocks.LOCKER_BOTTOM)) {
            GL11.glScalef(0.6F, 0.6F, 0.6F);
            RENDER_LOCKER.render(null, 0.75F, -0.4F, 0, 0, 0, 0);
        }

    }

    private void renderMapFirstPerson(ItemStack stack) {
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(1F, 1F, 1F);
        GlStateManager.disableLighting();
        this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.translate(-0.5F, -1.5F, -0.2F);
        GlStateManager.scale(0.0078125F, 0.0078125F, 0.0078125F);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-7.0D, 135.0D, 0.0D).tex(0.0D, 1.0D).endVertex();
        bufferbuilder.pos(135.0D, 135.0D, 0.0D).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos(135.0D, -7.0D, 0.0D).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos(-7.0D, -7.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
        GL11.glPushMatrix();
        GlStateManager.translate(-30F, -46F, 0F);
        GlStateManager.scale(1.5F, 1.5F, 1.5F);
        tessellator.draw();
        GL11.glPopMatrix();
        MapData mapdata = ((ItemVaultMap) stack.getItem()).getMapData(stack, this.mc.world);

        if (mapdata != null) {
            ClientProxy.mapItemRenderer.renderMap(mapdata, false);
        }

        GlStateManager.enableLighting();
    }

    private void renderArms() {
        if (!this.mc.player.isInvisible()) {
            GlStateManager.disableCull();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 1F, 0.25F);
            GlStateManager.scale(1.25F, 1.25F, 1.25F);
            GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
            this.renderArm(EnumHandSide.RIGHT);
            this.renderArm(EnumHandSide.LEFT);
            GlStateManager.popMatrix();
            GlStateManager.enableCull();
        }
    }

    private void renderArm(EnumHandSide p_187455_1_) {
        this.mc.getTextureManager().bindTexture(this.mc.player.getLocationSkin());
        Render<AbstractClientPlayer> render = this.mc.getRenderManager().<AbstractClientPlayer>getEntityRenderObject(this.mc.player);
        RenderPlayer renderplayer = (RenderPlayer) render;
        GlStateManager.pushMatrix();
        float f = p_187455_1_ == EnumHandSide.RIGHT ? 1.0F : -1.0F;
        GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f * -41.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(f * 0.3F, -1.1F, 0.45F);

        if (p_187455_1_ == EnumHandSide.RIGHT) {
            renderplayer.renderRightArm(this.mc.player);
        } else {
            renderplayer.renderLeftArm(this.mc.player);
        }

        GlStateManager.popMatrix();
    }

    private float getMapAngleFromPitch(float pitch) {
        float f = 1.0F - pitch / 45.0F + 0.1F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        f = -MathHelper.cos(f * (float) Math.PI) * 0.5F + 0.5F;
        return f;
    }

}
