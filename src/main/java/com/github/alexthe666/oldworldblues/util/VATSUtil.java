package com.github.alexthe666.oldworldblues.util;

import com.github.alexthe666.oldworldblues.init.ActionPointMappings;
import com.github.alexthe666.oldworldblues.item.ItemPipBoy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class VATSUtil {
    public static int getVATSChanceForEntity(EntityPlayer player, Entity target){
        float dist = player.getDistance(target);
        return Math.max(0, (1000 - (int)dist * 10) / 10);
    }

    public static int getPlayerHeldAP(EntityPlayer player){
        ItemStack heldStack = player.getHeldItemMainhand();
        int ap = ActionPointMappings.INSTANCE.getItemAP(heldStack);
        return ap == 0 ? 20 : ap;
    }

    public static boolean isWearingPipBoy(EntityLivingBase living){
        return living.getHeldItemMainhand().getItem() instanceof ItemPipBoy || living.getHeldItemOffhand().getItem() instanceof ItemPipBoy;
    }
}
