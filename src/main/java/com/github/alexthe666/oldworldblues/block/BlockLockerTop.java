package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.CommonProxy;
import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.Random;

public class BlockLockerTop extends Block {

    public BlockLockerTop() {
        super(Material.IRON);
        this.setHardness(5F);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setUnlocalizedName("oldworldblues.locker_top");
        this.setRegistryName(OldWorldBlues.MODID, "locker_top");
        this.setLightOpacity(1);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if(worldIn.getBlockState(pos.down()).getBlock() == OWBBlocks.LOCKER_BOTTOM){
            worldIn.destroyBlock(pos, true);
        }
        super.breakBlock(worldIn, pos, state);
    }

    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        this.checkAndDropBlock(worldIn, pos, state);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        this.checkAndDropBlock(worldIn, pos, state);
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        return worldIn.getBlockState(pos.down()).getBlock() == OWBBlocks.LOCKER_BOTTOM;
    }

    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(worldIn, pos, state)) {
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }

    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player){
        return new ItemStack(OWBBlocks.LOCKER_BOTTOM);
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            return false;
        } else {
            if(world.getBlockState(pos.down()).getBlock() == OWBBlocks.LOCKER_BOTTOM) {
                player.openGui(OldWorldBlues.INSTANCE, CommonProxy.GUI_OWB_STORAGE, world, pos.getX(), pos.getY() - 1, pos.getZ());
            }
            return true;
        }
    }
}
