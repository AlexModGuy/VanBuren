package com.github.alexthe666.oldworldblues.world.biome;

import com.github.alexthe666.oldworldblues.block.BlockAsphaltRoadLines;
import com.github.alexthe666.oldworldblues.block.BlockCrackedAsphaltRoadLines;
import com.github.alexthe666.oldworldblues.block.IRoad;
import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import com.github.alexthe666.oldworldblues.world.gen.WorldGenBoulder;
import com.github.alexthe666.oldworldblues.world.gen.WorldGenWastelandGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BiomeWasteland extends OWBBiome {
    protected IBlockState stone;
    public static final int GRASS_COLOR = 0XCAB397;
    public BiomeWasteland(String name, BiomeProperties properties) {
        super(name, properties);
        stone = OWBBlocks.IRRADIATED_STONE.getDefaultState();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableMonsterList.clear();
    }

    @SideOnly(Side.CLIENT)
    public int getGrassColorAtPos(BlockPos pos){
        return GRASS_COLOR;
    }

    public int getWaterColorMultiplier(){
        return 0X472F1F;
    }

    public void decorate(World worldIn, Random rand, BlockPos pos) {
        if(pos.getX() % 256 == 0){
            for(int length = 0; length < 15; length++) {
                for (int width = 0; width < 7; width++) {
                    BlockPos bottom = worldIn.getHeight(pos.add(width, 0, length)).down();
                    boolean hole = rand.nextFloat() < Math.abs(width - 3.5F) / 10F;
                    if(!hole && !(worldIn.getBlockState(bottom).getBlock() instanceof IRoad) && worldIn.getBlockState(bottom).getBlock() != OWBBlocks.ASPHALT && worldIn.getBlockState(bottom).getMaterial() != Material.IRON){
                        IBlockState asphalt = rand.nextInt(10)== 0 ? OWBBlocks.ASPHALT.getDefaultState() : OWBBlocks.CRACKED_ASPHALT.getDefaultState();
                        IBlockState lines = rand.nextInt(10)== 0 ? OWBBlocks.ASPHALT_ROAD_LINES.getDefaultState().withProperty(BlockAsphaltRoadLines.E_AXIS, BlockAsphaltRoadLines.EnumAxis.Z) : OWBBlocks.CRACKED_ASPHALT_ROAD_LINES.getDefaultState().withProperty(BlockCrackedAsphaltRoadLines.E_AXIS, BlockAsphaltRoadLines.EnumAxis.Z);

                        if (width == 3 && (length % 4 == 0 || length % 4 == 1)) {
                            worldIn.setBlockState(bottom, lines);
                        } else {
                            worldIn.setBlockState(bottom, asphalt);
                        }
                        worldIn.setBlockState(bottom.down(), asphalt);
                    }

                }
            }
        }else if(pos.getZ() % 256 == 0){
             for(int length = 0; length < 15; length++) {
                for (int width = 0; width < 7; width++) {
                    BlockPos bottom = worldIn.getHeight(pos.add(length, 0, width)).down();
                    boolean hole = rand.nextFloat() < Math.abs(width - 3.5F) / 10F;
                    if(!hole && !(worldIn.getBlockState(bottom).getBlock() instanceof IRoad) && worldIn.getBlockState(bottom).getBlock() != OWBBlocks.ASPHALT && worldIn.getBlockState(bottom).getMaterial() != Material.IRON){
                        IBlockState asphalt = rand.nextInt(10)== 0 ? OWBBlocks.ASPHALT.getDefaultState() : OWBBlocks.CRACKED_ASPHALT.getDefaultState();
                        IBlockState lines = rand.nextInt(10)== 0 ? OWBBlocks.ASPHALT_ROAD_LINES.getDefaultState().withProperty(BlockAsphaltRoadLines.E_AXIS, BlockAsphaltRoadLines.EnumAxis.X) : OWBBlocks.CRACKED_ASPHALT_ROAD_LINES.getDefaultState().withProperty(BlockCrackedAsphaltRoadLines.E_AXIS, BlockAsphaltRoadLines.EnumAxis.X);
                        if (width == 3 && (length % 4 == 0 || length % 4 == 1)) {
                            worldIn.setBlockState(bottom, lines);
                        } else {
                            worldIn.setBlockState(bottom, asphalt);
                        }
                        worldIn.setBlockState(bottom.down(), asphalt);
                    }

                }
            }
        }
        super.decorate(worldIn, rand, pos);
        {
            int i = rand.nextInt(2) - 1;
            for (int j = 0; j < i; ++j) {
                int k = rand.nextInt(16) + 8;
                int l = rand.nextInt(16) + 8;
                BlockPos blockpos = worldIn.getHeight(pos.add(k, 0, l));
                new WorldGenBoulder(OWBBlocks.IRRADIATED_STONE.getDefaultState(), 1, true).generate(worldIn, rand, blockpos);
            }
        }
    }

    public WorldGenerator getRandomWorldGenForGrass(Random rand) {
        return new WorldGenWastelandGrass(OWBBlocks.IRRADIATED_TALL_GRASS.getDefaultState());
    }

    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        this.generateWastelandTerrain(worldIn, rand, chunkPrimerIn, x, z, noiseVal);
    }

    public void generateWastelandTerrain(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        int i = worldIn.getSeaLevel();
        IBlockState iblockstate = this.topBlock;
        IBlockState iblockstate1 = this.fillerBlock;
        int j = -1;
        int k = (int) (noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int l = x & 15;
        int i1 = z & 15;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int j1 = 255; j1 >= 0; --j1) {
            if (j1 <= rand.nextInt(5)) {
                chunkPrimerIn.setBlockState(i1, j1, l, BEDROCK);
            } else {
                IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);

                if (iblockstate2.getMaterial() == Material.AIR) {
                    j = -1;
                } else if (iblockstate2.getBlock() == OWBBlocks.IRRADIATED_STONE) {
                    if (j == -1) {
                        if (k <= 0) {
                            iblockstate = AIR;
                            iblockstate1 = stone;
                        } else if (j1 >= i - 4 && j1 <= i + 1) {
                            iblockstate = this.topBlock;
                            iblockstate1 = this.fillerBlock;
                        }

                        if (j1 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR)) {
                            if (this.getTemperature(blockpos$mutableblockpos.setPos(x, j1, z)) < 0.15F) {
                                iblockstate = ICE;
                            } else {
                                iblockstate = WATER;
                            }
                        }

                        j = k;

                        if (j1 >= i - 1) {
                            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate);

                        } else if (j1 < i - 7 - k) {
                            iblockstate = AIR;
                            iblockstate1 = stone;
                            chunkPrimerIn.setBlockState(i1, j1, l, GRAVEL);
                        } else {
                            chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);
                        }
                    } else if (j > 0) {
                        --j;
                        chunkPrimerIn.setBlockState(i1, j1, l, iblockstate1);

                        if (j == 0 && iblockstate1.getBlock() == OWBBlocks.IRRADIATED_SOIL && k > 1) {
                            j = rand.nextInt(4) + Math.max(0, j1 - 63);
                            iblockstate1 = OWBBlocks.IRRADIATED_GRAVEL.getDefaultState();
                        }
                        if (j == 0 && iblockstate1.getBlock() == OWBBlocks.WASTELAND_SAND && k > 1) {
                            j = rand.nextInt(4) + Math.max(0, j1 - 63);
                            iblockstate1 = OWBBlocks.DRY_IRRADIATED_MUD.getDefaultState();
                        }
                    }
                }
            }
        }
    }
}
