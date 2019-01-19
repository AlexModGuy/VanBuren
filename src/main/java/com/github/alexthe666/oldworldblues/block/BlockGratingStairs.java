package com.github.alexthe666.oldworldblues.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGratingStairs extends BlockGenericStairs implements IGrating {

    public BlockGratingStairs(IBlockState modelState, String name) {
        super(modelState, name);
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();
        if(block instanceof BlockGratingStairs){
            BlockStairs.EnumHalf stairShape = iblockstate.getValue(BlockStairs.HALF);
            return stairShape != blockState.getValue(BlockStairs.HALF);
        }
        if (block instanceof IGrating && iblockstate.getBlockFaceShape(blockAccess, pos.offset(side), side.getOpposite()) == BlockFaceShape.SOLID) {
            return false;
        }
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return state.isOpaqueCube();
    }

}
