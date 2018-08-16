package com.github.alexthe666.oldworldblues.recipe;

import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import com.github.alexthe666.oldworldblues.init.OWBItems;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeNumberDecal extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    private int number;

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        number = 0;
        for(int i = 0; i < 9; i++){
            if(inv.getStackInSlot(i).getItem() == Item.getItemFromBlock(OWBBlocks.VAULT_DOOR)){
                return false;
            }
            if(inv.getStackInSlot(i).getItem() == OWBItems.NUMBER_DECAL){
                number += inv.getStackInSlot(i).getMetadata();
            }else if(!inv.getStackInSlot(i).isEmpty()){
                number = 0;
                return false;
            }
        }
        ;
        return number > 0;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack stack =  new ItemStack(OWBItems.NUMBER_DECAL, 1, Math.min(number, 9));
        number = 0;
        return stack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        ItemStack stack =  new ItemStack(OWBItems.NUMBER_DECAL, 1, Math.min(number, 9));
        number = 0;
        return stack;
    }
}
