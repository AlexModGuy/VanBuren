package com.github.alexthe666.oldworldblues.world;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenBoulder extends WorldGenerator {
    private IBlockState block;
    private final int startRadius;
    private boolean replaceAir;

    public WorldGenBoulder(IBlockState blockIn, int startRadiusIn, boolean replaceAir) {
        super(false);
        this.block = blockIn;
        this.startRadius = startRadiusIn;
        this.replaceAir = replaceAir;
    }

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int i1 = this.startRadius;

        for (int i = 0; i1 >= 0 && i < 3; ++i) {
            int j = i1 + rand.nextInt(2);
            int k = i1 + rand.nextInt(2);
            int l = i1 + rand.nextInt(2);
            float f = (float) (j + k + l) * 0.333F + 0.5F;

            for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l))) {
                if (blockpos.distanceSq(position) <= (double) (f * f) && (replaceAir ||worldIn.getBlockState(blockpos).isOpaqueCube())) {
                    worldIn.setBlockState(blockpos, this.block, 4);
                }
            }
            position = position.add(-(i1 + 1) + rand.nextInt(2 + i1 * 2), 0 - rand.nextInt(2), -(i1 + 1) + rand.nextInt(2 + i1 * 2));
        }

        return true;
    }
}