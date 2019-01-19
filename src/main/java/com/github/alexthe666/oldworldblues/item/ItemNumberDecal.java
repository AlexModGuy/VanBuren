package com.github.alexthe666.oldworldblues.item;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemNumberDecal extends Item implements ISpecialItemRender{

    public ItemNumberDecal(){
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setTranslationKey("oldworldblues.number_decal");
        this.setRegistryName(OldWorldBlues.MODID, "number_decal");
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn){
        tooltip.add(I18n.format("oldworldblues.crafting_ingredient"));

    }

    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey() + "_" + Math.min(stack.getMetadata(), 9);
    }

    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for(int i = 0; i < 10; i++){
                items.add(new ItemStack(this, 1, i));
            }
        }
    }
}
