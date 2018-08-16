package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.block.entity.TileEntityInteriorVaultDoor;
import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import com.github.alexthe666.oldworldblues.util.VATSUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class BlockVaultDoorAccess extends BlockGenericDecoration {
    public static AxisAlignedBB AABB_SOUTH = OWBBlocks.generateAABBFromPixels(1F, 0F, 7F, 15F, 20F, 16F);
    public static AxisAlignedBB AABB_NORTH = OWBBlocks.generateAABBFromPixels(1F, 0F, 0F, 15F, 20F, 9F);
    public static AxisAlignedBB AABB_WEST = OWBBlocks.generateAABBFromPixels(7F, 0F, 1F, 16F, 20F, 15F);
    public static AxisAlignedBB AABB_EAST = OWBBlocks.generateAABBFromPixels(1F, 0F, 15F, 7F, 20F, 1F);

    public BlockVaultDoorAccess() {
        super("vault_door_access",  Material.IRON, SoundType.METAL, false, AABB_NORTH, AABB_SOUTH, AABB_WEST, AABB_EAST);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(VATSUtil.isWearingPipBoy(playerIn)) {
            for(int i = 0; i < 3; i++) {
                for (EnumFacing facing1 : EnumFacing.VALUES) {
                    if (worldIn.getBlockState(pos.offset(facing1, i)).getBlock() instanceof BlockVaultDoor) {
                        BlockVaultDoor vault_door = (BlockVaultDoor) worldIn.getBlockState(pos.offset(facing1, i)).getBlock();
                        vault_door.openDoor(worldIn, pos.offset(facing1, i));
                    }
                    if (worldIn.getBlockState(pos.offset(facing1, i)).getBlock() instanceof BlockVaultDoorFrame) {
                        BlockVaultDoorFrame vault_door = (BlockVaultDoorFrame) worldIn.getBlockState(pos.offset(facing1, i)).getBlock();
                        vault_door.openDoor(worldIn, pos.offset(facing1, i));
                    }
                }
            }
            return true;
        }else{
            playerIn.sendStatusMessage(new TextComponentTranslation("oldworldblues.needpipboy"), true);
            return true;
        }
    }
    private void openDoor(World worldIn, BlockPos pos){
        if (worldIn.getBlockState(pos).getBlock() instanceof BlockVaultDoor) {
            BlockVaultDoor vault_door = (BlockVaultDoor) worldIn.getBlockState(pos).getBlock();
            vault_door.openDoor(worldIn, pos);
        }
        if (worldIn.getBlockState(pos).getBlock() instanceof BlockVaultDoorFrame) {
            BlockVaultDoorFrame vault_door = (BlockVaultDoorFrame) worldIn.getBlockState(pos).getBlock();
            vault_door.openDoor(worldIn, pos);
        }
    }

}
