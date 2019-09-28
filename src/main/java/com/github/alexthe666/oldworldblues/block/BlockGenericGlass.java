package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class BlockGenericGlass extends BlockGeneric {
    public BlockGenericGlass(String name, float hardness, float resistance, Material material, SoundType soundType) {
        super(name, hardness, resistance, material, soundType);
        this.setLightOpacity(0);
    }

    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return this == OWBBlocks.BROKEN_GLASS ? BlockRenderLayer.TRANSLUCENT : BlockRenderLayer.CUTOUT;
    }

    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
        Block block = iblockstate.getBlock();
        if(this == OWBBlocks.RUSTY_LATTICE){
            return true;
        }else{
            if (blockState != iblockstate) {
                return true;
            }
            if (block == this) {
                return false;
            }
            return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }

    }
}
