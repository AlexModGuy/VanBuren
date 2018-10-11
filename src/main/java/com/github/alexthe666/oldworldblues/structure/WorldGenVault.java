package com.github.alexthe666.oldworldblues.structure;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.block.BlockInteriorVaultDoor;
import com.github.alexthe666.oldworldblues.block.BlockInteriorVaultDoorFrame;
import com.github.alexthe666.oldworldblues.block.BlockVaultBeam;
import com.github.alexthe666.oldworldblues.block.BlockVaultLighting;
import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockGlazedTerracotta;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockRotatedPillar;
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
import net.minecraft.util.math.Vec3d;
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
    public int roomChance = 200;
    public int tunnelLength;
    public int doorCount = 0;
    public RoomType nextRoom;
    public AxisAlignedBB nextRoomAABB;
    private EnumFacing nextRoomFacing;
    public List<RoomType> possibleRooms;
    public List<AxisAlignedBB> roomAABBs;
    public int size;
    public BlockPos start;

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
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
        nextRoomFacing = EnumFacing.NORTH;
        nextRoomAABB = null;
        tryGenerateRoom(VAULT_ROOM_ENTRANCE, worldIn, position, rand, getRotationFromFacing(EnumFacing.getHorizontal(rand.nextInt(3))), false, false, StructureUtils.ProcessorType.VAULT, LootTableList.EMPTY, this);
        return true;
    }

    public boolean canGenerateRoom(World world, BlockPos doorPos, Random random, EnumFacing facing) {
        nextRoom = RoomType.getRandom(possibleRooms);
        nextRoomFacing = EnumFacing.getHorizontal(random.nextInt(3));
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        Template template = templateManager.getTemplate(world.getMinecraftServer(), nextRoom.structure);
        int largestSize = Math.max(template.getSize().getX(), template.getSize().getZ());
        tunnelLength = (largestSize / 4) + 2;
        int actualLength = tunnelLength * 4;
        BlockPos pathPos = doorPos.offset(facing, actualLength);
        AxisAlignedBB boundingBox = rotateAABB(nextRoom.getAABB(world), nextRoomFacing).offset(pathPos);
        AxisAlignedBB tunnelBox = rotateAABB(new AxisAlignedBB(0, 0, -1, 7, 7, actualLength - 1), facing).offset(doorPos);
        for(AxisAlignedBB box : roomAABBs){
            if (box.intersects(boundingBox)) {
                return false;
            }
        }
        nextRoomAABB = boundingBox;
        return checkIfRoomCanGenerate(nextRoom, world, pathPos.offset(facing.getOpposite().rotateY(), 3).up(), random, getRotationFromFacing(nextRoomFacing), false, false, StructureUtils.ProcessorType.VAULT, LootTableList.EMPTY, this);
    }

    public void generateDoor(World world, BlockPos doorPos, Random random, EnumFacing facing){
        doorCount++;
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        Template template = templateManager.getTemplate(world.getMinecraftServer(), nextRoom.structure);
        int largestSize = Math.max(template.getSize().getX(), template.getSize().getZ());
        doorPositions.add(doorPos);
        roomChance = Math.round(roomChance * 0.75F);
        BlockPos pathPos = doorPos.offset(facing.getOpposite().rotateY(), -3).down(4);
        int segments = 0;
        world.setBlockState(doorPos.up(7), Blocks.COBBLESTONE.getDefaultState(), 2);
        world.setBlockState(doorPos.up(8), Blocks.PUMPKIN.getDefaultState().withProperty(BlockHorizontal.FACING, facing), 2);

        while(segments < tunnelLength && generateTunnel(world, pathPos, facing)) {
            segments++;
            pathPos = pathPos.offset(facing, 4);
        }
        BlockPos basePosition = pathPos.offset(facing.getOpposite().rotateY(), 4).up(3);
        IBlockState doorState = OWBBlocks.INTERIOR_VAULT_DOOR.getDefaultState().withProperty(BlockInteriorVaultDoor.FACING, facing.getOpposite());
        tryGenerateRoom(nextRoom, world, pathPos.up(), random, getRotationFromFacing(nextRoomFacing), false, false, StructureUtils.ProcessorType.VAULT, LootTableList.EMPTY, this);

        switch(getRotationFromFacing(nextRoomFacing)){
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
        world.setBlockState(basePosition.offset(facing), Blocks.COBBLESTONE.getDefaultState(), 2);
        world.setBlockState(basePosition.offset(facing).up(), Blocks.PUMPKIN.getDefaultState().withProperty(BlockHorizontal.FACING, facing), 2);
    }

    private boolean generateTunnel(World world, BlockPos pos, EnumFacing facing){
        if(!this.roomAABBs.isEmpty()){
            for(AxisAlignedBB box : this.roomAABBs){
                if(box.contains(new Vec3d(pos))){
                    return false;
                }
            }
        }
        tunnelBase(world, pos, facing, false);
        tunnelBase(world, pos.offset(facing), facing, true);
        tunnelBase(world, pos.offset(facing, 2), facing, false);
        tunnelSupport(world, pos.offset(facing, 3), facing);
        return true;
    }

    private void tunnelSupport(World world, BlockPos pos, EnumFacing facing){
        pos = pos.offset(facing);
        for(int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {
                BlockPos frame = pos.offset(facing.rotateYCCW(), x).up(y + 1);
                world.setBlockState(frame, OWBBlocks.BLAST_CONCRETE.getDefaultState());
            }
        }
        for(int x = 2; x < 5; x++) {
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

        world.setBlockState(pos.offset(facing.rotateYCCW(), 2).up(2), OWBBlocks.VAULT_FLOOR_TILING.getDefaultState());
        world.setBlockState(pos.offset(facing.rotateYCCW(), 3).up(2), OWBBlocks.VAULT_FLOOR_TILING.getDefaultState());
        world.setBlockState(pos.offset(facing.rotateYCCW(), 4).up(2), OWBBlocks.VAULT_FLOOR_TILING.getDefaultState());
    }

    private void tunnelBase(World world, BlockPos pos, EnumFacing facing, boolean light){
        pos = pos.offset(facing);
        for(int x = 0; x < 7; x++) {
            for (int y = 0; y < 7; y++) {
                BlockPos frame = pos.offset(facing.rotateYCCW(), x).up(y + 1);
                world.setBlockState(frame, OWBBlocks.BLAST_CONCRETE.getDefaultState());
            }
        }
        for(int x = 2; x < 5; x++) {
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
        if(light) {
            world.setBlockState(pos.offset(facing.rotateYCCW(), 3).up(5), OWBBlocks.VAULT_LIGHTING.getDefaultState().withProperty(BlockHorizontal.FACING, facing).withProperty(BlockVaultLighting.HALF, BlockVaultLighting.Half.TOP));
        }
        world.setBlockState(pos.offset(facing.rotateYCCW(), 2).up(2), OWBBlocks.VAULT_FLOOR_TILING.getDefaultState());
        world.setBlockState(pos.offset(facing.rotateYCCW(), 3).up(2), OWBBlocks.VAULT_FLOOR_TILING.getDefaultState());
        world.setBlockState(pos.offset(facing.rotateYCCW(), 4).up(2), OWBBlocks.VAULT_FLOOR_TILING.getDefaultState());
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
        MinecraftServer server = world.getMinecraftServer();
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(rotation);
        Template template = templateManager.getTemplate(server, structure.structure);
        int x = template.getSize().getX();
        int z = template.getSize().getZ();
        BlockPos genPos = pos.subtract(new BlockPos(x, 0, z).rotate(rotation)).add(new BlockPos(x, 0, z));
        template.addBlocksToWorld(world, genPos, type.get(pos, settings, loot, vault, structure), settings, 2);
        world.setBlockState(genPos, Blocks.OBSIDIAN.getDefaultState());
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

    public static boolean checkIfRoomCanGenerate(RoomType structure, World world, BlockPos pos, Random random, Rotation rotation, boolean buildDoor, boolean removeAir, StructureUtils.ProcessorType type, ResourceLocation loot, WorldGenVault vault) {
        MinecraftServer server = world.getMinecraftServer();
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        Template template = templateManager.getTemplate(server, structure.structure);
        EnumFacing facing = rotation.rotate(EnumFacing.NORTH);
        int xSize = template.getSize().getX();
        int zSize = template.getSize().getZ();
        BlockPos center = pos;
        switch(rotation){
            case NONE:
                center = pos;
                break;
            case CLOCKWISE_90:
                center = pos.add(template.getSize().getZ() - 1, 0, 0);
                xSize = template.getSize().getZ();
                zSize = template.getSize().getX();
                break;
            case COUNTERCLOCKWISE_90:
                center = pos.add(0, 0, template.getSize().getX() - 1);
                xSize = template.getSize().getZ();
                zSize = template.getSize().getX();
                break;
            case CLOCKWISE_180:
                center = pos.add(template.getSize().getX() - 1, 0, template.getSize().getZ() - 1);
                break;
        }

        if(vault != null) {
            AxisAlignedBB box = new AxisAlignedBB(0, 0, 0, xSize, template.getSize().getY(), zSize);
            if (!vault.addAABB(center, box, facing)) {
                return false;
            }
        }
        if(center.getDistance(vault.start.getX(), vault.start.getY(), vault.start.getZ()) > vault.size){
            vault.size = (int)center.getDistance(vault.start.getX(), vault.start.getY(), vault.start.getZ());
        }
        return true;
    }

    enum RoomType{
        ATRIUM(VAULT_ROOM_ATRIUM, 80, true),
        RESIDENTIAL(VAULT_ROOM_RESIDENTIAL, 60, false, Blocks.CONCRETE.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE)),
        RECREATIONAL(VAULT_ROOM_RECREATIONAL, 40, false),
        CAFETERIA(VAULT_ROOM_CAFETERIA, 40, true, Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE)),
        BUNKS(VAULT_ROOM_BUNKS, 50, false),
        OFFICE(VAULT_ROOM_OFFICE, 40, false),
        STORAGE(VAULT_ROOM_STORAGE, 40, false);

        private int weight;
        public ResourceLocation structure;
        private boolean single;
        public IBlockState wallState;
        RoomType(ResourceLocation structure, int weight, boolean single){
            this.weight = weight;
            this.structure = structure;
            this.single = single;
            this.wallState = OWBBlocks.VAULT_WALL.getDefaultState();
        }

        RoomType(ResourceLocation structure, int weight, boolean single, IBlockState wall){
            this.weight = weight;
            this.structure = structure;
            this.single = single;
            this.wallState = wall;
        }

        public int getXSize(World world){
            TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
            Template template = templateManager.getTemplate(world.getMinecraftServer(), this.structure);
            return template.getSize().getX();
        }

        public int getZSize(World world){
            TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
            Template template = templateManager.getTemplate(world.getMinecraftServer(), this.structure);
            return template.getSize().getZ();
        }

        public int getYSize(World world){
            TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
            Template template = templateManager.getTemplate(world.getMinecraftServer(), this.structure);
            return template.getSize().getY();
        }

        public int getLargestSize(World world){
            TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
            Template template = templateManager.getTemplate(world.getMinecraftServer(), this.structure);
            return Math.max(template.getSize().getZ(), template.getSize().getX());
        }

        public AxisAlignedBB getAABB(World world){
            return new AxisAlignedBB(-1, -1, -1, getXSize(world) + 1, getYSize(world) + 1, getZSize(world) + 1);
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
