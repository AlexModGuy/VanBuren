package com.github.alexthe666.oldworldblues.world.gen;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.block.MetalBlocks;
import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

import java.util.Random;

public class WorldGenVehicle extends WorldGenerator {
    public static final ResourceLocation CAR_0 = new ResourceLocation(OldWorldBlues.MODID, "car_0");
    public static final ResourceLocation CAR_1 = new ResourceLocation(OldWorldBlues.MODID, "car_1");
    public static final ResourceLocation CAR_2 = new ResourceLocation(OldWorldBlues.MODID, "car_2");
    public static final ResourceLocation CAR_3 = new ResourceLocation(OldWorldBlues.MODID, "car_3");
    public static final ResourceLocation TRUCK = new ResourceLocation(OldWorldBlues.MODID, "truck");
    public static final ResourceLocation TRUCK_LOAD = new ResourceLocation(OldWorldBlues.MODID, "truck_load");
    public static final ResourceLocation MOTORCYCLE = new ResourceLocation(OldWorldBlues.MODID, "motorcycle");
    public static final ResourceLocation BUS = new ResourceLocation(OldWorldBlues.MODID, "bus");

    public EnumFacing facing;
    public WorldGenVehicle(EnumFacing facing) {
        super(false);

        this.facing = facing;

    }

    public boolean generate(World worldIn, Random rand, BlockPos position) {
        ResourceLocation model = null;
        boolean paint = true;
        int chance = rand.nextInt(99) + 1;
        if(chance < 80){
            if(chance > 20){
                model = CAR_0;
            }else if(chance > 40){
                model = CAR_1;
            }else if(chance > 60){
                model = CAR_2;
            }else{
                model = CAR_3;
            }
        }else{
            if(chance < 88){
                model = BUS;
                paint = false;
            }else if(chance < 95){
                if(rand.nextBoolean()){
                    model = TRUCK;
                    paint = false;
                }else{
                    model = TRUCK_LOAD;
                    paint = false;
                }
            }else{
                model = MOTORCYCLE;
            }
        }
        position = position.add(rand.nextInt(8) - 4, 1, rand.nextInt(8) - 4);
        MinecraftServer server = worldIn.getMinecraftServer();
        TemplateManager templateManager = worldIn.getSaveHandler().getStructureTemplateManager();
        Template template = templateManager.getTemplate(server, model);
        PlacementSettings settings = new PlacementSettings().setRotation(getRotationFromFacing(facing));
        BlockPos pos = worldIn.getHeight(position).offset(facing, template.getSize().getZ() / 2).offset(facing.rotateYCCW(), template.getSize().getX() / 2);
        settings.setReplacedBlock(Blocks.AIR);
        if(checkIfCanGenAt(worldIn, pos, template.getSize().getX(), template.getSize().getZ(), facing)){
            //worldIn.setBlockState(worldIn.getHeight(position).up(5), Blocks.GLOWSTONE.getDefaultState());
            template.addBlocksToWorld(worldIn, pos, new OWBBlockProcessorCar(pos, settings, rand, paint, rand.nextBoolean()), settings, 2);
        }
         return true;
    }

    public boolean checkIfCanGenAt(World world, BlockPos middle, int x, int z, EnumFacing facing){
        return !isPartOfACar(world.getBlockState(middle.offset(facing, z / 2))) && !isPartOfACar(world.getBlockState(middle.offset(facing.getOpposite(), z / 2))) &&
                !isPartOfACar(world.getBlockState(middle.offset(facing.rotateY(), x / 2))) && !isPartOfACar(world.getBlockState(middle.offset(facing.rotateYCCW(), x / 2)));
    }

    private boolean isPartOfACar(IBlockState state){
        return state.getBlock() instanceof MetalBlocks.BlockMetal || state.getBlock() instanceof MetalBlocks.BlockMetalSheet || state.getBlock() instanceof MetalBlocks.SlabDouble || state.getBlock() instanceof MetalBlocks.SlabHalf
                || state.getBlock() == OWBBlocks.CAR_GRILL || state.getBlock() == OWBBlocks.HEADLIGHT || state.getBlock() == OWBBlocks.HEADLIGHT_BROKEN;
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
}