package com.github.alexthe666.oldworldblues.structure;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class OWBWorldGenerator implements IWorldGenerator{

    private static final WorldGenVault VAULT_GEN = new WorldGenVault();

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int x = chunkX * 16;
        int z = chunkZ * 16;
        if(chunkX % 8 == 0 && chunkZ % 8 == 0 && random.nextInt(3) == 0){
            VAULT_GEN.generate(world, random, new BlockPos(x, 10 + random.nextInt(70), z));
        }
    }
}
