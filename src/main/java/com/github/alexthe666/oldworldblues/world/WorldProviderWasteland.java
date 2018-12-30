package com.github.alexthe666.oldworldblues.world;

import com.github.alexthe666.oldworldblues.init.OWBWorld;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderWasteland extends WorldProvider {

    protected void init() {
        this.hasSkyLight = true;
        this.biomeProvider = new BiomeProviderWasteland(world.getSeed(), world.getWorldType());
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkProviderWasteland(world, world.getSeed(), true, "");
    }

    @Override
    public DimensionType getDimensionType() {
        return OWBWorld.WASTELAND_DIMENSION;
    }
}
