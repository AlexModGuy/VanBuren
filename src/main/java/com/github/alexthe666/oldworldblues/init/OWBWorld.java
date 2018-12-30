package com.github.alexthe666.oldworldblues.init;

import com.github.alexthe666.oldworldblues.world.WorldProviderWasteland;
import com.github.alexthe666.oldworldblues.world.biome.BiomeWastelandDesert;
import com.github.alexthe666.oldworldblues.world.biome.BiomeWastelandRiver;
import com.github.alexthe666.oldworldblues.world.biome.BiomeWastelandTemperate;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.DimensionManager;

public class OWBWorld {

    public static DimensionType WASTELAND_DIMENSION;
    public static Biome WASTELAND_PLAINS_BIOME;
    public static Biome WASTELAND_DESERT_BIOME;
    public static Biome WASTELAND_RIVER_BIOME;

    public static void init(){
        WASTELAND_PLAINS_BIOME = new BiomeWastelandTemperate("Open Wastes", new Biome.BiomeProperties("wasteland_plains").setRainDisabled().setTemperature(0.8F).setBaseHeight(0.125F).setHeightVariation(0.05F));
        WASTELAND_DESERT_BIOME = new BiomeWastelandDesert("Irradiated Desert", new Biome.BiomeProperties("wasteland_desert").setRainDisabled().setBaseHeight(0.125F).setHeightVariation(0.05F).setTemperature(2.0F));
        WASTELAND_RIVER_BIOME = new BiomeWastelandRiver("Toxic River", new Biome.BiomeProperties("wasteland_river").setRainDisabled().setBaseHeight(-0.5F).setHeightVariation(0.0F));
        WASTELAND_DIMENSION = DimensionType.register("Wasteland", "_wasteland", 76, WorldProviderWasteland.class, false);
        DimensionManager.registerDimension(76, WASTELAND_DIMENSION);
    }

    public static Biome getIrradiatedVersion(Biome biome, int random){
        if(biome == null){
            return random == 1 ? WASTELAND_DESERT_BIOME : WASTELAND_PLAINS_BIOME;
        }else{
            if(BiomeDictionary.hasType(biome, BiomeDictionary.Type.SANDY)){
                return WASTELAND_DESERT_BIOME;
            }
            return WASTELAND_PLAINS_BIOME;
        }
    }
}
