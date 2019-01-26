package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRustyPole extends Block implements IDecorationBlock {

    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.25D, 0D, 0.25D, 0.75D, 1, 0.75D);

    public BlockRustyPole() {
        super(Material.IRON);
        this.setHardness(3.5F);
        this.setSoundType(SoundType.METAL);
        this.setResistance(10);
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setTranslationKey("oldworldblues.rusty_pole");
        this.setRegistryName(OldWorldBlues.MODID, "rusty_pole");
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face != EnumFacing.UP && face != EnumFacing.DOWN ? BlockFaceShape.MIDDLE_POLE : BlockFaceShape.CENTER;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}
