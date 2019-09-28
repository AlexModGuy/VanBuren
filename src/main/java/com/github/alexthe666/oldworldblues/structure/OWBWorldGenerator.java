package com.github.alexthe666.oldworldblues.structure;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.world.gen.WorldGenBillboard;
import com.github.alexthe666.oldworldblues.world.gen.WorldGenVault;
import com.github.alexthe666.oldworldblues.world.gen.WorldGenVehicle;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
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
        if(world.provider.getDimension() == 0 && chunkX % 8 == 0 && chunkZ % 8 == 0 && random.nextInt(15) == 0){
            int x2 = x + random.nextInt(15);
            int z2 = z + random.nextInt(15);
            BlockPos centerPos = world.getHeight(new BlockPos(x2, 0, z2));
            int y2 = Math.max(centerPos.getY() - (40 + random.nextInt(20)), 10);
            new WorldGenVault().generate(world, random, new BlockPos(x2, y2, z2));
        }
        if(world.provider.getDimension() == OldWorldBlues.CONFIG.wastelandDimensionID){
            if(chunkX % 16 == 0){
                if(random.nextInt(2) == 0){
                    int x2 = x + random.nextInt(15);
                    int z2 = z + random.nextInt(15);
                    BlockPos centerPos = world.getHeight(new BlockPos(x2, 0, z2));
                    new WorldGenVehicle(random.nextBoolean() ? EnumFacing.NORTH : EnumFacing.SOUTH).generate(world, random, centerPos);
                }
                if(random.nextInt(5) == 0 && chunkX % 16 != 0){
                    int x2 = random.nextBoolean() ?  (x + 7 + random.nextInt(7)) : x - random.nextInt(7);
                    int z2 = z + random.nextInt(15);
                    BlockPos centerPos = world.getHeight(new BlockPos(x2, 0, z2));
                    new WorldGenBillboard(centerPos.getX() < chunkX * 16 ? EnumFacing.EAST : EnumFacing.WEST, random.nextBoolean()).generate(world, random, centerPos);
                    world.setBlockState(world.getHeight(new BlockPos(x, 0, z)), Blocks.GOLD_BLOCK.getDefaultState());
                }
            }
            if(chunkZ % 16 == 0){
                if(random.nextInt(2) == 0){
                    int x2 = x + random.nextInt(15);
                    int z2 = z + random.nextInt(15);
                    BlockPos centerPos = world.getHeight(new BlockPos(x2, 0, z2));
                    new WorldGenVehicle(random.nextBoolean() ? EnumFacing.WEST : EnumFacing.EAST).generate(world, random, centerPos);
                }
                if(random.nextInt(5) == 0 && chunkX % 16 != 0){
                    int x2 = x + random.nextInt(15);
                    int z2 = random.nextBoolean() ?  (z + 7 + random.nextInt(7)) : z - random.nextInt(7);
                    BlockPos centerPos = world.getHeight(new BlockPos(x2, 0, z2));
                    new WorldGenBillboard(centerPos.getZ() < chunkZ * 16 ? EnumFacing.NORTH : EnumFacing.SOUTH, random.nextBoolean()).generate(world, random, centerPos);
                    world.setBlockState(world.getHeight(new BlockPos(x, 0, z)), Blocks.DIAMOND_BLOCK.getDefaultState());
                }
            }
        }
    }
}
