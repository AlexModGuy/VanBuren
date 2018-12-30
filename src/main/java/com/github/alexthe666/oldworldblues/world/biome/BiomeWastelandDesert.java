package com.github.alexthe666.oldworldblues.world.biome;

import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import com.github.alexthe666.oldworldblues.world.WorldGenBoulder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BiomeWastelandDesert extends BiomeWasteland {

    public BiomeWastelandDesert(String name, BiomeProperties properties) {
        super(name, properties);
        this.topBlock = OWBBlocks.WASTELAND_SAND.getDefaultState();
        this.fillerBlock = OWBBlocks.WASTELAND_SAND.getDefaultState();
    }

    public void decorate(World worldIn, Random rand, BlockPos pos) {
        {
            int i = 1 + rand.nextInt(3);
            for (int j = 0; j < i; ++j) {
                int k = rand.nextInt(16) + 8;
                int l = rand.nextInt(16) + 8;
                BlockPos blockpos = worldIn.getHeight(pos.add(k, 0, l));
                new WorldGenBoulder(OWBBlocks.DRY_IRRADIATED_MUD.getDefaultState(), 3, false).generate(worldIn, rand, blockpos);
            }
        }
        {
            int i = 1 + rand.nextInt(3);
            for (int j = 0; j < i; ++j) {
                int k = rand.nextInt(16) + 8;
                int l = rand.nextInt(16) + 8;
                BlockPos blockpos = worldIn.getHeight(pos.add(k, 0, l));
                new WorldGenBoulder(OWBBlocks.WASTELAND_SAND.getDefaultState(), 3, false).generate(worldIn, rand, blockpos);
            }
        }
        super.decorate(worldIn, rand, pos);
    }
}
