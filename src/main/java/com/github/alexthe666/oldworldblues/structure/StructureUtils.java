package com.github.alexthe666.oldworldblues.structure;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class StructureUtils {

    public static void generateStructureAtRotation(ResourceLocation structure, World world, BlockPos pos, Random random, Rotation rotation){
        Mirror mirror = Mirror.values()[random.nextInt(Mirror.values().length)];
        MinecraftServer server = world.getMinecraftServer();
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(rotation).setMirror(mirror);
        Template template = templateManager.getTemplate(server, structure);
        BlockPos center = pos.add(template.getSize().getX() / 2, 0, template.getSize().getZ() / 2);
        template.addBlocksToWorldChunk(world, center, settings);
    }

    public static void generateStructureAt(ResourceLocation structure, World world, BlockPos pos, Random random){
        Rotation rotation = Rotation.values()[random.nextInt(Rotation.values().length)];
        Mirror mirror = Mirror.values()[random.nextInt(Mirror.values().length)];
        MinecraftServer server = world.getMinecraftServer();
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(rotation).setMirror(mirror);
        Template template = templateManager.getTemplate(server, structure);
        BlockPos center = pos.add(template.getSize().getX() / 2, 0, template.getSize().getZ() / 2);
        template.addBlocksToWorldChunk(world, center, settings);
    }
}
