package com.github.alexthe666.oldworldblues.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum MetalBlocks {
    UNPAINTED,
    BABY_BLUE,
    DARK_BLUE,
    RED,
    BLACK,
    LIGHT_GREEN,
    BEIGE,
    YELLOW,
    ORANGE,
    PINK;

    public String friendlyName;
    public Block metalBlock;
    public BlockGenericSlab metalSlab;
    public BlockGenericSlab metalSlabDouble;
    public Block metalStairs;
    public Block metalSheet;
    protected static final AxisAlignedBB CARPET_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);

    MetalBlocks(){
        friendlyName = this.name().toLowerCase();
    }

    public void initialize() {
        metalBlock = new BlockMetal(this);
        metalSlab = new SlabHalf(friendlyName + "_metal_slab", metalSlab, metalSlabDouble);
        metalSlabDouble = new SlabDouble(friendlyName + "_metal_slab", metalSlab, metalSlabDouble);
        metalStairs = new BlockGenericStairs(metalBlock.getDefaultState(), friendlyName + "_metal_stairs");
        metalSheet = new BlockMetalSheet(this);
    }


    public class BlockMetal extends BlockGeneric {
        public BlockMetal(MetalBlocks enumMetalColors) {
            super(enumMetalColors.friendlyName + "_metal_block", 4.0F, 14F, Material.IRON, SoundType.METAL);
        }
    }

    public static class SlabDouble extends BlockGenericSlab {
        private BlockSlab singleBlock;
        private BlockSlab doubleBlock;
        private String name;
        public SlabDouble(String name, BlockSlab singleBlock, BlockSlab doubleBlock) {
            super(name, 4.0F, 14F, SoundType.METAL, singleBlock);
            this.singleBlock = singleBlock;
            this.doubleBlock = doubleBlock;
            this.name = name;
        }

        @Override
        public ItemBlock getItemBlock() {
            return null;
        }

        @Override
        public boolean isDouble() {
            return true;
        }
    }

    public static class SlabHalf extends BlockGenericSlab {
        private BlockSlab singleBlock;
        private BlockSlab doubleBlock;
        private String name;
        public SlabHalf(String name, BlockSlab singleBlock, BlockSlab doubleBlock) {
            super(name, 4.0F, 14F, SoundType.METAL, singleBlock);
            this.singleBlock = singleBlock;
            this.doubleBlock = doubleBlock;
            this.name = name;
        }

        @Override
        public ItemBlock getItemBlock() {
            return null;
        }

        @Override
        public boolean isDouble() {
            return false;
        }


    }

    private class BlockMetalSheet extends BlockGeneric {

        public BlockMetalSheet(MetalBlocks metalBlocks) {
            super(metalBlocks.friendlyName + "_metal_sheet", 2.0F, 10F, Material.IRON, SoundType.METAL);
        }

        public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
            return CARPET_AABB;
        }

        public boolean isOpaqueCube(IBlockState state) {
            return false;
        }

        public boolean isFullCube(IBlockState state) {
            return false;
        }

        @SideOnly(Side.CLIENT)
        public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
            if (side == EnumFacing.UP) {
                return true;
            } else {
                return blockAccess.getBlockState(pos.offset(side)).getBlock() == this ? true : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
            }
        }

        public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
            return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
        }
    }
}
