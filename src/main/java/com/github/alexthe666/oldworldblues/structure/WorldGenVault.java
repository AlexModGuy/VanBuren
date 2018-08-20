package com.github.alexthe666.oldworldblues.structure;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.block.BlockInteriorVaultDoor;
import com.github.alexthe666.oldworldblues.block.BlockInteriorVaultDoorFrame;
import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockGlazedTerracotta;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGenVault extends WorldGenerator {
    public static final ResourceLocation VAULT_ROOM_ENTRANCE = new ResourceLocation(OldWorldBlues.MODID, "vault_room_entrance");
    public static final ResourceLocation VAULT_ROOM_ENTRANCE_OUTSIDE = new ResourceLocation(OldWorldBlues.MODID, "vault_room_entrance_outside");
    public static final ResourceLocation VAULT_ROOM_ATRIUM = new ResourceLocation(OldWorldBlues.MODID, "vault_room_atrium");
    public static final ResourceLocation VAULT_ROOM_TUNNEL = new ResourceLocation(OldWorldBlues.MODID, "vault_room_tunnel");
    public static final ResourceLocation VAULT_ROOM_RESIDENTIAL = new ResourceLocation(OldWorldBlues.MODID, "vault_room_residential");
    public static final ResourceLocation VAULT_ROOM_RECREATIONAL = new ResourceLocation(OldWorldBlues.MODID, "vault_room_recreational");
    public static final ResourceLocation VAULT_ROOM_CAFETERIA = new ResourceLocation(OldWorldBlues.MODID, "vault_room_cafeteria");
    public static final ResourceLocation VAULT_ROOM_BUNKS = new ResourceLocation(OldWorldBlues.MODID, "vault_room_bunks");
    public static final ResourceLocation VAULT_ROOM_OFFICE = new ResourceLocation(OldWorldBlues.MODID, "vault_room_office");
    public static final ResourceLocation VAULT_ROOM_STORAGE = new ResourceLocation(OldWorldBlues.MODID, "vault_room_storage");
    public List<BlockPos> doorPositions;
    public Rotation rotation;
    public int roomChance = 200;
    public int tunnelLength;
    public int doorCount = 0;
    public RoomType nextRoom;
    public List<RoomType> possibleRooms;
    public List<AxisAlignedBB> roomAABBs;
    public int size;
    public BlockPos start;

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        rotation = Rotation.values()[rand.nextInt(Rotation.values().length)];
        doorPositions = new ArrayList<>();
        possibleRooms = new ArrayList<>();
        roomAABBs = new ArrayList<>();
        doorCount = 0;
        size = 0;
        for(RoomType room : RoomType.values()){
            possibleRooms.add(room);
        }
        roomChance = 200;
        start = position;
        tryGenerateRoom(VAULT_ROOM_ENTRANCE, worldIn, position, rand, rotation, false, false, StructureUtils.ProcessorType.VAULT, LootTableList.EMPTY, this);
        return true;
    }

    public boolean canGenerateRoom(World world, BlockPos doorPos, Random random, EnumFacing facing) {
        nextRoom = RoomType.getRandom(possibleRooms);
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        Template template = templateManager.getTemplate(world.getMinecraftServer(), nextRoom.structure);
        int largestSize = Math.max(template.getSize().getX(), template.getSize().getZ());
        tunnelLength = (largestSize / 4) + 2;
        int actualLength = tunnelLength * 4;
        BlockPos pathPos = doorPos.offset(facing, actualLength);
        AxisAlignedBB boundingBox = rotateAABB(nextRoom.sizeAABB, facing).offset(pathPos);
        AxisAlignedBB tunnelBox = rotateAABB(new AxisAlignedBB(0, 0, -1, 7, 7, actualLength - 1), facing).offset(doorPos);
        for(AxisAlignedBB box : roomAABBs){
            if (box.intersects(boundingBox) || tunnelBox.intersects(boundingBox)) {
                return false;
            }
        }
        return true;
    }

    public void generateDoor(World world, BlockPos doorPos, Random random, EnumFacing facing){
        doorCount++;
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        Template template = templateManager.getTemplate(world.getMinecraftServer(), nextRoom.structure);
        int largestSize = Math.max(template.getSize().getX(), template.getSize().getZ());
        doorPositions.add(doorPos);
        roomChance = Math.round(roomChance * 0.75F);
        BlockPos pathPos = doorPos;
        int segments = 0;
        while(segments < tunnelLength) {
            StructureUtils.generateStructureAtWithRotation(VAULT_ROOM_TUNNEL, world, pathPos, random, getRotationFromFacing(facing), false, false, StructureUtils.ProcessorType.VAULT_DECO, LootTableList.EMPTY, this);
            segments++;
            pathPos = pathPos.offset(facing, 4);

        }
        BlockPos basePosition = pathPos;
        switch(getRotationFromFacing(facing)){
            case NONE://Green
                basePosition = basePosition.offset(EnumFacing.NORTH, 1 - largestSize).offset(facing.rotateY(), 2).up(2);
                break;
            case CLOCKWISE_90://Red
                basePosition = basePosition.offset(facing.rotateY(), 2).up(2);
                break;
            case COUNTERCLOCKWISE_90://Yellow
                basePosition = basePosition.offset(EnumFacing.WEST, 1- largestSize).offset(facing.rotateYCCW(), 4).up(2);
                break;
            case CLOCKWISE_180://Blue
                basePosition = basePosition.offset(facing.rotateYCCW(), 4).up(2);
                break;
        }
        IBlockState doorState = OWBBlocks.INTERIOR_VAULT_DOOR.getDefaultState().withProperty(BlockInteriorVaultDoor.FACING, facing.getOpposite());
        tryGenerateRoom(nextRoom, world, pathPos, random, getRotationFromFacing(facing), false, false, StructureUtils.ProcessorType.VAULT, LootTableList.EMPTY, this);
        world.setBlockState(basePosition, doorState, 2);
        for (int y = 0; y < 3; y++) {
            BlockPos ySearch = basePosition.up(y);
            for (int xz = 0; xz < 3; xz++) {
                BlockPos xzSearch = ySearch.offset(doorState.getValue(BlockInteriorVaultDoor.FACING).rotateYCCW(), xz);
                world.setBlockState(xzSearch, OWBBlocks.INTERIOR_VAULT_DOOR_FRAME.getDefaultState().withProperty(BlockInteriorVaultDoorFrame.FACING, doorState.getValue(BlockInteriorVaultDoor.FACING)), 2);
            }
        }
        world.setBlockState(basePosition, doorState, 2);
    }

    private EnumFacing getNextRotation(EnumFacing facing, Random random){
        EnumFacing next;
        do{
            next = EnumFacing.HORIZONTALS[random.nextInt(EnumFacing.HORIZONTALS.length - 1)];
        }while(next != facing.getOpposite());
        return next;
    }

    public boolean addAABB(BlockPos pos, AxisAlignedBB box, EnumFacing facing){
        AxisAlignedBB aabb = rotateAABB(box, facing).offset(pos);
        for(AxisAlignedBB aabb2 : roomAABBs){
            if (aabb.intersects(aabb2)) {
                return false;
            }
        }
        roomAABBs.add(aabb);
        return true;
    }

    public static Rotation getRotationFromFacing(EnumFacing facing){
        switch (facing){
            case EAST:
                return Rotation.CLOCKWISE_90;
            case SOUTH:
                return Rotation.CLOCKWISE_180;
            case WEST:
                return Rotation.COUNTERCLOCKWISE_90;
            default:
                return Rotation.NONE;
        }
    }

    private static AxisAlignedBB rotateAABB(AxisAlignedBB aabb, EnumFacing facing){
        if(facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
            return new AxisAlignedBB(aabb.minZ, aabb.minY, aabb.minX, aabb.maxZ, aabb.maxY, aabb.maxX);
        }else{
            return aabb;
        }
    }

    public static boolean tryGenerateRoom(RoomType structure, World world, BlockPos pos, Random random, Rotation rotation, boolean buildDoor, boolean removeAir, StructureUtils.ProcessorType type, ResourceLocation loot, WorldGenVault vault) {
        Mirror mirror = Mirror.NONE;
        MinecraftServer server = world.getMinecraftServer();
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(rotation).setMirror(mirror);
        if (removeAir) {
            settings.setReplacedBlock(Blocks.AIR);
        }
        Template template = templateManager.getTemplate(server, structure.structure);
        EnumFacing facing = rotation.rotate(EnumFacing.NORTH);
        int xSize = template.getSize().getX();
        int zSize = template.getSize().getZ();
        BlockPos center = pos;
        switch(rotation){
            case NONE:
                center = pos;
                world.setBlockState(pos.down(6), Blocks.GREEN_GLAZED_TERRACOTTA.getDefaultState().withProperty(BlockGlazedTerracotta.FACING, facing));
                break;
            case CLOCKWISE_90:
                center = pos.add(template.getSize().getZ() - 1, 0, 0);
                xSize = template.getSize().getZ();
                zSize = template.getSize().getX();
                world.setBlockState(pos.down(6), Blocks.RED_GLAZED_TERRACOTTA.getDefaultState().withProperty(BlockGlazedTerracotta.FACING, facing));
                break;
            case COUNTERCLOCKWISE_90:
                center = pos.add(0, 0, template.getSize().getX() - 1);
                xSize = template.getSize().getZ();
                zSize = template.getSize().getX();
                world.setBlockState(pos.down(6), Blocks.YELLOW_GLAZED_TERRACOTTA.getDefaultState().withProperty(BlockGlazedTerracotta.FACING, facing));

                break;
            case CLOCKWISE_180:
                center = pos.add(template.getSize().getX() - 1, 0, template.getSize().getZ() - 1);
                world.setBlockState(pos.down(6), Blocks.BLUE_GLAZED_TERRACOTTA.getDefaultState().withProperty(BlockGlazedTerracotta.FACING, facing));
                break;
        }
        for(int i = 0; i < xSize; i++){
            for(int j = 0; j < zSize; j++){
                world.setBlockState(pos.add(i, -5, j), Blocks.GLOWSTONE.getDefaultState());
            }
        }
        if(vault != null){
            AxisAlignedBB box = new AxisAlignedBB(0, 0, 0, xSize, template.getSize().getY(), zSize);
            if(!vault.addAABB(center, box, facing)){
                return false;
            }
        }
        template.addBlocksToWorld(world, center, type.get(center, settings, loot, vault, structure), settings, 2);
        return true;
    }

    public static boolean tryGenerateRoom(ResourceLocation structure, World world, BlockPos pos, Random random, Rotation rotation, boolean buildDoor, boolean removeAir, StructureUtils.ProcessorType type, ResourceLocation loot, WorldGenVault vault) {
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
                world.setBlockState(pos.down(6), Blocks.GREEN_GLAZED_TERRACOTTA.getDefaultState().withProperty(BlockGlazedTerracotta.FACING, facing));
                break;
            case CLOCKWISE_90:
                center = pos.add(template.getSize().getZ() - 1, 0, 0);
                xSize = template.getSize().getZ();
                zSize = template.getSize().getX();
                world.setBlockState(pos.down(6), Blocks.RED_GLAZED_TERRACOTTA.getDefaultState().withProperty(BlockGlazedTerracotta.FACING, facing));
                break;
            case COUNTERCLOCKWISE_90:
                center = pos.add(0, 0, template.getSize().getX() - 1);
                xSize = template.getSize().getZ();
                zSize = template.getSize().getX();
                world.setBlockState(pos.down(6), Blocks.YELLOW_GLAZED_TERRACOTTA.getDefaultState().withProperty(BlockGlazedTerracotta.FACING, facing));

                break;
            case CLOCKWISE_180:
                center = pos.add(template.getSize().getX() - 1, 0, template.getSize().getZ() - 1);
                world.setBlockState(pos.down(6), Blocks.BLUE_GLAZED_TERRACOTTA.getDefaultState().withProperty(BlockGlazedTerracotta.FACING, facing));
                break;
        }
        for(int i = 0; i < xSize; i++){
            for(int j = 0; j < zSize; j++){
                world.setBlockState(pos.add(i, -5, j), Blocks.GLOWSTONE.getDefaultState());
            }
        }
        if(vault != null){
            AxisAlignedBB box = new AxisAlignedBB(0, 0, 0, xSize, template.getSize().getY(), zSize);
            if(!vault.addAABB(center, box, facing)){
                return false;
            }
        }
        if(center.getDistance(vault.start.getX(), vault.start.getY(), vault.start.getZ()) > vault.size){
            vault.size = (int)center.getDistance(vault.start.getX(), vault.start.getY(), vault.start.getZ());
        }
        template.addBlocksToWorld(world, center, type.get(center, settings, loot, vault, RoomType.OFFICE), settings, 2);
        return true;
    }


    enum RoomType{
        ATRIUM(VAULT_ROOM_ATRIUM, 80, true, new AxisAlignedBB(0, 0, 0, 15, 12, 27)),
        RESIDENTIAL(VAULT_ROOM_RESIDENTIAL, 60, false, new AxisAlignedBB(0, 0, 0, 11, 7, 11), Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE)),
        RECREATIONAL(VAULT_ROOM_RECREATIONAL, 40, false, new AxisAlignedBB(0, 0, 0, 16, 7, 16)),
        CAFETERIA(VAULT_ROOM_CAFETERIA, 40, true, new AxisAlignedBB(0, 0, 0, 11, 7, 11), Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE)),
        BUNKS(VAULT_ROOM_BUNKS, 50, false, new AxisAlignedBB(0, 0, 0, 16, 7, 16)),
        OFFICE(VAULT_ROOM_OFFICE, 40, false, new AxisAlignedBB(0, 0, 0, 7, 7, 7)),
        STORAGE(VAULT_ROOM_STORAGE, 40, false, new AxisAlignedBB(0, 0, 0, 11, 7, 11));

        private int weight;
        public ResourceLocation structure;
        private boolean single;
        public AxisAlignedBB sizeAABB;
        public IBlockState wallState;
        RoomType(ResourceLocation structure, int weight, boolean single, AxisAlignedBB sizeAABB){
            this.weight = weight;
            this.structure = structure;
            this.single = single;
            this.sizeAABB = sizeAABB;
            this.wallState = OWBBlocks.VAULT_WALL.getDefaultState();
        }

        RoomType(ResourceLocation structure, int weight, boolean single, AxisAlignedBB sizeAABB, IBlockState wall){
            this.weight = weight;
            this.structure = structure;
            this.single = single;
            this.sizeAABB = sizeAABB;
            this.wallState = wall;
        }

        public static RoomType getRandom(List<RoomType> list){
            double completeWeight = 0;
            for (RoomType item : list) {
                completeWeight += item.weight;
            }
            double r = Math.random() * completeWeight;
            double countWeight = 0;
            for (RoomType item : list) {
                countWeight += item.weight;
                if (countWeight >= r) {
                    if(item.single){
                        list.remove(item);
                    }
                    return item;
                }
            }
            return ATRIUM;
        }
    }
}
