package com.github.alexthe666.oldworldblues.event;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.client.gui.GuiVATS;
import com.github.alexthe666.oldworldblues.client.gui.GuiVATSCombat;
import com.github.alexthe666.oldworldblues.client.render.entity.ICustomVATSRender;
import com.github.alexthe666.oldworldblues.client.render.entity.layer.LayerVATSRender;
import com.github.alexthe666.oldworldblues.client.render.entity.layer.LayerVaultJumpsuitNumber;
import com.github.alexthe666.oldworldblues.entity.VATSProperties;
import com.github.alexthe666.oldworldblues.init.OWBSounds;
import com.github.alexthe666.oldworldblues.keybinding.OWBKeybinds;
import com.github.alexthe666.oldworldblues.message.MessageTriggerVats;
import com.github.alexthe666.oldworldblues.util.VATSUtil;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.Map;

public class VATSClientEvents {

    @SubscribeEvent
    public void onFOVUpdate(FOVUpdateEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        if(properties != null && properties.isVatsTriggered()){
            float distance = properties.currentTarget == null ? 1.5F : MathHelper.clamp(player.getDistance(properties.currentTarget) * 0.5F, 1, 10);
            event.setNewfov(-distance);
        }
    }


    @SubscribeEvent
    public void onEntityHearSound(PlaySoundAtEntityEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        if(properties != null && properties.isVatsTriggered() && !event.getSound().getSoundName().getNamespace().equals(OldWorldBlues.MODID) && !event.getSound().getSoundName().getPath().contains("vats")){
            event.setPitch(event.getDefaultPitch() - 0.75F);
        }
    }
    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        if(Minecraft.getMinecraft().currentScreen instanceof GuiVATSCombat) {
            if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 1) {
                GL11.glTranslatef(2F, 0F, 2F);
            }
            if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2) {
                GL11.glRotatef(45, 0, 1, 0);
            }
        }
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if(event.getEntityLiving() instanceof EntityPlayer){
            EntityPlayer player = (EntityPlayer)event.getEntityLiving();
            VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
            if(properties != null) {
                if (player.world.isRemote && OWBKeybinds.VATS_KEYBIND.isPressed() && !properties.isVatsTriggered() && properties.ticksAfterVats == 0) {
                    if (VATSUtil.isWearingPipBoy(player)) {
                        properties.setVatsTriggered(true);
                        Minecraft.getMinecraft().displayGuiScreen(new GuiVATS());
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(OWBSounds.ENTER_VATS, 1.0F));
                        OldWorldBlues.NETWORK_WRAPPER.sendToServer(new MessageTriggerVats(true, false));
                        return;
                    } else {
                        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(OWBSounds.VATS_DESELECT_TARGET, 1.0F));
                        properties.setVatsTriggered(false);
                        OldWorldBlues.NETWORK_WRAPPER.sendToServer(new MessageTriggerVats(false, false));
                    }
                }
                if (player.world.isRemote && OWBKeybinds.VATS_KEYBIND.isKeyDown() && properties.isVatsTriggered() && properties.vatsTicks > 10) {
                    properties.setVatsTriggered(false);
                    Minecraft.getMinecraft().displayGuiScreen(null);
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(OWBSounds.LEAVE_VATS, 1.0F));
                    OldWorldBlues.NETWORK_WRAPPER.sendToServer(new MessageTriggerVats(false, false));
                    return;
                }

                if (properties.isCombatTriggered) {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiVATSCombat());
                } else if (Minecraft.getMinecraft().currentScreen instanceof GuiVATSCombat) {
                    Minecraft.getMinecraft().displayGuiScreen(null);
                    Minecraft.getMinecraft().gameSettings.thirdPersonView = properties.previousViewPoint;
                }
            }
        }
    }

    public static void initializeVATSRenderLayer() {
        for (Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entry : Minecraft.getMinecraft().getRenderManager().entityRenderMap.entrySet()) {
            Render render = entry.getValue();
            if (render instanceof RenderLivingBase && EntityLivingBase.class.isAssignableFrom(entry.getKey())) {
                ((RenderLivingBase) render).addLayer(new LayerVATSRender((RenderLivingBase) render));
                ((RenderLivingBase) render).addLayer(new LayerVaultJumpsuitNumber((RenderLivingBase)render));
            }
        }

        Field renderingRegistryField = ReflectionHelper.findField(RenderingRegistry.class, ObfuscationReflectionHelper.remapFieldNames(RenderingRegistry.class.getName(), new String[]{"INSTANCE", "INSTANCE"}));
        Field entityRendersField = ReflectionHelper.findField(RenderingRegistry.class, ObfuscationReflectionHelper.remapFieldNames(RenderingRegistry.class.getName(), new String[]{"entityRenderers", "entityRenderers"}));
        Field entityRendersOldField = ReflectionHelper.findField(RenderingRegistry.class, ObfuscationReflectionHelper.remapFieldNames(RenderingRegistry.class.getName(), new String[]{"entityRenderersOld", "entityRenderersOld"}));
        RenderingRegistry registry = null;
        try {
            Field modifier = Field.class.getDeclaredField("modifiers");
            modifier.setAccessible(true);
            registry = (RenderingRegistry) renderingRegistryField.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (registry != null) {
            Map<Class<? extends Entity>, IRenderFactory<? extends Entity>> entityRenders = null;
            Map<Class<? extends Entity>, Render<? extends Entity>> entityRendersOld = null;
            try {
                Field modifier1 = Field.class.getDeclaredField("modifiers");
                modifier1.setAccessible(true);
                entityRenders = (Map<Class<? extends Entity>, IRenderFactory<? extends Entity>>) entityRendersField.get(registry);
                entityRendersOld = (Map<Class<? extends Entity>, Render<? extends Entity>>) entityRendersOldField.get(registry);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (entityRenders != null) {
                for (Map.Entry<Class<? extends Entity>, IRenderFactory<? extends Entity>> entry : entityRenders.entrySet()) {
                    if (entry.getValue() != null) {
                        try{
                            Render render = entry.getValue().createRenderFor(Minecraft.getMinecraft().getRenderManager());
                            if (render != null && render instanceof RenderLivingBase && EntityLivingBase.class.isAssignableFrom(entry.getKey())) {
                                LayerRenderer vatsLayer = render instanceof ICustomVATSRender ? ((ICustomVATSRender)render).getVATSLayer((RenderLivingBase) render) : new LayerVATSRender((RenderLivingBase) render);
                                ((RenderLivingBase) render).addLayer(new LayerVaultJumpsuitNumber((RenderLivingBase)render));
                                ((RenderLivingBase) render).addLayer(vatsLayer);
                            }
                        }catch(NullPointerException exp){
                            System.out.println("Old World Blues: Could not apply VATS render layer to " + entry.getKey().getSimpleName() + ", someone isn't registering their renderer properly... <.<");
                        }
                    }

                }
            }
            if (entityRendersOld != null) {
                for (Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entry : entityRendersOld.entrySet()) {
                    Render render = entry.getValue();
                    if (render instanceof RenderLivingBase && EntityLivingBase.class.isAssignableFrom(entry.getKey())) {
                        LayerRenderer vatsLayer = render instanceof ICustomVATSRender ? ((ICustomVATSRender)render).getVATSLayer((RenderLivingBase) render) : new LayerVATSRender((RenderLivingBase) render);
                        ((RenderLivingBase) render).addLayer(new LayerVaultJumpsuitNumber((RenderLivingBase)render));
                        ((RenderLivingBase) render).addLayer(vatsLayer);
                    }
                }
            }
        }
        for(String type : Minecraft.getMinecraft().getRenderManager().getSkinMap().keySet()){
            RenderPlayer renderPlayer = Minecraft.getMinecraft().getRenderManager().getSkinMap().get(type);
            renderPlayer.addLayer(new LayerVaultJumpsuitNumber(renderPlayer));
        }

    }

    private static final ResourceLocation WIDGETS = new ResourceLocation("oldworldblues:textures/gui/widgets.png");
    private static final ResourceLocation VIGNETTE = new ResourceLocation("oldworldblues:textures/gui/vignette.png");

    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) return;
        EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().player;
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        if(properties != null && (properties.isVatsTriggered() || properties.isCombatTriggered)){
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableAlpha();
            Minecraft.getMinecraft().getTextureManager().bindTexture(VIGNETTE);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(0.0D, (double) res.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
            bufferbuilder.pos((double) res.getScaledWidth(), (double) res.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
            bufferbuilder.pos((double) res.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
            bufferbuilder.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
            tessellator.draw();
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableAlpha();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
        ScaledResolution scaled = new ScaledResolution(Minecraft.getMinecraft());
        int width = scaled.getScaledWidth();
        int height = scaled.getScaledHeight();
        drawCenteredString(Minecraft.getMinecraft().fontRenderer, "AP", (int)(width - (width * 0.2F)), (int)(height - (height * 0.05F)), 0X14FD15);
        Minecraft.getMinecraft().getTextureManager().bindTexture(WIDGETS);
        drawTexturedModalRect((int)(width - (width * 0.18F)), (int)(height - (height * 0.05F)), 0, 0, 76, 9);
        if(properties != null){
            drawTexturedModalRect((int)(width - (width * 0.18F)) + 2, (int)(height - (height * 0.05F)), 2, 9, 72 - (int)(72F * ((100 - properties.currentAP) / (float)properties.maxAP)), 7);
        }

    }

    private void drawCenteredString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        fontRendererIn.drawStringWithShadow(text, (float)(x - fontRendererIn.getStringWidth(text) / 2), (float)y, color);
    }

    public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)(x + 0), (double)(y + height), (double)100).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), (double)100).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + height) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + 0), (double)100).tex((double)((float)(textureX + width) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + 0), (double)(y + 0), (double)100).tex((double)((float)(textureX + 0) * 0.00390625F), (double)((float)(textureY + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }
}
