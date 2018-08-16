package com.github.alexthe666.oldworldblues.item;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.entity.EntityVaultTecPoster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemVaultTecPoster extends Item {

    public ItemVaultTecPoster(){
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setUnlocalizedName("oldworldblues.vault_tec_poster");
        this.setRegistryName(OldWorldBlues.MODID, "vault_tec_poster");
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        BlockPos placePos = pos.offset(facing);
        if (facing != EnumFacing.DOWN && facing != EnumFacing.UP && player.canPlayerEdit(placePos, facing, player.getHeldItem(hand))) {
            EntityVaultTecPoster entity = new EntityVaultTecPoster(world, placePos, facing);
            if (entity.onValidSurface()) {
                if (!world.isRemote) {
                    entity.playPlaceSound();
                    world.spawnEntity(entity);
                }
                player.getHeldItem(hand).shrink(1);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.FAIL;
    }
}
