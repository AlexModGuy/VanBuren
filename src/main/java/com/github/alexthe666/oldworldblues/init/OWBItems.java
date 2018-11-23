package com.github.alexthe666.oldworldblues.init;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.item.*;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class OWBItems {

    public static ItemArmor.ArmorMaterial VAULT_JUMPSUIT_ARMOR = EnumHelper.addArmorMaterial("VaultJumpsuit", "oldworldblues:vault_jumpsuit", 10, new int[]{2, 3, 4, 2}, 10, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0);
    public static ItemArmor.ArmorMaterial VAULT_SECURITY_ARMOR = EnumHelper.addArmorMaterial("VaultSecurity", "oldworldblues:vault_security", 20, new int[]{4, 6, 8, 4}, 10, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 1);

    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":pipBoy3000IV")
    public static final Item PIPBOY3000IV = new ItemPipBoy("3000IV");
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":number_decal")
    public static final Item NUMBER_DECAL = new ItemNumberDecal();

    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_jumpsuit_chest")
    public static final Item VAULT_JUMPSUIT_CHEST = new ItemVaultJumpsuit(VAULT_JUMPSUIT_ARMOR, 1, EntityEquipmentSlot.CHEST, "vault_jumpsuit_chest");
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_jumpsuit_legs")
    public static final Item VAULT_JUMPSUIT_LEGS = new ItemVaultJumpsuit(VAULT_JUMPSUIT_ARMOR, 2, EntityEquipmentSlot.LEGS, "vault_jumpsuit_legs");
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_jumpsuit_feet")
    public static final Item VAULT_JUMPSUIT_FEET = new ItemVaultJumpsuit(VAULT_JUMPSUIT_ARMOR, 3, EntityEquipmentSlot.FEET, "vault_jumpsuit_feet");


    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_security_helmet")
    public static final Item VAULT_SECURITY_HELMET = new ItemVaultJumpsuit(VAULT_SECURITY_ARMOR, 0, EntityEquipmentSlot.HEAD, "vault_security_helmet");
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_security_chest")
    public static final Item VAULT_SECURITY_CHEST = new ItemVaultJumpsuit(VAULT_SECURITY_ARMOR, 1, EntityEquipmentSlot.CHEST, "vault_security_chest");
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_security_legs")
    public static final Item VAULT_SECURITY_LEGS = new ItemVaultJumpsuit(VAULT_SECURITY_ARMOR, 2, EntityEquipmentSlot.LEGS, "vault_security_legs");
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_security_feet")
    public static final Item VAULT_SECURITY_FEET = new ItemVaultJumpsuit(VAULT_SECURITY_ARMOR, 3, EntityEquipmentSlot.FEET, "vault_security_feet");

    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_tec_poster")
    public static final Item VAULT_TEC_POSTER = new ItemVaultTecPoster();

    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_map")
    public static final Item VAULT_MAP = new ItemEmptyVaultMap();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":filled_vault_map")
    public static final Item FILLED_VAULT_MAP = new ItemVaultMap();
}
