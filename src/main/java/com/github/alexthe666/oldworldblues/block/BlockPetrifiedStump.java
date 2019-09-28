package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPetrifiedStump extends BlockGeneric {
    public static final PropertyInteger UP = PropertyInteger.create("up", 0, 5);
    public static final PropertyInteger NORTH = PropertyInteger.create("north", 0, 5);
    public static final PropertyInteger EAST = PropertyInteger.create("east", 0, 5);
    public static final PropertyInteger SOUTH = PropertyInteger.create("south", 0, 5);
    public static final PropertyInteger WEST = PropertyInteger.create("west", 0, 5);

    public BlockPetrifiedStump() {
        super("petrified_tree", 2F, 1, Material.WOOD, SoundType.WOOD);
        this.setDefaultState(this.getBlockState().getBaseState().withProperty(UP, Integer.valueOf(0))
                .withProperty(WEST, Integer.valueOf(0))
                .withProperty(NORTH, Integer.valueOf(0))
                .withProperty(EAST, Integer.valueOf(0))
                .withProperty(SOUTH, Integer.valueOf(0))
        );
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        int upperLimit = getBlocksUp(worldIn, pos, EnumFacing.UP);
        return state.withProperty(UP, upperLimit)
                .withProperty(NORTH, getBlocksSide(worldIn, pos, EnumFacing.NORTH, upperLimit))
                .withProperty(SOUTH, getBlocksSide(worldIn, pos, EnumFacing.SOUTH, upperLimit))
                .withProperty(EAST, getBlocksSide(worldIn, pos, EnumFacing.EAST, upperLimit))
                .withProperty(WEST, getBlocksSide(worldIn, pos, EnumFacing.WEST, upperLimit));
    }

    private int getBlocksUp(IBlockAccess worldIn, BlockPos pos, EnumFacing up) {
        int count = 0;
        if (worldIn.isAirBlock(pos.offset(up, 1))) {
            return 0;
        }
        for (int i = 0; i < 5; i++) {
            if (worldIn.getBlockState(pos.offset(up, i)).getBlock() == OWBBlocks.PETRIFIED_TREE && count < 5) {
                count++;
            }

        }
        return count;
    }

    private int getStumpProximity(IBlockAccess worldIn, BlockPos pos){
        for(BlockPos allPos : BlockPos.getAllInBox(pos.add(-5, -5, -5), pos.add(5, 5, 5)) ){
            if (worldIn.getBlockState(allPos).getBlock() == OWBBlocks.PETRIFIED_TREE && worldIn.getBlockState(allPos).getValue(UP) > 0) {
                return 4 - MathHelper.clamp((int)(pos.distanceSq(allPos) / 4), 0, 4);
            }
        }
        return 0;
    }

    private int getBlocksSide(IBlockAccess worldIn, BlockPos pos, EnumFacing up, int upperLimit) {
        int count = 0;

        for (int i = 0; i < 5; i++) {
            if (worldIn.getBlockState(pos.offset(up, i)).getBlock() == OWBBlocks.PETRIFIED_TREE_BRANCH && count < 5) {
                count++;
            }
            if (worldIn.isAirBlock(pos.offset(up, i))) {
                return count - 1;
            }
        }
        return count - 1;
    }



    private boolean isStump(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == OWBBlocks.PETRIFIED_TREE && world.getBlockState(pos).getValue(UP) > 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UP, NORTH, SOUTH, EAST, WEST);
    }

    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        AxisAlignedBB aabb = FULL_BLOCK_AABB;
        return aabb.offset(this.getOffset(state, source, pos));
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }


    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.MIDDLE_POLE;
    }

    private AxisAlignedBB rotateAABB(AxisAlignedBB aabb) {
        return new AxisAlignedBB(aabb.minZ, aabb.minY, aabb.minX, aabb.maxZ, aabb.maxY, aabb.maxX);
    }
}
