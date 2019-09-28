package com.github.alexthe666.oldworldblues.world.biome;

import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BiomeWastelandRiver extends BiomeWasteland {

    public BiomeWastelandRiver(String name, BiomeProperties properties) {
        super(name, properties);
        this.topBlock = OWBBlocks.IRRADIATED_MUD.getDefaultState();
        this.fillerBlock = OWBBlocks.IRRADIATED_MUD.getDefaultState();
    }

    @SideOnly(Side.CLIENT)
    public int getSkyColorByTemp(float currentTemperature){
        return 0XA1BCE9;
    }
}
