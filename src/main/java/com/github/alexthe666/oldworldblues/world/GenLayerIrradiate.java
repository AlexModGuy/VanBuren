package com.github.alexthe666.oldworldblues.world;

import com.github.alexthe666.oldworldblues.init.OWBWorld;
import com.github.alexthe666.oldworldblues.world.biome.OWBBiome;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GenLayerIrradiate extends GenLayer {
    private static final Logger LOGGER = LogManager.getLogger();

    public GenLayerIrradiate(long p_i45479_1_, GenLayer parent) {
        super(p_i45479_1_);
        this.parent = parent;
    }

    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
        int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
        for (int i = 0; i < areaHeight; ++i) {
            for (int j = 0; j < areaWidth; ++j) {
                this.initChunkSeed((long) (j + areaX), (long) (i + areaY));
                int k = aint[j + 1 + (i + 1) * (areaWidth + 2)];
                if (k > 255) {
                    LOGGER.debug("old! {}", (int) k);
                }
                Biome biome = Biome.getBiomeForId(k);
                if (biome == null || !(biome instanceof OWBBiome)) {
                    aint2[j + i * areaWidth] = Biome.getIdForBiome(OWBWorld.getIrradiatedVersion(biome, this.nextInt(1)));
                }
            }
        }

        return aint2;
    }
}