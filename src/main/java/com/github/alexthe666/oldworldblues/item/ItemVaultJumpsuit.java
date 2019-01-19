package com.github.alexthe666.oldworldblues.item;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.init.OWBItems;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemVaultJumpsuit extends ItemArmor implements IVaultJumpsuit {
    public ItemVaultJumpsuit(ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot, String name) {
        super(material, renderIndex, slot);
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setTranslationKey("oldworldblues." + name);
        this.setRegistryName(OldWorldBlues.MODID, name);
    }

    @SideOnly(Side.CLIENT)
    public net.minecraft.client.model.ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, net.minecraft.client.model.ModelBiped _default) {
        return (ModelBiped) OldWorldBlues.PROXY.getArmorModel(entityLiving, renderIndex, isSecurity() ? 4 : 0);
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return OldWorldBlues.PROXY.getVaultJumpsuitTexture(entity, renderIndex, isSecurity() ? 1 : 0);
    }

    public boolean isSecurity(){
        return this.getArmorMaterial() == OWBItems.VAULT_SECURITY_ARMOR;
    }

    public static ItemStack createStackFromNumber(ItemStack input, int number){
        input.setTagCompound(new NBTTagCompound());
        input.getTagCompound().setInteger("Number", Math.min(number, 1000));
        return input;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int f, boolean f1) {
        if(this.getEquipmentSlot() == EntityEquipmentSlot.CHEST) {
            if (stack.getTagCompound() == null) {
                stack.setTagCompound(new NBTTagCompound());
                stack.getTagCompound().setInteger("Number", 1000);
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if(this.getEquipmentSlot() == EntityEquipmentSlot.CHEST) {
            if (stack.getTagCompound() != null && stack.getTagCompound().getInteger("Number") < 1000) {
                tooltip.add(I18n.format("oldworldblues.vault_number") + " " + stack.getTagCompound().getInteger("Number"));
            }
        }else{
            super.addInformation(stack, worldIn, tooltip, flagIn);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == this.getCreativeTab()) {
            if(this.getEquipmentSlot() == EntityEquipmentSlot.CHEST) {
                items.add(createStackFromNumber(new ItemStack(this), 1000));
                items.add(createStackFromNumber(new ItemStack(this), 13));
                items.add(createStackFromNumber(new ItemStack(this), 22));
                items.add(createStackFromNumber(new ItemStack(this), 101));
                items.add(createStackFromNumber(new ItemStack(this), 111));
                items.add(createStackFromNumber(new ItemStack(this), 76));
            }else{
                super.getSubItems(tab, items);
            }
        }
    }
}
