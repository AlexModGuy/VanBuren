package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.block.entity.TileEntityInteriorVaultDoor;
import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockInteriorVaultDoor extends BlockContainer {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public Item itemBlock;
    protected static final AxisAlignedBB AABB_EMPTY = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    protected static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.5, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.0, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.0D, 0.0D, 0.0, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_TOP = new AxisAlignedBB(0.0D, 0.75D, 0.0, 1.0D, 1.0D, 1.0D);

    public BlockInteriorVaultDoor() {
        super(Material.IRON);
        this.setHardness(5);
        this.setResistance(100000.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setUnlocalizedName("oldworldblues.interior_vault_door");
        this.setRegistryName(OldWorldBlues.MODID, "interior_vault_door");
        GameRegistry.registerTileEntity(TileEntityInteriorVaultDoor.class, "interior_vault_door");
        this.setLightOpacity(1);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityInteriorVaultDoor();
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityInteriorVaultDoor) {
            TileEntityInteriorVaultDoor doorEntity = (TileEntityInteriorVaultDoor) worldIn.getTileEntity(pos);
            boolean preOpen = doorEntity.open;
            doorEntity.open = !preOpen;
        }
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        for (int y = 0; y < 3; y++) {
            BlockPos ySearch = pos.up(y);
            for (int xz = 0; xz < 3; xz++) {
                BlockPos xzSearch = ySearch.offset(state.getValue(FACING).rotateYCCW(), xz);
                if (worldIn.getBlockState(xzSearch).getBlock().isReplaceable(worldIn, pos)) {
                    worldIn.setBlockState(xzSearch, OWBBlocks.INTERIOR_VAULT_DOOR_FRAME.getDefaultState().withProperty(BlockInteriorVaultDoorFrame.FACING, state.getValue(FACING)));
                }
            }
        }
    }

    public static void onStructureGen(World worldIn, BlockPos pos, IBlockState state) {
        for (int y = 0; y < 3; y++) {
            BlockPos ySearch = pos.up(y);
            for (int xz = 0; xz < 3; xz++) {
                BlockPos xzSearch = ySearch.offset(state.getValue(FACING).rotateYCCW(), xz);
                worldIn.setBlockState(xzSearch, OWBBlocks.INTERIOR_VAULT_DOOR_FRAME.getDefaultState().withProperty(BlockInteriorVaultDoorFrame.FACING, state.getValue(FACING)));
            }
        }
        worldIn.setBlockState(pos, OWBBlocks.INTERIOR_VAULT_DOOR.getDefaultState().withProperty(BlockInteriorVaultDoorFrame.FACING, state.getValue(FACING)));

    }

    @Deprecated
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityInteriorVaultDoor) {
            TileEntityInteriorVaultDoor door = (TileEntityInteriorVaultDoor) worldIn.getTileEntity(pos);
            if (door.open) {
                switch (state.getValue(FACING).rotateYCCW()) {
                    case NORTH:
                        return AABB_NORTH;
                    case SOUTH:
                        return AABB_SOUTH;
                    case WEST:
                        return AABB_WEST;
                    case EAST:
                        return AABB_EAST;
                }
            }
        }
        return FULL_BLOCK_AABB.offset(pos);
    }

    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        if (worldIn.getTileEntity(pos) == null || !(worldIn.getTileEntity(pos) instanceof TileEntityInteriorVaultDoor)) {
        } else {
            TileEntityInteriorVaultDoor door = (TileEntityInteriorVaultDoor) worldIn.getTileEntity(pos);
            if (door.open) {
                switch ((EnumFacing) state.getValue(FACING)) {
                    case NORTH:
                        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WEST);
                        break;
                    case SOUTH:
                        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_EAST);
                        break;
                    case WEST:
                        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_SOUTH);
                        break;
                    case EAST:
                        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_NORTH);
                        break;
                }
            } else {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, FULL_BLOCK_AABB);
            }
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        for (int y = 0; y < 3; y++) {
            BlockPos ySearch = pos.up(y);
            for (int xz = 0; xz < 3; xz++) {
                BlockPos xzSearch = ySearch.offset(state.getValue(FACING).rotateYCCW(), xz);
                if (worldIn.getBlockState(xzSearch).getBlock() instanceof BlockInteriorVaultDoorFrame || worldIn.getBlockState(xzSearch).getBlock() instanceof BlockInteriorVaultDoor) {
                    worldIn.setBlockToAir(xzSearch);
                }
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (canPlaceHere(placer, world, pos)) {
            return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
        } else {
            return Blocks.AIR.getDefaultState();
        }
    }

    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
    }


    public boolean canPlaceHere(EntityLivingBase placer, World world, BlockPos pos) {
        for (int y = 0; y < 3; y++) {
            BlockPos ySearch = pos.up(y);
            for (int xz = 0; xz < 3; xz++) {
                BlockPos xzSearch = ySearch.offset(placer.getHorizontalFacing().rotateYCCW(), -xz);
                if (world.getBlockState(xzSearch).getBlock() != Blocks.AIR) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState blockstate) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState blockstate) {
        return false;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING});
    }
}
