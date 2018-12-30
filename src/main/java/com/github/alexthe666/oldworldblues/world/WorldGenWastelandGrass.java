package com.github.alexthe666.oldworldblues.world;

import com.github.alexthe666.oldworldblues.block.BlockIrradiatedTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenWastelandGrass extends WorldGenerator {
    IBlockState grassState;
    public WorldGenWastelandGrass(IBlockState grassState){
        this.grassState = grassState;
    }
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        for (IBlockState iblockstate = worldIn.getBlockState(position); (iblockstate.getBlock().isAir(iblockstate, worldIn, position) || iblockstate.getBlock().isLeaves(iblockstate, worldIn, position)) && position.getY() > 0; iblockstate = worldIn.getBlockState(position)) {
            position = position.down();
        }

        for (int i = 0; i < 128; ++i) {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (worldIn.isAirBlock(blockpos) && BlockIrradiatedTallGrass.canGrowOn(worldIn.getBlockState(blockpos.down()))) {
                worldIn.setBlockState(blockpos, this.grassState, 2);
            }
        }

        return true;
    }
}
