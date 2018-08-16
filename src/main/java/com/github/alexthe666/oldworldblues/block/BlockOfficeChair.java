package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.entity.EntitySeat;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockOfficeChair extends BlockHorizontal implements ISittable{

    protected static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.0D, 0D, 0.125D, 1D, 1.25D, 0.875D);
    protected static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.0D, 0D, 0.125D, 1D, 1.25D, 0.875D);
    protected static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.125D, 0.0D, 0.0D, 0.875D, 1.25D, 1.0D);
    protected static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.125D, 0D, 0.0D, 0.875D, 1.25D, 1.0D);

    public BlockOfficeChair() {
        super(Material.IRON);
        this.setHardness(5F);
        this.setSoundType(SoundType.METAL);
        this.setResistance(Float.MAX_VALUE);
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setUnlocalizedName("oldworldblues.office_chair");
        this.setRegistryName(OldWorldBlues.MODID, "office_chair");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setLightOpacity(0);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch ((EnumFacing) state.getValue(FACING)) {
            case NORTH:
                return AABB_SOUTH;
            case SOUTH:
                return AABB_NORTH;
            case WEST:
                return AABB_EAST;
            case EAST:
            default:
                return AABB_WEST;
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

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING});
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

    @Override
    public void startRiding(EntityLivingBase rider, IBlockState blockState, BlockPos pos) {
        EntitySeat seat = new EntitySeat(rider.world);
        seat.setPosition(pos.getX() + 0.5D, pos.getY() - 0.25D, pos.getZ() + 0.5D);
        rider.world.spawnEntity(seat);
        rider.startRiding(seat);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            return false;
        } else {
            startRiding(player, state, pos);
            return true;
        }
    }
}
