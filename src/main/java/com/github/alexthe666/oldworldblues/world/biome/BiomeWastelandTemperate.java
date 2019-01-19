package com.github.alexthe666.oldworldblues.world.biome;

import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import com.github.alexthe666.oldworldblues.world.gen.WorldGenBoulder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BiomeWastelandTemperate extends BiomeWasteland {

    public BiomeWastelandTemperate(String name, BiomeProperties properties) {
        super(name, properties);
        this.topBlock = OWBBlocks.IRRADIATED_GRASS.getDefaultState();
        this.fillerBlock = OWBBlocks.IRRADIATED_SOIL.getDefaultState();
    }

    public void decorate(World worldIn, Random rand, BlockPos pos) {
        {
            int i = 1 + rand.nextInt(3);
            for (int j = 0; j < i; ++j) {
                int k = rand.nextInt(16) + 8;
                int l = rand.nextInt(16) + 8;
                BlockPos blockpos = worldIn.getHeight(pos.add(k, 0, l));
                new WorldGenBoulder(OWBBlocks.ROCKY_IRRADIATED_SOIL.getDefaultState(), 3, false).generate(worldIn, rand, blockpos);
            }
        }
        {
            int i = 1 + rand.nextInt(3);
            for (int j = 0; j < i; ++j) {
                int k = rand.nextInt(16) + 8;
                int l = rand.nextInt(16) + 8;
                BlockPos blockpos = worldIn.getHeight(pos.add(k, 0, l));
                new WorldGenBoulder(OWBBlocks.IRRADIATED_MUD.getDefaultState(), 3, false).generate(worldIn, rand, blockpos);
            }
        }
        super.decorate(worldIn, rand, pos);
        int grass = 5;
        for (int i3 = 0; i3 < grass; ++i3)
        {
            int j7 = rand.nextInt(16) + 8;
            int i11 = rand.nextInt(16) + 8;
            int k14 = worldIn.getHeight(pos.add(j7, 0, i11)).getY() * 2;
            if (k14 > 0){
                int l17 = rand.nextInt(k14);
                this.getRandomWorldGenForGrass(rand).generate(worldIn, rand, pos.add(j7, l17, i11));
            }
        }
    }
}
