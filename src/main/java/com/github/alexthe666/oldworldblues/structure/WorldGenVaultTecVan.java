package com.github.alexthe666.oldworldblues.structure;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;

import java.util.Random;

public class WorldGenVaultTecVan extends WorldGenerator {

    private static final ResourceLocation VAULT_TEC_VAN = new ResourceLocation(OldWorldBlues.MODID, "vault_tec_van");

    private Rotation rotation;
    public WorldGenVaultTecVan(EnumFacing facing){
        switch(facing){
            case SOUTH:
                rotation = Rotation.CLOCKWISE_180;
                break;
            case EAST:
                rotation = Rotation.CLOCKWISE_90;
                break;
            case WEST:
                rotation = Rotation.COUNTERCLOCKWISE_90;
                break;
            default:
                rotation = Rotation.NONE;
                break;
        }
    }
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        if (worldIn == null) {
            return false;
        }
        StructureUtils.generateStructureAtRotation(VAULT_TEC_VAN, worldIn, position.up(), rand, rotation);
        return true;
    }
}
