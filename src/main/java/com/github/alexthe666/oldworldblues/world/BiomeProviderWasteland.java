package com.github.alexthe666.oldworldblues.world;

import com.github.alexthe666.oldworldblues.init.OWBWorld;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.WorldTypeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BiomeProviderWasteland extends BiomeProvider {
    private GenLayer genBiomes;
    private GenLayer biomeIndexLayer;
    private BiomeCache biomeCache;
    private List<Biome> biomesToSpawnIn;

    public BiomeProviderWasteland() {
        this.biomeCache = new BiomeCache(this);
        this.biomesToSpawnIn = new ArrayList<>();
        this.biomesToSpawnIn.add(OWBWorld.WASTELAND_PLAINS_BIOME);
        this.biomesToSpawnIn.add(OWBWorld.WASTELAND_DESERT_BIOME);
    }

    public BiomeProviderWasteland(long seed, WorldType worldtype) {
        this();
        GenLayer[] layers = GenLayerWasteland.initializeAllBiomeGenerators(seed, worldtype, null);
        this.genBiomes = layers[0];
        this.biomeIndexLayer = layers[1];
    }

    @Override
    public List<Biome> getBiomesToSpawnIn() {
        return this.biomesToSpawnIn;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getTemperatureAtHeight(float x, int z) {
        return x;
    }

    @Override
    public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height) {
        IntCache.resetIntCache();
        if (biomes == null || biomes.length < width * height) {
            biomes = new Biome[width * height];
        }
        int[] biomeIds = this.genBiomes.getInts(x, z, width, height);
        try {
            for (int i = 0; i < width * height; ++i) {
                biomes[i] = Biome.getBiomeForId(biomeIds[i]);
            }
            return biomes;
        } catch (Throwable throwable) {
            CrashReport report = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory category = report.makeCategory("RawBiomeBlock");
            category.addCrashSection("biomes[] size", biomes.length);
            category.addCrashSection("x", x);
            category.addCrashSection("z", z);
            category.addCrashSection("w", width);
            category.addCrashSection("h", height);
            throw new ReportedException(report);
        }
    }

    @Override
    public Biome[] getBiomes(Biome[] listToReuse, int x, int z, int width, int height) {
        return this.getBiomes(listToReuse, x, z, width, height, true);
    }

    @Override
    public Biome[] getBiomes(Biome[] biomes, int x, int z, int width, int height, boolean flag) {
        IntCache.resetIntCache();

        if (biomes == null || biomes.length < width * height) {
            biomes = new Biome[width * height];
        }

        if (flag && width == 16 && height == 16 && (x & 15) == 0 && (z & 15) == 0) {
            Biome[] cachedBiomes = this.biomeCache.getCachedBiomes(x, z);
            System.arraycopy(cachedBiomes, 0, biomes, 0, width * height);
            return biomes;
        } else {
            int[] indexLayer = this.biomeIndexLayer.getInts(x, z, width, height);

            for (int i = 0; i < width * height; ++i) {
                biomes[i] = Biome.getBiome(indexLayer[i]);
            }

            return biomes;
        }
    }

    @Override
    public boolean areBiomesViable(int x, int y, int list, List allowed) {
        IntCache.resetIntCache();
        int l = x - list >> 2;
        int i1 = y - list >> 2;
        int j1 = x + list >> 2;
        int k1 = y + list >> 2;
        int l1 = j1 - l + 1;
        int i2 = k1 - i1 + 1;
        int[] aint = this.genBiomes.getInts(l, i1, l1, i2);

        try {
            for (int j2 = 0; j2 < l1 * i2; ++j2) {
                Biome biome = Biome.getBiome(aint[j2]);

                if (!allowed.contains(biome)) {
                    return false;
                }
            }

            return true;
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
            crashreportcategory.addCrashSection("Layer", this.genBiomes.toString());
            crashreportcategory.addCrashSection("x", x);
            crashreportcategory.addCrashSection("z", y);
            crashreportcategory.addCrashSection("radius", list);
            crashreportcategory.addCrashSection("allowed", allowed);
            throw new ReportedException(crashreport);
        }
    }

    @Override
    public BlockPos findBiomePosition(int x, int y, int z, List allowed, Random rand) {
        IntCache.resetIntCache();
        int l = x - z >> 2;
        int i1 = y - z >> 2;
        int j1 = x + z >> 2;
        int k1 = y + z >> 2;
        int l1 = j1 - l + 1;
        int i2 = k1 - i1 + 1;
        int[] aint = this.genBiomes.getInts(l, i1, l1, i2);
        BlockPos chunkposition = null;
        int j2 = 0;

        for (int k2 = 0; k2 < l1 * i2; ++k2) {
            int l2 = l + k2 % l1 << 2;
            int i3 = i1 + k2 / l1 << 2;
            Biome biome = Biome.getBiome(aint[k2]);

            if (allowed.contains(biome) && (chunkposition == null || rand.nextInt(j2 + 1) == 0)) {
                chunkposition = new BlockPos(l2, 0, i3);
                ++j2;
            }
        }

        return chunkposition;
    }

    @Override
    public void cleanupCache() {
        this.biomeCache.cleanupCache();
    }

    @Override
    public GenLayer[] getModdedBiomeGenerators(WorldType worldType, long seed, GenLayer[] original) {
        WorldTypeEvent.InitBiomeGens event = new WorldTypeEvent.InitBiomeGens(worldType, seed, original);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return event.getNewBiomeGens();
    }
}
