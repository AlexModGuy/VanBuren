package com.github.alexthe666.oldworldblues.keybinding;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class OWBKeybinds {

    public static final KeyBinding VATS_KEYBIND = new KeyBinding("key.vats", Keyboard.KEY_V, "key.categories.gameplay");
    public static final KeyBinding LEAVE_VATS_KEYBIND = new KeyBinding("key.leave_vats", Keyboard.KEY_TAB, "key.categories.gameplay");

    public static void register(){
        ClientRegistry.registerKeyBinding(VATS_KEYBIND);
        ClientRegistry.registerKeyBinding(LEAVE_VATS_KEYBIND);
    }

}
