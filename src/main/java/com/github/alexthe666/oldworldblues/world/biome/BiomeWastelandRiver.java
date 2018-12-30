package com.github.alexthe666.oldworldblues.world.biome;

import com.github.alexthe666.oldworldblues.init.OWBBlocks;

public class BiomeWastelandRiver extends BiomeWasteland {

    public BiomeWastelandRiver(String name, BiomeProperties properties) {
        super(name, properties);
        this.topBlock = OWBBlocks.IRRADIATED_MUD.getDefaultState();
        this.fillerBlock = OWBBlocks.IRRADIATED_MUD.getDefaultState();
    }
}
