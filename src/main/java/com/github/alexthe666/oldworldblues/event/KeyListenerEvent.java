package com.github.alexthe666.oldworldblues.event;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyListenerEvent {

    @SubscribeEvent
    public void onKeyboardInteraction(InputEvent.KeyInputEvent event) {
       /* EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
        if(OWBKeybinds.VATS_KEYBIND.isKeyDown()) {
            if (!renderer.isShaderActive()) {
                renderer.loadShader(new ResourceLocation("vats:shaders/post/vats.json"));
            } else {
                renderer.stopUseShader();
            }
        }*/
    }
}
