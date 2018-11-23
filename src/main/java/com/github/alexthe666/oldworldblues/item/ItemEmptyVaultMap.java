package com.github.alexthe666.oldworldblues.item;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.world.OWBWorldData;
import com.github.alexthe666.oldworldblues.world.VaultData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemEmptyVaultMap extends Item {

    public ItemEmptyVaultMap(){
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setUnlocalizedName("oldworldblues.vault_map");
        this.setRegistryName(OldWorldBlues.MODID, "vault_map");
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        VaultData data = OWBWorldData.get(worldIn).getNearestVault(new BlockPos(playerIn), 10000);
        ItemStack itemstack1 = playerIn.getHeldItem(handIn);
        if(data == null || data.getEnterence() == null){
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack1);
        }
        ItemStack itemstack = ItemVaultMap.setupNewMap(worldIn, data.getEnterence().getX(), data.getEnterence().getZ(), (byte) 2, true, true);
        itemstack1.shrink(1);

        if (itemstack1.isEmpty()) {
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        } else {
            if (!playerIn.inventory.addItemStackToInventory(itemstack.copy())) {
                playerIn.dropItem(itemstack, false);
            }

            playerIn.addStat(StatList.getObjectUseStats(this));
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack1);
        }
    }
}
