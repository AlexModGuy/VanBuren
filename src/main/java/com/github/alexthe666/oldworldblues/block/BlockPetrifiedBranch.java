package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPetrifiedBranch extends BlockGeneric {
    public static final PropertyInteger UP = PropertyInteger.create("up", 0, 5);
    public static final PropertyInteger DOWN = PropertyInteger.create("down", 0, 5);
    public static final PropertyInteger NORTH = PropertyInteger.create("north", 0, 5);
    public static final PropertyInteger EAST = PropertyInteger.create("east", 0, 5);
    public static final PropertyInteger SOUTH = PropertyInteger.create("south", 0, 5);
    public static final PropertyInteger WEST = PropertyInteger.create("west", 0, 5);

    public BlockPetrifiedBranch() {
        super("petrified_tree_branch", 2F, 1, Material.WOOD, SoundType.WOOD);
        this.setDefaultState(this.getBlockState().getBaseState().withProperty(UP, Integer.valueOf(0))
                .withProperty(DOWN, Integer.valueOf(0))
                .withProperty(WEST, Integer.valueOf(0))
                .withProperty(NORTH, Integer.valueOf(0))
                .withProperty(EAST, Integer.valueOf(0))
                .withProperty(SOUTH, Integer.valueOf(0))
        );
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state.withProperty(UP, getBlocksUp(worldIn, pos, EnumFacing.UP))
                .withProperty(DOWN, getBlocksUp(worldIn, pos, EnumFacing.DOWN))
                .withProperty(NORTH, getBlocksUp(worldIn, pos, EnumFacing.NORTH))
                .withProperty(SOUTH, getBlocksUp(worldIn, pos, EnumFacing.SOUTH))
                .withProperty(EAST, getBlocksUp(worldIn, pos, EnumFacing.EAST))
                .withProperty(WEST, getBlocksUp(worldIn, pos, EnumFacing.WEST));
    }

    private int getBlocksUp(IBlockAccess worldIn, BlockPos pos, EnumFacing up) {
        if (worldIn.getBlockState(pos.offset(up)).getBlock() == OWBBlocks.PETRIFIED_TREE) {
            return Math.max(1, getTrunkThickness(worldIn, pos.offset(up)) - 1);
        }
        if (worldIn.getBlockState(pos.offset(up)).getBlock() == OWBBlocks.PETRIFIED_TREE_BRANCH) {
            return Math.max(1, getBranchThickness(worldIn, pos.offset(up)) - 1);
        }
        return 0;
    }

    public static int getBranchThickness(IBlockAccess worldIn, BlockPos pos){
        if(worldIn.getBlockState(pos).getBlock() == OWBBlocks.PETRIFIED_TREE_BRANCH){
            int up = worldIn.getBlockState(pos).getValue(UP);
            int down = worldIn.getBlockState(pos).getValue(DOWN);
            int east = worldIn.getBlockState(pos).getValue(EAST);
            int west = worldIn.getBlockState(pos).getValue(WEST);
            int north = worldIn.getBlockState(pos).getValue(NORTH);
            int south = worldIn.getBlockState(pos).getValue(SOUTH);
            return Math.max(Math.max(Math.max(east, west), Math.max(up, down)), Math.max(north, south));
        }
        return 1;
    }

    private int getTrunkThickness(IBlockAccess worldIn, BlockPos pos){
        if(worldIn.getBlockState(pos).getBlock() == OWBBlocks.PETRIFIED_TREE){
            return worldIn.getBlockState(pos).getValue(BlockPetrifiedStump.UP);
        }
        return 1;
    }

    private int getStumpProximity(IBlockAccess worldIn, BlockPos pos){
        for(BlockPos allPos : BlockPos.getAllInBox(pos.add(-5, -5, -5), pos.add(5, 5, 5)) ){
            if (worldIn.getBlockState(allPos).getBlock() == OWBBlocks.PETRIFIED_TREE && worldIn.getBlockState(allPos).getValue(UP) > 0) {
                return 4 - MathHelper.clamp((int)(pos.distanceSq(allPos) / 4), 0, 4);
            }
        }
        return 0;
    }

    private boolean isStump(IBlockAccess world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == OWBBlocks.PETRIFIED_TREE && world.getBlockState(pos).getValue(UP) > 0;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UP, DOWN, NORTH, SOUTH, EAST, WEST);
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
