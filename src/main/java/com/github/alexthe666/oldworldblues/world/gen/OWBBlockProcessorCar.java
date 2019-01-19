package com.github.alexthe666.oldworldblues.world.gen;

import com.github.alexthe666.oldworldblues.block.BlockInteriorVaultDoor;
import com.github.alexthe666.oldworldblues.block.BlockWheel;
import com.github.alexthe666.oldworldblues.block.MetalBlocks;
import com.github.alexthe666.oldworldblues.block.entity.TileEntityVaultDoor;
import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import javax.annotation.Nullable;
import java.util.Random;

public class OWBBlockProcessorCar extends OWBBlockProcessor {
    public MetalBlocks color;
    public boolean axels;
    public boolean paint;
    public OWBBlockProcessorCar(BlockPos pos, PlacementSettings settings, Random rand, boolean paint, boolean axels) {
        super(pos, settings);
        this.color = MetalBlocks.values()[rand.nextInt(MetalBlocks.values().length - 1)];
        this.paint = paint;
        this.axels = axels;
    }


    @Nullable
    public Template.BlockInfo processBlock(World worldIn, BlockPos pos, Template.BlockInfo blockInfoIn) {
        if (blockInfoIn.blockState.getBlock() == Blocks.DIAMOND_BLOCK) {
            return new Template.BlockInfo(pos, Blocks.AIR.getDefaultState(), null);
        }
        if(blockInfoIn.blockState.getBlock() == OWBBlocks.WHEEL && this.axels){
            EnumFacing facing = blockInfoIn.blockState.getValue(BlockWheel.FACING);
            BlockWheel.EnumPosition positions = blockInfoIn.blockState.getValue(BlockWheel.E_POS);
            return new Template.BlockInfo(pos, OWBBlocks.AXEL.getDefaultState().withProperty(BlockWheel.FACING, facing).withProperty(BlockWheel.E_POS, positions), null);
        }
        if(paint) {
            if (blockInfoIn.blockState.getBlock() instanceof MetalBlocks.BlockMetal && blockInfoIn.blockState.getBlock() != MetalBlocks.UNPAINTED.metalBlock) {
                return new Template.BlockInfo(pos, color.metalBlock.getDefaultState(), null);
            }
            if (blockInfoIn.blockState.getBlock() instanceof MetalBlocks.BlockMetalSheet && blockInfoIn.blockState.getBlock() != MetalBlocks.UNPAINTED.metalSheet) {
                return new Template.BlockInfo(pos, color.metalSheet.getDefaultState(), null);
            }
            if (blockInfoIn.blockState.getBlock() instanceof MetalBlocks.BlockMetalStairs && blockInfoIn.blockState.getBlock() != MetalBlocks.UNPAINTED.metalStairs) {
                EnumFacing facing = blockInfoIn.blockState.getValue(MetalBlocks.BlockMetalStairs.FACING);
                BlockStairs.EnumHalf half = blockInfoIn.blockState.getValue(MetalBlocks.BlockMetalStairs.HALF);
                BlockStairs.EnumShape shape = blockInfoIn.blockState.getValue(MetalBlocks.BlockMetalStairs.SHAPE);
                return new Template.BlockInfo(pos, color.metalStairs.getDefaultState().withProperty(MetalBlocks.BlockMetalStairs.FACING, facing).withProperty(MetalBlocks.BlockMetalStairs.HALF, half).withProperty(MetalBlocks.BlockMetalStairs.SHAPE, shape), null);
            }
            if (blockInfoIn.blockState.getBlock() instanceof MetalBlocks.SlabHalf && blockInfoIn.blockState.getBlock() != MetalBlocks.UNPAINTED.metalSlab) {
                BlockSlab.EnumBlockHalf half = blockInfoIn.blockState.getValue(MetalBlocks.SlabHalf.HALF);
                return new Template.BlockInfo(pos, color.metalSlab.getDefaultState().withProperty(MetalBlocks.SlabHalf.HALF, half), null);
            }
            if (blockInfoIn.blockState.getBlock() instanceof MetalBlocks.SlabDouble) {
                return new Template.BlockInfo(pos, color.metalBlock.getDefaultState(), null);
            }
        }
        return super.processBlock(worldIn, pos, blockInfoIn);
    }
}
