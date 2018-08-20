package com.github.alexthe666.oldworldblues.structure;

import net.minecraft.block.BlockGlazedTerracotta;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class StructureUtils {

    public static boolean generateStructureAtWithRotation(ResourceLocation structure, World world, BlockPos pos, Random random, Rotation rotation, boolean checkGround, boolean removeAir, ProcessorType type, ResourceLocation loot, Object... processorArgs) {
        Mirror mirror = Mirror.NONE;
        MinecraftServer server = world.getMinecraftServer();
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(rotation).setMirror(mirror);
        if (removeAir) {
            settings.setReplacedBlock(Blocks.AIR);
        }
        Template template = templateManager.getTemplate(server, structure);
        EnumFacing facing = rotation.rotate(EnumFacing.NORTH);
        int xSize = template.getSize().getX();
        int zSize = template.getSize().getZ();
        BlockPos center = pos;
        switch(rotation){
            case NONE:
                center = pos;
                world.setBlockState(pos.down().east(), Blocks.GREEN_GLAZED_TERRACOTTA.getDefaultState().withProperty(BlockGlazedTerracotta.FACING, facing));
                break;
            case CLOCKWISE_90:
                center = pos.add(template.getSize().getZ() - 1, 0, 0);
                xSize = template.getSize().getZ();
                zSize = template.getSize().getX();
                world.setBlockState(pos.down(), Blocks.RED_GLAZED_TERRACOTTA.getDefaultState().withProperty(BlockGlazedTerracotta.FACING, facing));
                break;
            case COUNTERCLOCKWISE_90:
                center = pos.add(0, 0, template.getSize().getX() - 1);
                xSize = template.getSize().getZ();
                zSize = template.getSize().getX();
                world.setBlockState(pos.down(), Blocks.YELLOW_GLAZED_TERRACOTTA.getDefaultState().withProperty(BlockGlazedTerracotta.FACING, facing));
                break;
            case CLOCKWISE_180:
                center = pos.add(template.getSize().getX() - 1, 0, template.getSize().getZ() - 1);
                world.setBlockState(pos.down(), Blocks.BLUE_GLAZED_TERRACOTTA.getDefaultState().withProperty(BlockGlazedTerracotta.FACING, facing));
        }
        for(int i = 0; i < xSize; i++){
            for(int j = 0; j < zSize; j++){
                world.setBlockState(pos.add(i, -5, j), Blocks.GLOWSTONE.getDefaultState());
            }
        }
        if(processorArgs[0] != null && processorArgs[0] instanceof WorldGenVault){
            AxisAlignedBB box = new AxisAlignedBB(0, 0, 0, xSize, template.getSize().getY(), zSize);
            //((WorldGenVault)processorArgs[0]).addAABB(center, box, facing);
        }
        if (checkGround) {
            BlockPos corner1 = pos.down();
            BlockPos corner2 = pos.add(template.getSize().getX(), -1, 0);
            BlockPos corner3 = pos.add(template.getSize().getX(), -1, template.getSize().getZ());
            BlockPos corner4 = pos.add(0, -1, template.getSize().getZ());
            if (world.getBlockState(center).isOpaqueCube() && world.getBlockState(corner1).isOpaqueCube() && world.getBlockState(corner2).isOpaqueCube() && world.getBlockState(corner3).isOpaqueCube() && world.getBlockState(corner4).isOpaqueCube()) {
                template.addBlocksToWorld(world, center, type.get(center, settings, loot, processorArgs), settings, 2);
                return true;
            }
            return false;
        } else {
            template.addBlocksToWorld(world, center, type.get(center, settings, loot, processorArgs), settings, 2);
            return true;
        }
    }

    public static boolean generateStructureAtWithRotationOffset(ResourceLocation structure, World world, BlockPos pos, Random random, Rotation rotation, boolean checkGround, boolean removeAir, ProcessorType type, ResourceLocation loot, Object... processorArgs) {
        Mirror mirror = Mirror.NONE;
        MinecraftServer server = world.getMinecraftServer();
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(rotation).setMirror(mirror);
        if (removeAir) {
            settings.setReplacedBlock(Blocks.AIR);
        }
        Template template = templateManager.getTemplate(server, structure);
        EnumFacing facing = rotation.rotate(EnumFacing.NORTH);
        int xSize = template.getSize().getX();
        int zSize = template.getSize().getZ();
        BlockPos center = pos;
        for(int i = 0; i < xSize; i++){
            for(int j = 0; j < zSize; j++){
                world.setBlockState(pos.add(i, -5, j), Blocks.GLOWSTONE.getDefaultState());
            }
        }
        if(processorArgs[0] != null && processorArgs[0] instanceof WorldGenVault){
            AxisAlignedBB box = new AxisAlignedBB(0, 0, 0, xSize, template.getSize().getY(), zSize);
            //((WorldGenVault)processorArgs[0]).addAABB(center, box, facing);
        }
        if (checkGround) {
            BlockPos corner1 = pos.down();
            BlockPos corner2 = pos.add(template.getSize().getX(), -1, 0);
            BlockPos corner3 = pos.add(template.getSize().getX(), -1, template.getSize().getZ());
            BlockPos corner4 = pos.add(0, -1, template.getSize().getZ());
            if (world.getBlockState(center).isOpaqueCube() && world.getBlockState(corner1).isOpaqueCube() && world.getBlockState(corner2).isOpaqueCube() && world.getBlockState(corner3).isOpaqueCube() && world.getBlockState(corner4).isOpaqueCube()) {
                template.addBlocksToWorld(world, center, type.get(center, settings, loot, processorArgs), settings, 2);
                return true;
            }
            return false;
        } else {
            template.addBlocksToWorld(world, center, type.get(center, settings, loot, processorArgs), settings, 2);
            return true;
        }
    }

    public static void generateStructureAtRotation(ResourceLocation structure, World world, BlockPos pos, Random random, Rotation rotation){
        Mirror mirror = Mirror.NONE;
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

    public enum ProcessorType{
        DEFAULT,
        LOOT,
        VAULT,
        VAULT_DECO;

        public OWBBlockProcessor get(BlockPos pos, PlacementSettings settings, ResourceLocation loot, Object... args){
            switch(this){
                case DEFAULT:
                    return new OWBBlockProcessor(pos, settings);
                case LOOT:
                    return new OWBBlockProcessorLoot(pos, settings, loot);
                case VAULT:
                    if(args[0] instanceof WorldGenVault && args[1] instanceof WorldGenVault.RoomType){
                        return new OWBBlockProcessorVault(pos, settings, loot, (WorldGenVault)args[0], (WorldGenVault.RoomType)args[1]);
                    }else{
                        return new OWBBlockProcessorLoot(pos, settings, loot);
                    }
                case VAULT_DECO:
                    if(args[0] instanceof WorldGenVault){
                        return new OWBBlockProcessorVaultDeco(pos, settings, loot, (WorldGenVault)args[0]);
                    }else{
                        return new OWBBlockProcessorLoot(pos, settings, loot);
                    }
            }
            return null;
        }
    }
}
