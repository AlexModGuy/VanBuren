package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockGrating extends Block implements IGrating{

    public BlockGrating() {
        super(Material.IRON);
        this.setHardness(4F);
        this.setResistance(15F);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setTranslationKey("oldworldblues.grating");
        this.setRegistryName(OldWorldBlues.MODID, "grating");
        this.setLightOpacity(1);
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }


    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();
        if (block instanceof IGrating && iblockstate.getBlockFaceShape(blockAccess, pos.offset(side), side.getOpposite()) == BlockFaceShape.SOLID) {
            return false;
        }
        if (blockState != iblockstate) {
            return true;
        }
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
}
