package com.github.alexthe666.oldworldblues.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.github.alexthe666.oldworldblues.OldWorldBlues.MODID;

@SuppressWarnings("WeakerAccess")
@GameRegistry.ObjectHolder(MODID)
public class OWBSounds {

    @GameRegistry.ObjectHolder("vats_start")
    public static final SoundEvent ENTER_VATS = createSoundEvent("vats_start");

    @GameRegistry.ObjectHolder("vats_exit")
    public static final SoundEvent LEAVE_VATS = createSoundEvent("vats_exit");

    @GameRegistry.ObjectHolder("vats_select_target")
    public static final SoundEvent VATS_SELECT_TARGET = createSoundEvent("vats_select_target");

    @GameRegistry.ObjectHolder("vats_deselect_target")
    public static final SoundEvent VATS_DESELECT_TARGET = createSoundEvent("vats_deselect_target");

    @GameRegistry.ObjectHolder("vats_change_target")
    public static final SoundEvent VATS_CHANGE_TARGET = createSoundEvent("vats_change_target");

    @GameRegistry.ObjectHolder("vats_critical")
    public static final SoundEvent VATS_CRITICAL = createSoundEvent("vats_critical");

    @GameRegistry.ObjectHolder("vats_critical_full")
    public static final SoundEvent VATS_CRITICAL_BAR_FULL = createSoundEvent("vats_critical_full");

    @GameRegistry.ObjectHolder("salesman_villager_idle")
    public static final SoundEvent SALESMAN_VILLAGER_IDLE = createSoundEvent("salesman_villager_idle");

    @GameRegistry.ObjectHolder("salesman_villager_hurt")
    public static final SoundEvent SALESMAN_VILLAGER_HURT = createSoundEvent("salesman_villager_hurt");

    @GameRegistry.ObjectHolder("salesman_villager_die")
    public static final SoundEvent SALESMAN_VILLAGER_DIE = createSoundEvent("salesman_villager_die");

    @GameRegistry.ObjectHolder("salesman_villager_trade")
    public static final SoundEvent SALESMAN_VILLAGER_TRADE = createSoundEvent("salesman_villager_trade");

    @GameRegistry.ObjectHolder("vault_door_open")
    public static final SoundEvent VAULT_DOOR_OPEN = createSoundEvent("vault_door_open");

    @GameRegistry.ObjectHolder("vault_door_slide")
    public static final SoundEvent VAULT_DOOR_SLIDE = createSoundEvent("vault_door_slide");

    @GameRegistry.ObjectHolder("interior_vault_door_open")
    public static final SoundEvent INTERIOR_VAULT_DOOR_OPEN = createSoundEvent("interior_vault_door_open");

    private static SoundEvent createSoundEvent(final String soundName) {
        final ResourceLocation soundID = new ResourceLocation("oldworldblues:" + soundName);
        return new SoundEvent(soundID).setRegistryName(soundID);
    }
}
