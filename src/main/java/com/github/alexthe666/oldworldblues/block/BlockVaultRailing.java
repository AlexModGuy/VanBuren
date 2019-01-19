package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
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

import javax.annotation.Nullable;
import java.util.List;

public class BlockVaultRailing extends BlockHorizontal {

    protected static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.0D, 0D, 0.75D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.0D, 0D, 0.0D, 1.0D, 1.0D, 0.25D);
    protected static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.75D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.0D, 0D, 0.0D, 0.25D, 1.0D, 1.0D);

    protected static final AxisAlignedBB AABB_SOUTH_COLLISION = new AxisAlignedBB(0.0D, 0D, 0.75D, 1.0D, 1.5D, 1.0D);
    protected static final AxisAlignedBB AABB_NORTH_COLLISION = new AxisAlignedBB(0.0D, 0D, 0.0D, 1.0D, 1.5D, 0.25D);
    protected static final AxisAlignedBB AABB_EAST_COLLISION = new AxisAlignedBB(0.75D, 0.0D, 0.0D, 1.0D, 1.5D, 1.0D);
    protected static final AxisAlignedBB AABB_WEST_COLLISION = new AxisAlignedBB(0.0D, 0D, 0.0D, 0.25D, 1.5D, 1.0D);


    public BlockVaultRailing() {
        super(Material.ROCK);
        this.setHardness(5F);
        this.setSoundType(SoundType.STONE);
        this.setResistance(Float.MAX_VALUE);
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setTranslationKey("oldworldblues.vault_railing");
        this.setRegistryName(OldWorldBlues.MODID, "vault_railing");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        switch ((EnumFacing) state.getValue(FACING)) {
            case NORTH:
                addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_NORTH_COLLISION);
                break;
            case SOUTH:
                addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_SOUTH_COLLISION);
                break;
            case WEST:
                addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WEST_COLLISION);
                break;
            case EAST:
            default:
                addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_EAST_COLLISION);
                break;
        }
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch ((EnumFacing) state.getValue(FACING)) {
            case NORTH:
                return AABB_NORTH;
            case SOUTH:
                return AABB_SOUTH;
            case WEST:
                return AABB_WEST;
            case EAST:
            default:
                return AABB_EAST;
        }
    }

    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
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

    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(FACING)).getIndex();
    }

    public IBlockState getStateFromMeta(int meta){
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING});
    }

}
