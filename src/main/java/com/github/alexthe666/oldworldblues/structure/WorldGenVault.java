package com.github.alexthe666.oldworldblues.structure;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.block.*;
import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import com.github.alexthe666.oldworldblues.world.OWBWorldData;
import com.github.alexthe666.oldworldblues.world.VaultData;
import com.google.common.collect.Maps;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
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
import java.util.Map;
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
    public static final ResourceLocation DEBUG = new ResourceLocation(OldWorldBlues.MODID, "debug");
    public List<BlockPos> doorPositions;
    public int roomChance = 200;
    public int tunnelLength;
    public int doorCount = 0;
    public int currentRooms = 0;
    public int maxRooms = 0;
    public RoomType nextRoom;
    public AxisAlignedBB nextRoomAABB;
    public EnumFacing nextRoomFacing;
    public List<AxisAlignedBB> roomAABBs;
    public int size;
    public BlockPos start;
    public Random random;
    //Air blocks for decoration that are ON THE GROUND
    private Map<BlockPos, RoomType> emeraldBlocks;
    //Air blocks for decoration that are AGAINST A WALL or ABOVE A DESK
    private Map<BlockPos, RoomType> diamondBlocks;
    //Air blocks for decoration that are ABOVE EMERALD BLOCKS
    private Map<BlockPos, RoomType> goldBlocks;
    public static Random ROOM_RANDOM;
    private boolean hasAtrium;
    public int vaultNumber = 0;
    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        hasAtrium = false;
        doorPositions = new ArrayList<>();
        roomAABBs = new ArrayList<>();
        emeraldBlocks =  Maps.newHashMap();
        diamondBlocks =  Maps.newHashMap();
        goldBlocks =  Maps.newHashMap();
        doorCount = 0;
        size = 0;
        vaultNumber = 1 + rand.nextInt(200);
        roomChance = 100;
        currentRooms = 0;
        maxRooms = 6 + rand.nextInt(11);
        start = position;
        nextRoomFacing = EnumFacing.getHorizontal(rand.nextInt(3));
        EnumFacing facingNow = EnumFacing.getHorizontal(rand.nextInt(3));
        random = rand;
        generateCaveEnterance(worldIn, position.offset(facingNow.getOpposite(), 9).offset(facingNow.rotateY(), 8).down(2), rand, facingNow.getOpposite());
        tryGenerateRoom(VAULT_ROOM_ENTRANCE, worldIn, position, rand, getRotationFromFacing(facingNow), false, false, StructureUtils.ProcessorType.VAULT, LootTableList.EMPTY, this, RoomType.ENTERANCE);
        tryGenerateRoom(VAULT_ROOM_ENTRANCE_OUTSIDE, worldIn, position.offset(facingNow.getOpposite(), 12).offset(facingNow.rotateY(), 5), rand, getRotationFromFacing(facingNow), false, false, StructureUtils.ProcessorType.VAULT_DECO, LootTableList.EMPTY, this, RoomType.ENTERANCE);
        checkAndAddAABB(RoomType.ENTERANCE, worldIn, position, getRotationFromFacing(facingNow), this);
        OWBWorldData.addVault(worldIn, new VaultData(position, position));
        worldIn.setBlockState(position, Blocks.DIAMOND_BLOCK.getDefaultState());
        decorateRooms(worldIn, rand);
        nextRoomAABB = null;
        return true;
    }

    private void generateCaveEnterance(World worldIn, BlockPos offset, Random rand, EnumFacing facing) {
        int size = 3;
        BlockPos pos = offset;
        while(!worldIn.canBlockSeeSky(pos.offset(facing, 2).down())) {
            pos = pos.offset(facing).up();
            for (float i = 0; i < size; i += 0.5) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5) {
                    int x = (int) Math.floor(Math.sin(j) * i);
                    int z = (int) Math.floor(Math.cos(j) * i);
                    if (facing.getAxis() == EnumFacing.Axis.Z) {
                        worldIn.setBlockToAir(pos.add(x, z, 0));
                    } else {
                        worldIn.setBlockToAir(pos.add(0, z, x));

                    }
                }
            }
        }
    }

    public boolean canGenerateRoom(World world, BlockPos doorPos, Random random, EnumFacing facing) {
        nextRoom = RoomType.getRandom(this);
        tunnelLength = random.nextInt(3) + 2;
        int actualLength = tunnelLength * 4;
        BlockPos pathPos = doorPos.offset(facing, actualLength);
        BlockPos roomPos = pathPos.up().offset(facing.getOpposite()).offset(facing.rotateY());
        return checkAndAddAABB(nextRoom, world, offsetRoom(world, roomPos, facing, nextRoom.structure), getRotationFromFacing(facing), this);
    }

    public void generateDoor(World world, BlockPos doorPos, Random random, EnumFacing facing) {
        doorCount++;
        nextRoomFacing = facing;
        doorPositions.add(doorPos);
        roomChance = Math.round(roomChance * 0.75F);
        BlockPos pathPos = doorPos.offset(facing.getOpposite().rotateY(), -3).down(4);
        int segments = 0;

        while (segments < tunnelLength && generateTunnel(world, pathPos, facing)) {
            segments++;
            pathPos = pathPos.offset(facing, 4);

        }
        BlockPos basePosition = pathPos.offset(facing.getOpposite().rotateY(), 4).up(3);
        IBlockState doorState = OWBBlocks.INTERIOR_VAULT_DOOR.getDefaultState().withProperty(BlockInteriorVaultDoor.FACING, facing.getOpposite());
        BlockPos roomPos = pathPos.up().offset(facing.getOpposite()).offset(facing.rotateY());
        tryGenerateRoom(nextRoom, world, offsetRoom(world, roomPos, nextRoomFacing, nextRoom.structure), random, getRotationFromFacing(nextRoomFacing), false, false, StructureUtils.ProcessorType.VAULT, LootTableList.EMPTY, this);

        switch (getRotationFromFacing(nextRoomFacing)) {
            case NONE:
                world.setBlockState(pathPos.offset(facing.getOpposite().rotateY(), 6).up().down(6), Blocks.GREEN_GLAZED_TERRACOTTA.getDefaultState().withProperty(BlockGlazedTerracotta.FACING, facing));
                break;
            case CLOCKWISE_90:
                world.setBlockState(pathPos.offset(facing.getOpposite().rotateY(), 6).up().down(6), Blocks.RED_GLAZED_TERRACOTTA.getDefaultState().withProperty(BlockGlazedTerracotta.FACING, facing));
                break;
            case COUNTERCLOCKWISE_90:
                world.setBlockState(pathPos.offset(facing.getOpposite().rotateY(), 6).up().down(6), Blocks.YELLOW_GLAZED_TERRACOTTA.getDefaultState().withProperty(BlockGlazedTerracotta.FACING, facing));
                break;
            case CLOCKWISE_180:
                world.setBlockState(pathPos.offset(facing.getOpposite().rotateY(), 6).up().down(6), Blocks.BLUE_GLAZED_TERRACOTTA.getDefaultState().withProperty(BlockGlazedTerracotta.FACING, facing));
                break;
        }

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

    private BlockPos offsetRoom(World world, BlockPos start, EnumFacing facing, ResourceLocation struct) {
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        Template template = templateManager.getTemplate(world.getMinecraftServer(), struct);
        int x = template.getSize().getX();
        int z = template.getSize().getZ();
        return start.offset(facing, z).offset(facing.rotateYCCW(), x);
    }

    private boolean generateTunnel(World world, BlockPos pos, EnumFacing facing) {
        tunnelBase(world, pos, facing, false, false);
        tunnelBase(world, pos.offset(facing), facing, true, false);
        tunnelBase(world, pos.offset(facing, 2), facing, false, false);
        tunnelSupport(world, pos.offset(facing, 3), facing, false);
        return true;
    }

    private void tunnelSupport(World world, BlockPos pos, EnumFacing facing, boolean stairs) {
        pos = pos.offset(facing);
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {
                BlockPos frame = pos.offset(facing.rotateYCCW(), x).up(y + 1);
                world.setBlockState(frame, OWBBlocks.BLAST_CONCRETE.getDefaultState());
            }
        }
        for (int x = 2; x < 5; x++) {
            for (int y = 2; y < 5; y++) {
                BlockPos frame = pos.offset(facing.rotateYCCW(), x).up(y + 1);
                world.setBlockState(frame, Blocks.AIR.getDefaultState());
            }
        }
        world.setBlockState(pos.offset(facing.rotateYCCW(), 1).up(3), OWBBlocks.VAULT_SUPPORT.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y));
        world.setBlockState(pos.offset(facing.rotateYCCW(), 1).up(4), OWBBlocks.VAULT_SUPPORT.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y));
        world.setBlockState(pos.offset(facing.rotateYCCW(), 1).up(5), OWBBlocks.VAULT_SUPPORT.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y));

        world.setBlockState(pos.offset(facing.rotateYCCW(), 5).up(3), OWBBlocks.VAULT_SUPPORT.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y));
        world.setBlockState(pos.offset(facing.rotateYCCW(), 5).up(4), OWBBlocks.VAULT_SUPPORT.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y));
        world.setBlockState(pos.offset(facing.rotateYCCW(), 5).up(5), OWBBlocks.VAULT_SUPPORT.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Y));

        world.setBlockState(pos.offset(facing.rotateYCCW(), 2).up(6), OWBBlocks.VAULT_SUPPORT.getDefaultState().withProperty(BlockRotatedPillar.AXIS, facing.rotateY().getAxis()));
        world.setBlockState(pos.offset(facing.rotateYCCW(), 3).up(6), OWBBlocks.VAULT_SUPPORT.getDefaultState().withProperty(BlockRotatedPillar.AXIS, facing.rotateY().getAxis()));
        world.setBlockState(pos.offset(facing.rotateYCCW(), 4).up(6), OWBBlocks.VAULT_SUPPORT.getDefaultState().withProperty(BlockRotatedPillar.AXIS, facing.rotateY().getAxis()));

        world.setBlockState(pos.offset(facing.rotateYCCW(), 2).up(5), OWBBlocks.VAULT_BEAM.getDefaultState().withProperty(BlockHorizontal.FACING, facing.rotateYCCW()).withProperty(BlockVaultBeam.HALF, BlockVaultBeam.Half.TOP));
        world.setBlockState(pos.offset(facing.rotateYCCW(), 3).up(5), OWBBlocks.VAULT_BEAM.getDefaultState().withProperty(BlockHorizontal.FACING, facing.rotateYCCW()).withProperty(BlockVaultBeam.HALF, BlockVaultBeam.Half.TOP));
        world.setBlockState(pos.offset(facing.rotateYCCW(), 4).up(5), OWBBlocks.VAULT_BEAM.getDefaultState().withProperty(BlockHorizontal.FACING, facing.rotateYCCW()).withProperty(BlockVaultBeam.HALF, BlockVaultBeam.Half.TOP));

        if(stairs){
            world.setBlockState(pos.offset(facing.rotateYCCW(), 2).up(2), OWBBlocks.VAULT_METAL_PLATING_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, facing.getOpposite()));
            world.setBlockState(pos.offset(facing.rotateYCCW(), 3).up(2), OWBBlocks.VAULT_METAL_PLATING_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, facing.getOpposite()));
            world.setBlockState(pos.offset(facing.rotateYCCW(), 4).up(2), OWBBlocks.VAULT_METAL_PLATING_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, facing.getOpposite()));
        }else{
            world.setBlockState(pos.offset(facing.rotateYCCW(), 2).up(2), OWBBlocks.VAULT_FLOOR_TILING.getDefaultState());
            world.setBlockState(pos.offset(facing.rotateYCCW(), 3).up(2), OWBBlocks.VAULT_FLOOR_TILING.getDefaultState());
            world.setBlockState(pos.offset(facing.rotateYCCW(), 4).up(2), OWBBlocks.VAULT_FLOOR_TILING.getDefaultState());
        }

        emeraldBlocks.put(pos.offset(facing.rotateYCCW(), 2).up(3), RoomType.TUNNEL);
        emeraldBlocks.put(pos.offset(facing.rotateYCCW(), 3).up(3), RoomType.TUNNEL);
        emeraldBlocks.put(pos.offset(facing.rotateYCCW(), 4).up(3), RoomType.TUNNEL);
        diamondBlocks.put(pos.offset(facing.rotateYCCW(), 2).up(4), RoomType.TUNNEL);
        goldBlocks.put(pos.offset(facing.rotateYCCW(), 3).up(4), RoomType.TUNNEL);
        diamondBlocks.put(pos.offset(facing.rotateYCCW(), 4).up(4), RoomType.TUNNEL);
    }

    private void tunnelBase(World world, BlockPos pos, EnumFacing facing, boolean light, boolean stairs) {
        pos = pos.offset(facing);
        for (int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {
                BlockPos frame = pos.offset(facing.rotateYCCW(), x).up(y + 1);
                world.setBlockState(frame, OWBBlocks.BLAST_CONCRETE.getDefaultState());
            }
        }
        for (int x = 2; x < 5; x++) {
            for (int y = 2; y < 5; y++) {
                BlockPos frame = pos.offset(facing.rotateYCCW(), x).up(y + 1);
                world.setBlockState(frame, Blocks.AIR.getDefaultState());
            }
        }
        world.setBlockState(pos.offset(facing.rotateYCCW(), 1).up(3), OWBBlocks.VAULT_SUPPORT.getDefaultState().withProperty(BlockRotatedPillar.AXIS, facing.getAxis()));
        world.setBlockState(pos.offset(facing.rotateYCCW(), 1).up(4), OWBBlocks.VAULT_WALL.getDefaultState());
        world.setBlockState(pos.offset(facing.rotateYCCW(), 1).up(5), OWBBlocks.VAULT_WALL.getDefaultState());

        world.setBlockState(pos.offset(facing.rotateYCCW(), 5).up(3), OWBBlocks.VAULT_SUPPORT.getDefaultState().withProperty(BlockRotatedPillar.AXIS, facing.getAxis()));
        world.setBlockState(pos.offset(facing.rotateYCCW(), 5).up(4), OWBBlocks.VAULT_WALL.getDefaultState());
        world.setBlockState(pos.offset(facing.rotateYCCW(), 5).up(5), OWBBlocks.VAULT_WALL.getDefaultState());

        world.setBlockState(pos.offset(facing.rotateYCCW(), 2).up(6), OWBBlocks.VAULT_WALL.getDefaultState());
        world.setBlockState(pos.offset(facing.rotateYCCW(), 3).up(6), OWBBlocks.VAULT_WALL.getDefaultState());
        world.setBlockState(pos.offset(facing.rotateYCCW(), 4).up(6), OWBBlocks.VAULT_WALL.getDefaultState());
        if (light) {
            world.setBlockState(pos.offset(facing.rotateYCCW(), 3).up(5), OWBBlocks.VAULT_LIGHTING.getDefaultState().withProperty(BlockHorizontal.FACING, facing).withProperty(BlockVaultLighting.HALF, BlockVaultLighting.Half.TOP));
        }
        if(stairs){
            world.setBlockState(pos.offset(facing.rotateYCCW(), 2).up(2), OWBBlocks.VAULT_METAL_PLATING_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, facing.getOpposite()));
            world.setBlockState(pos.offset(facing.rotateYCCW(), 3).up(2), OWBBlocks.VAULT_METAL_PLATING_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, facing.getOpposite()));
            world.setBlockState(pos.offset(facing.rotateYCCW(), 4).up(2), OWBBlocks.VAULT_METAL_PLATING_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, facing.getOpposite()));
        }else{
            world.setBlockState(pos.offset(facing.rotateYCCW(), 2).up(2), OWBBlocks.VAULT_FLOOR_TILING.getDefaultState());
            world.setBlockState(pos.offset(facing.rotateYCCW(), 3).up(2), OWBBlocks.VAULT_FLOOR_TILING.getDefaultState());
            world.setBlockState(pos.offset(facing.rotateYCCW(), 4).up(2), OWBBlocks.VAULT_FLOOR_TILING.getDefaultState());
        }
        emeraldBlocks.put(pos.offset(facing.rotateYCCW(), 2).up(3), RoomType.TUNNEL);
        emeraldBlocks.put(pos.offset(facing.rotateYCCW(), 4).up(3), RoomType.TUNNEL);
        diamondBlocks.put(pos.offset(facing.rotateYCCW(), 2).up(4), RoomType.TUNNEL);
        diamondBlocks.put(pos.offset(facing.rotateYCCW(), 4).up(4), RoomType.TUNNEL);
    }

    public boolean addAABB(AxisAlignedBB box) {
        for (AxisAlignedBB rooms : roomAABBs) {
            if (rooms.intersects(box)) {
                return false;
            }
        }
        roomAABBs.add(box);
        return true;
    }

    public static Rotation getRotationFromFacing(EnumFacing facing) {
        switch (facing) {
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

    public static boolean tryGenerateRoom(RoomType structure, World world, BlockPos pos, Random random, Rotation rotation, boolean buildDoor, boolean removeAir, StructureUtils.ProcessorType type, ResourceLocation loot, WorldGenVault vault) {
        return tryGenerateRoom(structure.structure, world, pos, random, rotation, buildDoor, removeAir, type, loot, vault, structure);
    }

    public static boolean tryGenerateRoom(ResourceLocation structure, World world, BlockPos pos, Random random, Rotation rotation, boolean buildDoor, boolean removeAir, StructureUtils.ProcessorType type, ResourceLocation loot, WorldGenVault vault, RoomType room) {
        ROOM_RANDOM = random;
        MinecraftServer server = world.getMinecraftServer();
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(rotation);
        if (removeAir) {
            settings.setReplacedBlock(Blocks.AIR);
        }
        Template template = templateManager.getTemplate(server, structure);
        if (pos.getDistance(vault.start.getX(), vault.start.getY(), vault.start.getZ()) > vault.size) {
            vault.size = (int) pos.getDistance(vault.start.getX(), vault.start.getY(), vault.start.getZ());
        }
        vault.currentRooms++;
        template.addBlocksToWorld(world, pos, type.get(pos, settings, loot, vault, room), settings, 2);
        return true;
    }

    public static boolean checkAndAddAABB(RoomType structure, World world, BlockPos pos, Rotation rotation, WorldGenVault vault) {
        MinecraftServer server = world.getMinecraftServer();
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        Template template = templateManager.getTemplate(server, structure.structure);
        int xMaxSize = template.getSize().getX();
        int zMaxSize = template.getSize().getZ();
        int xMinSize = 0;
        int zMinSize = 0;
        switch (rotation) {
            case NONE:
                break;
            case CLOCKWISE_90:
                xMaxSize = 0;
                xMinSize = -template.getSize().getZ();
                zMaxSize = template.getSize().getX();
                break;
            case COUNTERCLOCKWISE_90:
                xMinSize = 0;
                zMinSize = -template.getSize().getX();
                xMaxSize = template.getSize().getZ();
                zMaxSize = 0;
                break;
            case CLOCKWISE_180:
                xMinSize = -template.getSize().getX();
                zMinSize = -template.getSize().getZ();
                xMaxSize = 0;
                zMaxSize = 0;
                break;
        }
        AxisAlignedBB aabb = new AxisAlignedBB(xMinSize, 0, zMinSize, xMaxSize, template.getSize().getY(), zMaxSize).offset(pos);
        return vault.addAABB(aabb);
    }

    public void addDecorationSpace(RoomType type, BlockPos pos, Block blockType){
        if(blockType == Blocks.EMERALD_BLOCK){
            emeraldBlocks.put(pos, type);
        }
        if(blockType == Blocks.DIAMOND_BLOCK){
            diamondBlocks.put(pos, type);
        }
        if(blockType == Blocks.GOLD_BLOCK){
            goldBlocks.put(pos, type);
        }
    }

    private void decorateRooms(World worldIn, Random rand) {
        for(Map.Entry<BlockPos, RoomType> entry : emeraldBlocks.entrySet()){
            BlockPos pos = entry.getKey();
            if(worldIn.isAirBlock(pos)){
                if(rand.nextFloat() > 0.8F && (entry.getValue() == RoomType.ATRIUM || entry.getValue() == RoomType.RECREATIONAL || entry.getValue() == RoomType.CAFETERIA)){
                    VaultDecorations.generateDiningTable(worldIn, pos, rand);
                }
                if(rand.nextFloat() > 0.5F && entry.getValue() == RoomType.STORAGE){
                    VaultDecorations.generateShelf(worldIn, pos, rand);
                }
                if(rand.nextFloat() > 0.96F){
                    VaultDecorations.generateCrates(worldIn, pos, rand);
                }
                if(rand.nextFloat() > (entry.getValue() == RoomType.TUNNEL ? 0.7F : 0.99F)){
                    VaultDecorations.generateTrolley(worldIn, pos, rand);
                }
                if(rand.nextFloat() > 0.98F){
                    VaultDecorations.generateFloorMats(worldIn, pos, rand);
                }
            }
        }
        for(Map.Entry<BlockPos, RoomType> entry : goldBlocks.entrySet()){
            BlockPos pos = entry.getKey();

        }
        for(Map.Entry<BlockPos, RoomType> entry : diamondBlocks.entrySet()){
            BlockPos pos = entry.getKey();
            VaultDecorations.generateComputers(worldIn, pos, rand);
            if(rand.nextFloat() > 0.9F){
                VaultDecorations.generatePoster(worldIn, pos, rand);
            }
        }
    }

    enum RoomType {
        ENTERANCE(VAULT_ROOM_ENTRANCE, 0, true),
        TUNNEL(VAULT_ROOM_TUNNEL, 0, true),
        ATRIUM(VAULT_ROOM_ATRIUM, 80, true),
        RESIDENTIAL(VAULT_ROOM_RESIDENTIAL, 60, false),
        RECREATIONAL(VAULT_ROOM_RECREATIONAL, 40, false),
        CAFETERIA(VAULT_ROOM_CAFETERIA, 40, false),
        BUNKS(VAULT_ROOM_BUNKS, 50, false),
        STORAGE(VAULT_ROOM_STORAGE, 50, false),
        OFFICE(VAULT_ROOM_OFFICE, 40, false);

        private int weight;
        public ResourceLocation structure;
        private boolean single;
        private static final RoomType[] WITHOUT_ATRIUM = new RoomType[]{RESIDENTIAL, RECREATIONAL, CAFETERIA, BUNKS, STORAGE, OFFICE};
        RoomType(ResourceLocation structure, int weight, boolean single) {
            this.weight = weight;
            this.structure = structure;
            this.single = single;
        }

        public static RoomType getRandom(WorldGenVault vault) {
            if(vault.hasAtrium){
                return WITHOUT_ATRIUM[vault.random.nextInt(WITHOUT_ATRIUM.length - 1)];
            }else{
                vault.hasAtrium = true;
                return ATRIUM;
            }
        }

        public IBlockState getWallBlock() {
            if (this == RESIDENTIAL) {
                return Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE);
            }
            if (this == CAFETERIA) {
                return Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE);
            }
            return OWBBlocks.VAULT_WALL.getDefaultState();
        }

        public IBlockState getFlooringBlock() {
            if (this == RESIDENTIAL) {
                return Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
            }
            return OWBBlocks.VAULT_FLOOR_TILING.getDefaultState();
        }

        public IBlockState getCeilingBlock() {
            return OWBBlocks.VAULT_WALL.getDefaultState();
        }
    }
}
