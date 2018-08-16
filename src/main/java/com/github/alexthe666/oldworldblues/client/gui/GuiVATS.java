package com.github.alexthe666.oldworldblues.client.gui;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.entity.VATSProperties;
import com.github.alexthe666.oldworldblues.init.OWBSounds;
import com.github.alexthe666.oldworldblues.keybinding.OWBKeybinds;
import com.github.alexthe666.oldworldblues.message.MessageAddTargetToMap;
import com.github.alexthe666.oldworldblues.message.MessageTriggerVats;
import com.github.alexthe666.oldworldblues.message.MessageUseAP;
import com.github.alexthe666.oldworldblues.message.MessageVatsTarget;
import com.github.alexthe666.oldworldblues.util.VATSUtil;
import com.google.common.collect.Maps;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public class GuiVATS extends GuiScreen {

    private static final ResourceLocation WIDGETS = new ResourceLocation("oldworldblues:textures/gui/widgets.png");
    private Map<Entity, Integer> targetPoints = Maps.newHashMap();
    private Map<Entity, Integer> targetChances = Maps.newHashMap();
    private int targetCount = 0;
    public GuiVATS() {
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().player;
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        if(properties.currentTarget != null){
            targetChances.put(properties.currentTarget, VATSUtil.getVATSChanceForEntity(player, properties.currentTarget));
            GL11.glPushMatrix();
            Minecraft.getMinecraft().getTextureManager().bindTexture(WIDGETS);
            if(targetCount > 0){
                for(int i = 0; i < targetCount; i++){
                    float div = VATSUtil.getPlayerHeldAP(player) / (float)properties.maxAP;
                    drawAPSegment((int)(width - (width * 0.18F)) + 2 + (i * (((int)(69F * div)) + 1)), (int)(height - (height * 0.05F)) + 1, (int)(69F * div));
                }
            }
            GL11.glPushMatrix();
            GL11.glScalef(1.5F, 1.5F, 1.5F);
            drawTexturedModalRect((int)(width / 3) - 14, (int)(height / 3) - 13, 0, 50, 31, 26);
            for(int i = 0; i < targetPoints.getOrDefault(properties.currentTarget, 0); i++){
                drawTexturedModalRect((int)(width / 3) - 18 - (8 * i), (int)(height / 3) - 15, 31, 50, 7, 30);
                drawTexturedModalRect((int)(width / 3) + 15 + (8 * i), (int)(height / 3) - 15, 38, 50, 7, 30);
            }
            GL11.glPopMatrix();
            drawTexturedModalRect((int)((width - 153) / 2), 25, 76, 0, 152, 18);
            if(properties.currentTarget instanceof EntityLivingBase){
                EntityLivingBase living = (EntityLivingBase)properties.currentTarget;
                float health = ((living.getMaxHealth() - living.getHealth()) / living.getMaxHealth());
                float healthPossible = ((living.getMaxHealth() - Math.min(6 * targetPoints.getOrDefault(properties.currentTarget, 0), living.getHealth())) / living.getMaxHealth());
                float transparency = MathHelper.cos(player.ticksExisted * 0.5F + 1) + 1;
                drawTexturedModalRect((int)((width - 153) / 2), 25, 76, 36, 152 - (int)(152F * health), 18);
                GL11.glPushMatrix();
                GlStateManager.enableNormalize();
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                GlStateManager.color(1.0F, 1.0F, 1.0F, transparency * 0.75F);
                drawTexturedModalRect((int)((width - 153) / 2) + (int)(144F * (healthPossible - health)) + 4, 25, 80, 18, 144 - (int)(144F * healthPossible), 14);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableBlend();
                GlStateManager.disableNormalize();
                GL11.glPopMatrix();
            }else{
                drawTexturedModalRect((int)((width - 153) / 2), 25, 76, 36, 152, 18);
            }
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(1.5F, 1.5F, 1.5F);
            drawCenteredString(Minecraft.getMinecraft().fontRenderer, "" + properties.currentTarget.getDisplayName().getFormattedText().toUpperCase(), (int)(width / 3) + 1, 5, 0XDE5D48);
            drawCenteredString(Minecraft.getMinecraft().fontRenderer, "" + targetChances.getOrDefault(properties.currentTarget, 0), (int)(width / 3) + 1, (int)(height / 3) - 5, 0X14FD15);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glPopMatrix();
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        setRenderHand(false);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public void onGuiClosed() {
        EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().player;
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        properties.setVatsTriggered(false);
        if(properties != null && targetCount > 0) {
            properties.isCombatTriggered = true;
            properties.combatIndex = 0;
            properties.currentAP -= VATSUtil.getPlayerHeldAP(player) * targetCount;
            properties.previousViewPoint = Minecraft.getMinecraft().gameSettings.thirdPersonView;
            properties.targetPoints = Maps.newHashMap(targetPoints);
            properties.targetChances = Maps.newHashMap(targetChances);
            OldWorldBlues.NETWORK_WRAPPER.sendToServer(new MessageUseAP(VATSUtil.getPlayerHeldAP(player) * targetCount));
            for(Entity e : targetPoints.keySet()){
                OldWorldBlues.NETWORK_WRAPPER.sendToServer(new MessageAddTargetToMap(e.getEntityId(), targetPoints.get(e).intValue(), targetChances.get(e).intValue()));
            }
        }
        OldWorldBlues.NETWORK_WRAPPER.sendToServer(new MessageTriggerVats(false,targetCount > 0));
        setRenderHand(true);
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(OWBSounds.LEAVE_VATS, 1.0F));
    }

    private void drawAPSegment(int x, int y, int size){
        drawTexturedModalRect(x, y, 0, 18, size, 5);
        drawTexturedModalRect(x + size, y, 69, 18, 1, 5);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
        EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().player;
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        if(properties.currentTarget != null){
            if(mouseButton == 0 && targetCount < properties.currentAP / VATSUtil.getPlayerHeldAP(player)){
                targetPoints.put(properties.currentTarget, targetPoints.getOrDefault(properties.currentTarget, 0) + 1);
                targetCount++;
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(OWBSounds.VATS_SELECT_TARGET, 1.0F));
            }
            if(mouseButton == 1 && targetCount > 0){
                targetPoints.put(properties.currentTarget, Math.max(0, targetPoints.getOrDefault(properties.currentTarget, 0) - 1));
                targetCount--;
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(OWBSounds.VATS_DESELECT_TARGET, 1.0F));
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException{
        super.keyTyped(typedChar, keyCode);
        EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().player;
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        if(properties != null) {
            if (GameSettings.isKeyDown(OWBKeybinds.VATS_KEYBIND)) {
                properties.setVatsTriggered(false);
                Minecraft.getMinecraft().displayGuiScreen(null);
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(OWBSounds.LEAVE_VATS, 1.0F));
                OldWorldBlues.NETWORK_WRAPPER.sendToServer(new MessageTriggerVats(false, this.targetCount > 0));

            }

            if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindRight)) {
                properties.shiftTarget(true);
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(OWBSounds.VATS_CHANGE_TARGET, 1.0F));
                OldWorldBlues.NETWORK_WRAPPER.sendToServer(new MessageVatsTarget(true));
            }

            if (GameSettings.isKeyDown(this.mc.gameSettings.keyBindLeft)) {
                properties.shiftTarget(false);
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(OWBSounds.VATS_CHANGE_TARGET, 1.0F));
                OldWorldBlues.NETWORK_WRAPPER.sendToServer(new MessageVatsTarget(false));
            }
        }
    }

    private void drawAPRedBar(int x, int y, int width, int height){
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double)(x + 0), (double)(y + height), (double)this.zLevel).tex((double)((float)(0 + 0) * 0.00390625F), (double)((float)(3 + 5) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + height), (double)this.zLevel).tex((double)((float)(0 + 8) * 0.00390625F), (double)((float)(3 + 5) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + width), (double)(y + 0), (double)this.zLevel).tex((double)((float)(0 + 8) * 0.00390625F), (double)((float)(3 + 0) * 0.00390625F)).endVertex();
        bufferbuilder.pos((double)(x + 0), (double)(y + 0), (double)this.zLevel).tex((double)((float)(0 + 0) * 0.00390625F), (double)((float)(3 + 0) * 0.00390625F)).endVertex();
        tessellator.draw();
    }

    public static boolean getRenderHand() {
        Field field = ReflectionHelper.findField(EntityRenderer.class, ObfuscationReflectionHelper.remapFieldNames(EntityRenderer.class.getName(), new String[]{"renderHand", "field_175074_C"}));
        try {
            Field modifier = Field.class.getDeclaredField("modifiers");
            modifier.setAccessible(true);
            modifier.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            return (boolean)field.get(Minecraft.getMinecraft().entityRenderer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void setRenderHand(boolean renderHand){
        Field field = ReflectionHelper.findField(EntityRenderer.class, ObfuscationReflectionHelper.remapFieldNames(EntityRenderer.class.getName(), new String[]{"renderHand", "field_175074_C"}));
        try {
            Field modifier = Field.class.getDeclaredField("modifiers");
            modifier.setAccessible(true);
            modifier.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.setBoolean(Minecraft.getMinecraft().entityRenderer, renderHand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
