package com.github.alexthe666.oldworldblues.world.gen;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.block.MetalBlocks;
import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class WorldGenBillboard extends WorldGenerator {
    public boolean isShort;
    public EnumFacing facing;
    public WorldGenBillboard(EnumFacing facing, boolean isShort) {
        super(false);
        this.isShort = isShort;
        this.facing = facing;

    }

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int boardHeight = 3 + (isShort ? 0 : 1);
        int supportHeight = isShort ? 1 : 3 + rand.nextInt(5);
        int length = 5;
        if(!worldIn.isAirBlock(position.up(supportHeight)) || !worldIn.isAirBlock(position.up(supportHeight).offset(facing, length))){
            return false;
        }
        for(int y = 0; y < supportHeight; y++){
            worldIn.setBlockState(position.up(y), OWBBlocks.RUSTY_POLE.getDefaultState());
            worldIn.setBlockState(position.up(y).offset(facing, length - 1), OWBBlocks.RUSTY_POLE.getDefaultState());
        }
        BlockPos otherPole = position.offset(facing, length - 1);
        while(worldIn.isAirBlock(otherPole.down()) && otherPole.getY() > 1){
            otherPole = otherPole.down();
            worldIn.setBlockState(otherPole, OWBBlocks.RUSTY_POLE.getDefaultState());
        }
        for(int x = 0; x < length; x++){
            for(int y = supportHeight; y < supportHeight + boardHeight; y++){
                worldIn.setBlockState(position.up(y).offset(facing, x), OWBBlocks.RUSTY_LATTICE.getDefaultState());
            }
        }
        BlockPos paintingPos = position.up(supportHeight+ (isShort ? 0 : 1)).offset(facing.rotateY());
        worldIn.setBlockState(paintingPos, Blocks.PUMPKIN.getDefaultState().withProperty(BlockHorizontal.FACING, facing.rotateY()));
         return true;
    }
}