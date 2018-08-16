package com.github.alexthe666.oldworldblues.client.gui;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.entity.VATSProperties;
import com.github.alexthe666.oldworldblues.init.OWBSounds;
import com.github.alexthe666.oldworldblues.keybinding.OWBKeybinds;
import com.github.alexthe666.oldworldblues.message.MessageTriggerVats;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;

import java.io.IOException;

public class GuiVATSCombat extends GuiScreen {

    int ticksOpen = 0;

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public void onGuiClosed() {
        EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().player;
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        if(properties != null && ticksOpen > 5){
            properties.setVatsTriggered(false);
            properties.isCombatTriggered = false;
            OldWorldBlues.NETWORK_WRAPPER.sendToServer(new MessageTriggerVats(false,false));
        }

    }

    public void updateScreen() {
        ticksOpen++;
        EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().player;
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        if(properties != null && ticksOpen > 5) {
            if (GameSettings.isKeyDown(OWBKeybinds.VATS_KEYBIND)) {
                properties.setVatsTriggered(false);
                properties.isCombatTriggered = false;
                Minecraft.getMinecraft().displayGuiScreen(null);
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(OWBSounds.LEAVE_VATS, 1.0F));
                OldWorldBlues.NETWORK_WRAPPER.sendToServer(new MessageTriggerVats(false, false));

            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().player;
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        if(properties != null && ticksOpen > 5) {
            if (GameSettings.isKeyDown(OWBKeybinds.VATS_KEYBIND)) {
                properties.setVatsTriggered(false);
                properties.isCombatTriggered = false;
                Minecraft.getMinecraft().displayGuiScreen(null);
                Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(OWBSounds.LEAVE_VATS, 1.0F));
                OldWorldBlues.NETWORK_WRAPPER.sendToServer(new MessageTriggerVats(false, false));

            }
        }
    }

}
