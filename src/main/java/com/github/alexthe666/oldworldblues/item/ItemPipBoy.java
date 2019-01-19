package com.github.alexthe666.oldworldblues.item;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.init.OWBItems;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;

public class ItemPipBoy extends Item {
    public ItemPipBoy(String version){
        this.maxStackSize = 1;
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setTranslationKey("oldworldblues.pipboy" + version);
        this.setRegistryName(OldWorldBlues.MODID, "pipboy" + version);
    }
}
