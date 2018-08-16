package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockGratingSlab extends BlockGenericSlab implements IGrating {
    public BlockGratingSlab(String name, float hardness, float resistance, SoundType soundType) {
        super(name, hardness, resistance, soundType, OWBBlocks.GRATING_SINGLESLAB);
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockGenericSlab(this, OWBBlocks.GRATING_SINGLESLAB, OWBBlocks.GRATING_DOUBLESLAB);
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public static class Double extends BlockGenericSlab implements IGrating {
        public Double(String name, float hardness, float resistance, SoundType soundType) {
            super(name, hardness, resistance, soundType, OWBBlocks.GRATING_SINGLESLAB);
        }

        @Override
        public ItemBlock getItemBlock() {
            return new ItemBlockGenericSlab(this, OWBBlocks.GRATING_SINGLESLAB, OWBBlocks.GRATING_DOUBLESLAB);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public BlockRenderLayer getBlockLayer() {
            return BlockRenderLayer.CUTOUT;
        }

        public boolean isOpaqueCube(IBlockState state) {
            return false;
        }

        @Override
        public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
            return state.isOpaqueCube() && !(state.getBlock() instanceof IGrating);
        }

        @SideOnly(Side.CLIENT)
        public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
            IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
            Block block = iblockstate.getBlock();
            if(block instanceof IGrating && iblockstate.isFullCube()){
                return false;
            }
            return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }

        @Override
        public boolean isDouble() {
            return true;
        }
    }

    public static class Half extends BlockGenericSlab implements IGrating {
        public Half(String name, float hardness, float resistance, SoundType soundType) {
            super(name, hardness, resistance, soundType, OWBBlocks.GRATING_SINGLESLAB);
        }

        @Override
        public ItemBlock getItemBlock() {
            return new ItemBlockGenericSlab(this, OWBBlocks.GRATING_SINGLESLAB, OWBBlocks.GRATING_DOUBLESLAB);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public BlockRenderLayer getBlockLayer() {
            return BlockRenderLayer.CUTOUT;
        }

        public boolean isOpaqueCube(IBlockState state) {
            return false;
        }

        @SideOnly(Side.CLIENT)
        public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
            IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
            Block block = iblockstate.getBlock();

            if(block instanceof IGrating){
                return false;
            }
            return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }

        @Override
        public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
            return state.isOpaqueCube();
        }

        @Override
        public boolean isDouble() {
            return false;
        }
    }
}
