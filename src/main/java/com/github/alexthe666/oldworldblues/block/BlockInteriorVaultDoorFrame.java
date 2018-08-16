package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.block.entity.TileEntityInteriorVaultDoor;
import com.github.alexthe666.oldworldblues.block.entity.TileEntityVaultDoor;
import net.minecraft.block.Block;
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
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;

import javax.annotation.Nullable;
import java.util.List;

public class BlockInteriorVaultDoorFrame extends Block {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public Item itemBlock;
    protected static final AxisAlignedBB AABB_EMPTY = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    protected static final AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.5, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.0, 1.0D, 1.0D, 0.5D);
    protected static final AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.5D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.0D, 0.0D, 0.0, 0.5D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_TOP = new AxisAlignedBB(0.0D, 0.5D, 0.0, 1.0D, 1.0D, 1.0D);

    public BlockInteriorVaultDoorFrame() {
        super(Material.IRON);
        this.setHardness(5);
        this.setResistance(100000.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setSoundType(SoundType.METAL);
        this.setUnlocalizedName("oldworldblues.interior_vault_door_frame");
        this.setRegistryName(OldWorldBlues.MODID, "interior_vault_door_frame");
        this.setLightOpacity(1);
    }

    @Nullable
    public TileEntityInteriorVaultDoor getDoorTileEntity(World world, BlockPos position, IBlockState state) {
        for (int y = 0; y < 3; y++) {
            BlockPos ySearch = position.down(y);
            for (int xz = 0; xz < 3; xz++) {
                BlockPos xzSearch = ySearch.offset(state.getValue(FACING).rotateYCCW(), -xz);
                if (world.getBlockState(xzSearch).getBlock() instanceof BlockInteriorVaultDoor) {
                    if (world.getTileEntity(xzSearch) != null && world.getTileEntity(xzSearch) instanceof TileEntityInteriorVaultDoor) {
                        return (TileEntityInteriorVaultDoor) world.getTileEntity(xzSearch);
                    }
                }
            }
        }
        return null;
    }

    @Deprecated
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        if (getDoorTileEntity(worldIn, pos, state) != null) {
            TileEntityInteriorVaultDoor door = getDoorTileEntity(worldIn, pos, state);
            BlockPos tileEntityPos = door.getPos();
            int distX = Math.abs(pos.getX() - tileEntityPos.getX());
            int distZ = Math.abs(pos.getZ() - tileEntityPos.getZ());
            int distY = Math.abs(pos.getY() - tileEntityPos.getY());
            if (door.open) {
                return AABB_EMPTY.offset(pos);
            }
        }
        return FULL_BLOCK_AABB.offset(pos);
    }

    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        if (getDoorTileEntity(worldIn, pos, state) == null) {
        } else {
            TileEntityInteriorVaultDoor door = getDoorTileEntity(worldIn, pos, state);
            if (door.open) {
                BlockPos tileEntityPos = door.getPos();
                int distX = Math.abs(pos.getX() - tileEntityPos.getX());
                int distZ = Math.abs(pos.getZ() - tileEntityPos.getZ());
                int distY = Math.abs(pos.getY() - tileEntityPos.getY());
                if(distZ == 0) {
                    if (distX == 0) {
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
                    }
                    if (distX == 2) {
                        switch ((EnumFacing) state.getValue(FACING)) {
                            case NORTH:
                                addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_EAST);
                                break;
                            case SOUTH:
                                addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WEST);
                                break;
                            case WEST:
                                addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_NORTH);
                                break;
                            case EAST:
                                addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_SOUTH);
                                break;
                        }
                    }
                }
                if(distX == 0){
                    if (distZ == 0) {
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
                    }
                    if (distZ == 2) {
                        switch ((EnumFacing) state.getValue(FACING)) {
                            case NORTH:
                                addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_EAST);
                                break;
                            case SOUTH:
                                addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WEST);
                                break;
                            case WEST:
                                addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_NORTH);
                                break;
                            case EAST:
                                addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_SOUTH);
                                break;
                        }
                    }
                }
                if(distY > 1){
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_TOP);
                }
            } else {
                addCollisionBoxToList(pos, entityBox, collidingBoxes, FULL_BLOCK_AABB);
            }
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (getDoorTileEntity(worldIn, pos, state) != null) {
            TileEntityInteriorVaultDoor doorEntity = getDoorTileEntity(worldIn, pos, state);
            boolean preOpen = doorEntity.open;
            doorEntity.open = !preOpen;
        }
        return true;
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


    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        int startY = 0;
        BlockPos startYPos = pos;
        while (worldIn.getBlockState(startYPos.up().offset(state.getValue(FACING).rotateYCCW())).getBlock() instanceof BlockInteriorVaultDoorFrame || worldIn.getBlockState(startYPos.up().offset(state.getValue(FACING).rotateYCCW())).getBlock() instanceof BlockInteriorVaultDoor) {
            startYPos = startYPos.up().offset(state.getValue(FACING).rotateYCCW());
            startY--;
        }
        for (int y = startY; y < startY + 3; y++) {
            BlockPos ySearch = pos.down(y);
            boolean isFrame = worldIn.getBlockState(ySearch.offset(state.getValue(FACING).rotateYCCW(), -1)).getBlock() instanceof BlockInteriorVaultDoorFrame || worldIn.getBlockState(ySearch.offset(state.getValue(FACING).rotateYCCW(), -1)).getBlock() instanceof BlockInteriorVaultDoor;
            BlockPos xzSearch = ySearch.offset(state.getValue(FACING).rotateYCCW(), isFrame ? -1 : 1);
            if (worldIn.getBlockState(xzSearch).getBlock() instanceof BlockInteriorVaultDoorFrame || worldIn.getBlockState(xzSearch).getBlock() instanceof BlockInteriorVaultDoor) {
                worldIn.setBlockToAir(xzSearch);
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING});
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


}
