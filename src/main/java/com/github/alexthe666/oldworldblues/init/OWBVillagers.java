package com.github.alexthe666.oldworldblues.init;

import com.github.alexthe666.oldworldblues.structure.VillageComponentVan;
import com.github.alexthe666.oldworldblues.structure.VillageVanCreator;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class OWBVillagers {
    public static final VillagerRegistry.VillagerProfession SALESMAN_PROFESSION = new VillagerRegistry.VillagerProfession("oldworldblues:salesman", "oldworldblues:textures/models/entity/villager_salesman.png", "minecraft:textures/entity/zombie_villager/zombie_farmer.png");

    public static void init(){
        VillagerRegistry.VillagerCareer career = new VillagerRegistry.VillagerCareer(SALESMAN_PROFESSION, "salesman");
        career.addTrade(1, new EntityVillager.ListItemForEmeralds(OWBItems.PIPBOY3000IV, new EntityVillager.PriceInfo(1, 3)));
        MapGenStructureIO.registerStructureComponent(VillageComponentVan.class, "VaultTecVan");
        VillagerRegistry.instance().registerVillageCreationHandler(new VillageVanCreator());
    }

}
