package com.github.alexthe666.oldworldblues.structure;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class OWBWorldGenerator implements IWorldGenerator{

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        int x = chunkX * 16;
        int z = chunkZ * 16;
        if(chunkX % 8 == 0 && chunkZ % 8 == 0){
            int x2 = x + random.nextInt(15);
            int z2 = z + random.nextInt(15);
            BlockPos centerPos = world.getHeight(new BlockPos(x2, 0, z2));
            int y2 = Math.max(centerPos.getY() - (40 + random.nextInt(20)), 10);
            new WorldGenVault().generate(world, random, new BlockPos(x2, y2, z2));
        }
    }
}
