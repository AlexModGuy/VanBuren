package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.block.entity.TileEntityVaultDoor;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BlockVaultDoorFrame extends Block {

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public Item itemBlock;
    protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB AABB_TOP_HALF = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1D, 1.0D);
    protected static final AxisAlignedBB AABB_EMPTY = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);

    public BlockVaultDoorFrame() {
        super(Material.IRON);
        this.setHardness(20.0F);
        this.setResistance(100000.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setSoundType(SoundType.METAL);
        this.setTranslationKey("oldworldblues.vault_door_frame");
        this.setRegistryName(OldWorldBlues.MODID, "vault_door_frame");
        this.setLightOpacity(1);
    }

    @Nullable
    public TileEntityVaultDoor getDoorTileEntity(World world, BlockPos position, IBlockState state){
        for(int y = 0; y < 6; y++){
            BlockPos ySearch = position.down(y);
            for(int xz = 0; xz < 6; xz++) {
                BlockPos xzSearch = ySearch.offset(state.getValue(FACING).rotateYCCW(), -xz);
                if(world.getBlockState(xzSearch).getBlock() instanceof BlockVaultDoor){
                    if(world.getTileEntity(xzSearch) != null && world.getTileEntity(xzSearch) instanceof TileEntityVaultDoor){
                        return (TileEntityVaultDoor)world.getTileEntity(xzSearch);
                    }
                }
            }
        }
        return null;
    }

    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
    }

    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
    }

    @Deprecated
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        if(getDoorTileEntity(worldIn, pos, state) != null){
            TileEntityVaultDoor door = getDoorTileEntity(worldIn, pos, state);
            BlockPos tileEntityPos = door.getPos();
            int distX = Math.abs(pos.getX() - tileEntityPos.getX());
            int distZ = Math.abs(pos.getZ() - tileEntityPos.getZ());
            if(door.open) {
                if (pos.getY() == tileEntityPos.getY() || pos.getY() == tileEntityPos.getY() + 5) {
                    return FULL_BLOCK_AABB.offset(pos);
                } else if (distX % 5 == 0 && distZ % 5 == 0 || distX % 5 == 0 && distZ % 5 == 0) {
                    return FULL_BLOCK_AABB.offset(pos);
                } else if (pos.getY() == tileEntityPos.getY() + 1 && ((distX == 1 || distX == 4) || (distZ == 1 || distZ == 4))) {
                    return FULL_BLOCK_AABB.offset(pos);
                } else if (pos.getY() == tileEntityPos.getY() + 4 && ((distX == 1 || distX == 4) || (distZ == 1 || distZ == 4))) {
                    return FULL_BLOCK_AABB.offset(pos);
                } else {
                    return AABB_EMPTY.offset(pos);
                }
            }
        }
        return FULL_BLOCK_AABB.offset(pos);
    }

    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState){
        if(getDoorTileEntity(worldIn, pos, state) == null){
        }else {
            TileEntityVaultDoor door = getDoorTileEntity(worldIn, pos, state);
            BlockPos tileEntityPos = door.getPos();
            int distX = Math.abs(pos.getX() - tileEntityPos.getX());
            int distZ = Math.abs(pos.getZ() - tileEntityPos.getZ());
            if(door.open) {
                if (pos.getY() == tileEntityPos.getY() || pos.getY() == tileEntityPos.getY() + 5) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, FULL_BLOCK_AABB);
                } else if (distX % 5 == 0 && distZ % 5 == 0 || distX % 5 == 0 && distZ % 5 == 0) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, FULL_BLOCK_AABB);
                } else if (pos.getY() == tileEntityPos.getY() + 1 && ((distX == 1 || distX == 4) || (distZ == 1 || distZ == 4))) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BOTTOM_HALF);
                } else if (pos.getY() == tileEntityPos.getY() + 4 && ((distX == 1 || distX == 4) || (distZ == 1 || distZ == 4))) {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_TOP_HALF);
                } else {
                    addCollisionBoxToList(pos, entityBox, collidingBoxes, null);
                }
            }else{
                addCollisionBoxToList(pos, entityBox, collidingBoxes, FULL_BLOCK_AABB);
            }
        }
    }

    public boolean openDoor(World worldIn, BlockPos pos){
        if(getDoorTileEntity(worldIn, pos, worldIn.getBlockState(pos)) != null){
            TileEntityVaultDoor doorEntity = getDoorTileEntity(worldIn, pos, worldIn.getBlockState(pos));
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
        while(worldIn.getBlockState(startYPos.up().offset(state.getValue(FACING).rotateYCCW())).getBlock() instanceof BlockVaultDoorFrame || worldIn.getBlockState(startYPos.up().offset(state.getValue(FACING).rotateYCCW())).getBlock() instanceof BlockVaultDoor){
            startYPos = startYPos.up().offset(state.getValue(FACING).rotateYCCW());
            startY--;
        }
        for(int y = startY; y < startY + 6; y++){
            BlockPos ySearch = pos.down(y);
            boolean isFrame = worldIn.getBlockState(ySearch.offset(state.getValue(FACING).rotateYCCW(), -1)).getBlock() instanceof BlockVaultDoorFrame || worldIn.getBlockState(ySearch.offset(state.getValue(FACING).rotateYCCW(), -1)).getBlock() instanceof BlockVaultDoor;
            BlockPos xzSearch = ySearch.offset(state.getValue(FACING).rotateYCCW(), isFrame ? -1 : 1);
            if(worldIn.getBlockState(xzSearch).getBlock() instanceof BlockVaultDoorFrame || worldIn.getBlockState(xzSearch).getBlock() instanceof BlockVaultDoor){
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
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
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
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }


}
