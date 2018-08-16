package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
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
import java.util.Random;

public class BlockRustStain extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 4);
    protected static final AxisAlignedBB LADDER_EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
    protected static final AxisAlignedBB LADDER_WEST_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB LADDER_SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB LADDER_NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB BOTTOM_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);
    protected static final AxisAlignedBB TOP_AABB = new AxisAlignedBB(0.0D, 0.8125D, 0.0D, 1.0D, 1D, 1.0D);

    public BlockRustStain() {
        super(Material.IRON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(VARIANT, 0));
        this.setTickRandomly(true);
        this.setHardness(0.5F);
        this.setSoundType(SoundType.METAL);
        this.setResistance(Float.MAX_VALUE);
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setUnlocalizedName("oldworldblues.rust_stain");
        this.setRegistryName(OldWorldBlues.MODID, "rust_stain");
    }

    @Deprecated
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {

    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        BlockPos blockpos = pos.up();
        int variant = (pos.getX() + pos.getY() + pos.getZ()) % 4;
        return state.withProperty(FACING, state.getValue(FACING)).withProperty(VARIANT, variant);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch ((EnumFacing) state.getValue(FACING)) {
            case UP:
                return BOTTOM_AABB;
            case DOWN:
                return TOP_AABB;
            case NORTH:
                return LADDER_NORTH_AABB;
            case SOUTH:
                return LADDER_SOUTH_AABB;
            case WEST:
                return LADDER_WEST_AABB;
            case EAST:
            default:
                return LADDER_EAST_AABB;
        }
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    /**
     * Check whether this Block can be placed at pos, while aiming at the specified side of an adjacent block
     */
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        if (this.canAttachTo(worldIn, pos.west(), side)) {
            return true;
        } else if (this.canAttachTo(worldIn, pos.east(), side)) {
            return true;
        } else if (this.canAttachTo(worldIn, pos.north(), side)) {
            return true;
        } else if (this.canAttachTo(worldIn, pos.down(), side)) {
            return true;
        } else if (this.canAttachTo(worldIn, pos.up(), side)) {
            return true;
        } else {
            return this.canAttachTo(worldIn, pos.south(), side);
        }
    }

    private boolean canAttachTo(World p_193392_1_, BlockPos p_193392_2_, EnumFacing p_193392_3_) {
        IBlockState iblockstate = p_193392_1_.getBlockState(p_193392_2_);
        boolean flag = isExceptBlockForAttachWithPiston(iblockstate.getBlock());
        return !flag && iblockstate.getBlockFaceShape(p_193392_1_, p_193392_2_, p_193392_3_) == BlockFaceShape.SOLID && !iblockstate.canProvidePower();
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (this.canAttachTo(worldIn, pos.offset(facing.getOpposite()), facing)) {
            return this.getDefaultState().withProperty(FACING, facing);
        } else {
            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                if (this.canAttachTo(worldIn, pos.offset(enumfacing.getOpposite()), enumfacing)) {
                    return this.getDefaultState().withProperty(FACING, enumfacing);
                }
            }

            return this.getDefaultState();
        }
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
        if (!this.canAttachTo(worldIn, pos.offset(enumfacing.getOpposite()), enumfacing)) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
    }

    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(FACING)).getIndex();
    }

    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING, VARIANT});
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}