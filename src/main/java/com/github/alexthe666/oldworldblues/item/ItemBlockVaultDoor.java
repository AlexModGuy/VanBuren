package com.github.alexthe666.oldworldblues.item;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockVaultDoor extends ItemBlock {

    public ItemBlockVaultDoor(Block block) {
        super(block);
    }

    public static ItemStack createStackFromNumber(ItemStack input, int number){
        input.setTagCompound(new NBTTagCompound());
        input.getTagCompound().setInteger("Number", Math.min(number, 1000));
        return input;
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == this.getCreativeTab()) {
            items.add(createStackFromNumber(new ItemStack(this), 1000));
            items.add(createStackFromNumber(new ItemStack(this), 13));
            items.add(createStackFromNumber(new ItemStack(this), 22));
            items.add(createStackFromNumber(new ItemStack(this), 101));
            items.add(createStackFromNumber(new ItemStack(this), 111));
            items.add(createStackFromNumber(new ItemStack(this), 76));
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int f, boolean f1) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setInteger("Number", 1000);
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.getTagCompound() != null && stack.getTagCompound().getInteger("Number") < 1000) {
            tooltip.add(I18n.format("oldworldblues.vault_number") + " " + stack.getTagCompound().getInteger("Number"));
        }
    }
}