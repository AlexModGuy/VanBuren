package com.github.alexthe666.oldworldblues.world.biome;

public class OWBBiome extends net.minecraft.world.biome.Biome {

    public OWBBiome(String name, BiomeProperties properties) {
        super(properties);
        this.setRegistryName("oldworldblues", name);
    }
}
