package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.CommonProxy;
import com.github.alexthe666.oldworldblues.OldWorldBlues;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCardboardBox extends BlockHorizontal {
    public static final PropertyBool OPEN = PropertyBool.create("open");
    protected static final AxisAlignedBB AABB_OPEN_NS = new AxisAlignedBB(0.125, 0.0D, 0.0625D, 0.875D, 0.5D, 0.9375D);
    protected static final AxisAlignedBB AABB_OPEN_EW = new AxisAlignedBB(0.0625D, 0.0D, 0.125D, 0.9375D, 0.5D, 0.875D);

    protected static final AxisAlignedBB AABB_CLOSED_NS = new AxisAlignedBB(0.125, 0.0D, 0.0625D, 0.875D, 0.4375D, 0.9375D);
    protected static final AxisAlignedBB AABB_CLOSED_EW = new AxisAlignedBB(0.0625D, 0.0D, 0.125D, 0.9375D, 0.4375D, 0.875D);

    public BlockCardboardBox() {
        super(Material.IRON);
        this.setHardness(5F);
        this.setSoundType(SoundType.CLOTH);
        this.setResistance(Float.MAX_VALUE);
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setUnlocalizedName("oldworldblues.cardboard_box");
        this.setRegistryName(OldWorldBlues.MODID, "cardboard_box");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, true));
        this.setLightOpacity(0);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        AxisAlignedBB axisalignedbb;
        if (state.getValue(OPEN)) {
            switch ((EnumFacing) state.getValue(FACING)) {
                case NORTH:
                default:
                    axisalignedbb = AABB_OPEN_EW;
                    break;
                case SOUTH:
                    axisalignedbb = AABB_OPEN_EW;
                    break;
                case WEST:
                    axisalignedbb = AABB_OPEN_NS;
                    break;
                case EAST:
                    axisalignedbb = AABB_OPEN_NS;
                    break;
            }
        } else {
            switch ((EnumFacing) state.getValue(FACING)) {
                case NORTH:
                default:
                    axisalignedbb = AABB_CLOSED_EW;
                    break;
                case SOUTH:
                    axisalignedbb = AABB_CLOSED_EW;
                    break;
                case WEST:
                    axisalignedbb = AABB_CLOSED_NS;
                    break;
                case EAST:
                    axisalignedbb = AABB_CLOSED_NS;
                    break;
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
            return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
        } else {
            return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
        }
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4)).withProperty(OPEN, meta > 3);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            return false;
        } else {
            boolean preOpen = state.getBlock() instanceof BlockCardboardBox && state.getValue(OPEN);
            world.setBlockState(pos, state.withProperty(OPEN, !preOpen));
            world.playSound(player, pos, SoundEvents.BLOCK_CLOTH_HIT, SoundCategory.BLOCKS, 1 ,1);
            return true;
        }
    }

    public int getMetaFromState(IBlockState state) {
        int i = ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
        if (state.getValue(OPEN)) {
            i += 4;
        }
        return i;
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING, OPEN});
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

}
