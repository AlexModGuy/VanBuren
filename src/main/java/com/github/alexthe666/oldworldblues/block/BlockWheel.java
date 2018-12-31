package com.github.alexthe666.oldworldblues.block;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockWheel extends BlockGeneric implements IRoad {
    public static final PropertyEnum<BlockWheel.EnumPosition> E_POS = PropertyEnum.<BlockWheel.EnumPosition>create("position", BlockWheel.EnumPosition.class);
    public static final PropertyDirection FACING = BlockDirectional.FACING;
    private AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.0D, 0D, 0.6875D, 1.0D, 1.0D, 1.0D);
    private AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.0D, 0D, 0.0D, 1.0D, 1.0D, 0.325D);
    private AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.6875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    private AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.0D, 0D, 0.0D, 0.6875D, 1.0D, 1.0D);
    private AxisAlignedBB AABB_TOP = new AxisAlignedBB(0.0D, 0D, 0D, 1.0D, 0.325D, 1.0D);
    private AxisAlignedBB AABB_BOTTOM = new AxisAlignedBB(0.0D, 0.6875D, 0D, 1.0D, 1.0D, 1.0D);
    private AxisAlignedBB AABB_MIDDLE_NS = new AxisAlignedBB(0.25D, 0D, 0D, 0.75D, 1.0D, 1.0D);
    private AxisAlignedBB AABB_MIDDLE_EW = new AxisAlignedBB(0D, 0D, 0.25D, 1.0D, 1.0D, 0.75D);
    private AxisAlignedBB AABB_MIDDLE_TB = new AxisAlignedBB(0D, 0.25D, 0D, 1.0D, 0.75D, 1.0D);

    public BlockWheel(String name, float hardness, float resistance, Material material, SoundType sound, float wheelThick) {
        super(name, hardness, resistance, material, sound);
        AABB_SOUTH = new AxisAlignedBB(1 - wheelThick, 1 - wheelThick, 0.6875D, wheelThick, wheelThick, 1.0D);
        AABB_NORTH = new AxisAlignedBB(1 - wheelThick, 1 - wheelThick, 0.0D, wheelThick, wheelThick, 0.325D);
        AABB_EAST = new AxisAlignedBB(0.6875D, 1 - wheelThick, 1 - wheelThick, 1.0D, wheelThick, wheelThick);
        AABB_WEST = new AxisAlignedBB(0.0D, 1 - wheelThick, 1 - wheelThick, 0.325D, 1 - wheelThick, 1 - wheelThick);
        AABB_TOP = new AxisAlignedBB(1 - wheelThick, 0D, 1 - wheelThick, wheelThick, 0.325D, wheelThick);
        AABB_BOTTOM = new AxisAlignedBB(1 - wheelThick, 0.6875D, 1 - wheelThick, wheelThick, 1.0D, wheelThick);
        AABB_MIDDLE_NS = new AxisAlignedBB(0.25D, 1 - wheelThick, 1 - wheelThick, 0.75D, wheelThick, wheelThick);
        AABB_MIDDLE_EW = new AxisAlignedBB(1 - wheelThick, 1 - wheelThick, 0.25D, wheelThick, wheelThick, 0.75D);
        AABB_MIDDLE_TB = new AxisAlignedBB(1 - wheelThick, 0.25D, 1 - wheelThick, wheelThick, 0.75D, wheelThick);
        this.setDefaultState(this.blockState.getBaseState().withProperty(E_POS, EnumPosition.MIDDLE).withProperty(FACING, EnumFacing.NORTH));
    }

    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        AxisAlignedBB aabb;
        if (state.getValue(E_POS) == EnumPosition.SIDE) {
            switch ((EnumFacing) state.getValue(FACING)) {
                case UP:
                    aabb = AABB_TOP;
                    break;
                case DOWN:
                    aabb = AABB_BOTTOM;
                    break;
                case NORTH:
                    aabb = AABB_SOUTH;
                    break;
                case SOUTH:
                    aabb = AABB_NORTH;
                    break;
                case WEST:
                    aabb = AABB_EAST;
                    break;
                case EAST:
                default:
                    aabb = AABB_WEST;
                    break;
            }
            return aabb;
        } else {
            switch (state.getValue(FACING).getAxis()) {
                case X:
                    aabb = AABB_MIDDLE_EW;
                    break;
                case Y:
                    aabb = AABB_MIDDLE_TB;
                    break;
                case Z:
                    aabb = AABB_MIDDLE_NS;
                    break;
                default:
                    aabb = AABB_MIDDLE_NS;
                    break;
            }
            return aabb;
        }
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        BlockWheel.EnumPosition position = EnumPosition.SIDE;
        if ((hitX >= 0.4 && hitX <= 0.6 || hitZ >= 0.4 && hitZ <= 0.6) && facing == EnumFacing.UP) {
            position = EnumPosition.MIDDLE;
            facing = placer.getHorizontalFacing();
        }
        return this.getDefaultState().withProperty(FACING, facing).withProperty(E_POS, position);
    }

    public IBlockState getStateFromMeta(int meta) {
        EnumPosition position = EnumPosition.SIDE;
        if (meta >= 6) {
            position = EnumPosition.MIDDLE;
        }

        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta % 6)).withProperty(E_POS, position);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public int getMetaFromState(IBlockState state) {
        int i = ((EnumFacing) state.getValue(FACING)).getIndex();
        if (state.getValue(E_POS) == EnumPosition.MIDDLE) {
            i += 6;
        }
        return i;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING, E_POS});
    }


    public static enum EnumPosition implements IStringSerializable {
        SIDE("side"),
        MIDDLE("middle");

        private final String name;

        private EnumPosition(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }
    }
}
