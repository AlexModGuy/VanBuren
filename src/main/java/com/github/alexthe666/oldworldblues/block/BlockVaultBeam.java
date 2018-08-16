package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockVaultBeam extends BlockHorizontal {

    public static final PropertyEnum<BlockVaultBeam.Half> HALF = PropertyEnum.<BlockVaultBeam.Half>create("half", BlockVaultBeam.Half.class);
    protected static final AxisAlignedBB AABB_BOTTOM_HALF_NS = new AxisAlignedBB(0.25D, 0.0D, 0.0D, 0.75D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_BOTTOM_HALF_EW = new AxisAlignedBB(0.0D, 0.0D, 0.25D, 1.0D, 0.5D, 0.75D);
    protected static final AxisAlignedBB AABB_TOP_HALF_NS = new AxisAlignedBB(0.25D, 0.5D, 0.0D, 0.75D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_TOP_HALF_EW = new AxisAlignedBB(0.0D, 0.5D, 0.25D, 1.0D, 1.0D, 0.75D);

    public BlockVaultBeam() {
        super(Material.IRON);
        this.setHardness(5F);
        this.setSoundType(SoundType.METAL);
        this.setResistance(Float.MAX_VALUE);
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setUnlocalizedName("oldworldblues.vault_beam");
        this.setRegistryName(OldWorldBlues.MODID, "vault_beam");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HALF, BlockVaultBeam.Half.BOTTOM));
        this.setLightOpacity(0);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        AxisAlignedBB axisalignedbb;
        if (state.getValue(HALF) == Half.TOP) {
            switch ((EnumFacing) state.getValue(FACING)) {
                case NORTH:
                default:
                    axisalignedbb = AABB_TOP_HALF_NS;
                    break;
                case SOUTH:
                    axisalignedbb = AABB_TOP_HALF_NS;
                    break;
                case WEST:
                    axisalignedbb = AABB_TOP_HALF_EW;
                    break;
                case EAST:
                    axisalignedbb = AABB_TOP_HALF_EW;
            }
        } else {
            switch ((EnumFacing) state.getValue(FACING)) {
                case NORTH:
                default:
                    axisalignedbb = AABB_BOTTOM_HALF_NS;
                    break;
                case SOUTH:
                    axisalignedbb = AABB_BOTTOM_HALF_NS;
                    break;
                case WEST:
                    axisalignedbb = AABB_BOTTOM_HALF_EW;
                    break;
                case EAST:
                    axisalignedbb = AABB_BOTTOM_HALF_EW;
            }
        }

        return axisalignedbb;
    }

    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (facing.getAxis().isHorizontal()) {
            return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(HALF, hitY > 0.5F ? Half.TOP : Half.BOTTOM);
        } else {
            return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(HALF, facing == EnumFacing.UP ? Half.BOTTOM : Half.TOP);
        }
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4)).withProperty(HALF, meta > 3 ? Half.TOP : Half.BOTTOM);
    }

    public int getMetaFromState(IBlockState state) {
        int i = ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
        if (state.getValue(HALF) == Half.TOP) {
            i += 4;
        }
        return i;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING, HALF});
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public static enum Half implements IStringSerializable {
        TOP("top"),
        BOTTOM("bottom");

        private final String name;

        private Half(String name) {
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
