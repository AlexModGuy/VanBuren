package com.github.alexthe666.oldworldblues.recipe;

import com.github.alexthe666.oldworldblues.block.BlockVaultDoor;
import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import com.github.alexthe666.oldworldblues.init.OWBItems;
import com.github.alexthe666.oldworldblues.item.ItemBlockVaultDoor;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeVaultDoorNumber extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    private int number = 1000;

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        String numberTemp = "";
        int doorCount = 0;
        for(int i = 0; i < 9; i++){
            if(inv.getStackInSlot(i).getItem() == Item.getItemFromBlock(OWBBlocks.VAULT_DOOR)){
                doorCount++;
            }
        }

        for(int i = 0; i < 9; i++){
            if(inv.getStackInSlot(i).getItem() == OWBItems.NUMBER_DECAL){
                numberTemp = numberTemp + inv.getStackInSlot(i).getMetadata();
            }else if(!inv.getStackInSlot(i).isEmpty() && inv.getStackInSlot(i).getItem() != Item.getItemFromBlock(OWBBlocks.VAULT_DOOR) || doorCount == 0){
                return false;
            }
        }

        if(doorCount == 1 && !numberTemp.isEmpty()){
            int parsed = Integer.parseInt(numberTemp);
            if(parsed > -1 && parsed < 1000){
                number = parsed;
            }
            return true;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack stack = ItemBlockVaultDoor.createStackFromNumber(new ItemStack(OWBBlocks.VAULT_DOOR), number);
        return stack;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        ItemStack stack = ItemBlockVaultDoor.createStackFromNumber(new ItemStack(OWBBlocks.VAULT_DOOR), number);
        return stack;
    }
}
