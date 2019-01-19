package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGenericDecoration extends BlockHorizontal implements IDecorationBlock {
    protected AxisAlignedBB AABB_NORTH;
    protected AxisAlignedBB AABB_SOUTH;
    protected AxisAlignedBB AABB_EAST;
    protected AxisAlignedBB AABB_WEST;
    private BlockRenderLayer renderLayer = BlockRenderLayer.CUTOUT;
    private boolean offset;

    public BlockGenericDecoration(String name, Material material, SoundType sound, boolean offset) {
        super(material);
        this.setSoundType(sound);
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setTranslationKey("oldworldblues." + name);
        this.setRegistryName(OldWorldBlues.MODID, name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.offset = offset;
        AABB_NORTH = FULL_BLOCK_AABB;
        AABB_SOUTH = FULL_BLOCK_AABB;
        AABB_EAST = FULL_BLOCK_AABB;
        AABB_WEST = FULL_BLOCK_AABB;
    }

    public BlockGenericDecoration(String name, Material material, SoundType sound, boolean offset, AxisAlignedBB aabb) {
        this(name, material, sound, offset);
        AABB_NORTH = aabb;
        AABB_SOUTH = aabb;
        AABB_EAST = rotateAABB(aabb);
        AABB_WEST = rotateAABB(aabb);
    }

    public BlockGenericDecoration(String name, Material material, SoundType sound, boolean offset, AxisAlignedBB... aabbs) {
        this(name, material, sound, offset);
        if (aabbs.length > 3) {
            AABB_NORTH = aabbs[0];
            AABB_SOUTH = aabbs[1];
            AABB_EAST = aabbs[2];
            AABB_WEST = aabbs[3];
        }
    }

    public BlockGenericDecoration setRenderLayer(BlockRenderLayer layer){
        this.renderLayer = layer;
        return this;
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        AxisAlignedBB aabb;
        switch ((EnumFacing) state.getValue(FACING)) {
            case NORTH:
                aabb = AABB_NORTH;
                break;
            case SOUTH:
                aabb = AABB_SOUTH;
                break;
            case WEST:
                aabb = AABB_WEST;
                break;
            case EAST:
            default:
                aabb = AABB_EAST;
                break;
        }
        return aabb.offset(this.getOffset(state, source, pos));
    }

    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return renderLayer;
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(FACING)).getIndex();
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    public Block.EnumOffsetType getOffsetType() {
        return offset ? Block.EnumOffsetType.XZ : super.getOffsetType();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    private AxisAlignedBB rotateAABB(AxisAlignedBB aabb){
        return new AxisAlignedBB(aabb.minZ, aabb.minY, aabb.minX, aabb.maxZ, aabb.maxY, aabb.maxX);
    }
}
