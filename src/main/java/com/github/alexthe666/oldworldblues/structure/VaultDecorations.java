package com.github.alexthe666.oldworldblues.structure;

import com.github.alexthe666.oldworldblues.block.*;
import com.github.alexthe666.oldworldblues.entity.EntityVaultTecPoster;
import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VaultDecorations {

    public static void generateTrolley(World world, BlockPos pos, Random random) {
        EnumFacing facing = EnumFacing.byHorizontalIndex(random.nextInt(3));
        if (world.isAirBlock(pos)) {
            world.setBlockState(pos, OWBBlocks.SHELF_TROLLEY.getDefaultState().withProperty(BlockGenericDecoration.FACING, facing));
            world.setBlockState(pos.up(), OWBBlocks.TOOLBOX.getDefaultState().withProperty(BlockToolbox.FACING, facing));
        }
    }

    public static void generateCrates(World world, BlockPos pos, Random random){
        EnumFacing facing = EnumFacing.byHorizontalIndex(random.nextInt(3));
        Block crate;
        switch (random.nextInt(3)) {
            default:
                crate = OWBBlocks.VAULT_CRATE_BLUE;
                break;
            case 1:
                crate = OWBBlocks.VAULT_CRATE_BROWN;
                break;
            case 2:
                crate = OWBBlocks.VAULT_CRATE_RED;
                break;
            case 3:
                crate = OWBBlocks.VAULT_CRATE_WHITE;
                break;
        }
        IBlockState state = crate.getDefaultState().withProperty(BlockVaultCrate.FACING, facing);
        int size = 1 + random.nextInt(2);
        if(random.nextBoolean()){
            for(int i = -size/2; i < size/2; i++){
                for(int j = 0; j < size; j++){
                    for(int k = -size/2; k < size/2; k++){
                        if(world.isAirBlock(pos.add(i, j, k))){
                            world.setBlockState(pos.add(i, j, k), state);
                        }
                    }
                }
            }
        }else{
            size -= 1;
            for (float i = 0; i < size; i += 0.5) {
                for (float j = 0; j < 2 * Math.PI * i; j += 0.5) {
                    int x = (int) Math.floor(Math.sin(j) * i);
                    int z = (int) Math.floor(Math.cos(j) * i);
                    int y = size - (int)i - 1;
                    if(world.isAirBlock(pos.add(x, y, z)) && (world.getBlockState(pos.add(x, y - 1, z)).getBlock() instanceof BlockVaultCrate || world.getBlockState(pos.add(x, y - 1, z)).isOpaqueCube())){
                        world.setBlockState(pos.add(x, y, z), state);
                    }
                }
            }
        }
    }

    public static void generateFloorMats(World world, BlockPos pos, Random random){
        IBlockState state = OWBBlocks.VAULT_FLOOR_MAT.getDefaultState();
        int size = 1 + random.nextInt(2);
        for (float i = 0; i < size; i += 0.5) {
            for (float j = 0; j < 2 * Math.PI * i; j += 0.5) {
                int x = (int) Math.floor(Math.sin(j) * i);
                int z = (int) Math.floor(Math.cos(j) * i);
                if(world.isAirBlock(pos.add(x, 0, z)) && !world.isAirBlock(pos.add(x, -1, z))){
                    world.setBlockState(pos.add(x, 0, z), state);
                }
            }
        }
    }

    public static void generateShelf(World world, BlockPos pos, Random random) {
        int length = 4 + random.nextInt(1);
        int height = 1 + random.nextInt(3);
        EnumFacing facing = EnumFacing.byHorizontalIndex(random.nextInt(3));
        for(int i = 0; i < length; i++){
            for(int k = -1; k < 1; k++) {
                for (int j = 0; j < height; j++) {
                    if (!world.isAirBlock(pos.offset(facing, i).offset(facing.rotateY(), k).up(j))) {
                        return;
                    }
                }
            }
        }
        for(int i = 0; i < length; i++){
            for(int j = 0; j < height; j++){
                IBlockState railState = OWBBlocks.VAULT_RAILING.getDefaultState().withProperty(BlockVaultRailing.FACING, i == 0 ? facing : facing.getOpposite());
                IBlockState state = i == 0 || i == length - 1 ? railState : OWBBlocks.METAL_TABLE_TOP.getDefaultState();
                BlockPos shelfPos = pos.offset(facing, i).up(j);
                world.setBlockState(shelfPos, state);
                if(j == height - 1 && i > 0 && i < length - 1){
                    IBlockState boxState = null;
                    EnumFacing boxFacing = facing.rotateY();
                    if(random.nextBoolean()){
                        boxState = OWBBlocks.CARDBOARD_BOX.getDefaultState().withProperty(BlockCardboardBox.FACING, boxFacing).withProperty(BlockCardboardBox.OPEN, random.nextBoolean());
                    }else {
                        Block crate;
                        switch (random.nextInt(3)) {
                            default:
                                crate = OWBBlocks.VAULT_CRATE_BLUE;
                                break;
                            case 1:
                                crate = OWBBlocks.VAULT_CRATE_BROWN;
                                break;
                            case 2:
                                crate = OWBBlocks.VAULT_CRATE_RED;
                                break;
                            case 3:
                                crate = OWBBlocks.VAULT_CRATE_WHITE;
                                break;
                        }
                        boxState = crate.getDefaultState().withProperty(BlockVaultCrate.FACING, boxFacing);
                    }
                    world.setBlockState(shelfPos.up(), boxState);

                }
            }
        }
    }

    public static void generateDiningTable(World world, BlockPos pos, Random random){
        int width = 2 + random.nextInt(1);
        int length = width + 2 + random.nextInt(2);
        EnumFacing facing = EnumFacing.byHorizontalIndex(random.nextInt(3));
        for(int i = 0; i < length; i++){
            for(int j = -1; j < width + 1; j++){
                if(!world.isAirBlock(pos.offset(facing, i).offset(facing.rotateY(), j))){
                    return;
                }
            }
        }
        for(int i = 0; i < length; i++) {
            for (int j = -1; j < width + 1; j++) {
                IBlockState seatState = OWBBlocks.METAL_TABLE_SEAT.getDefaultState().withProperty(BlockMetalTableSeat.FACING, j == -1 ? facing.rotateYCCW() : facing.rotateY());
                IBlockState state = j == -1 || j == width ? seatState : OWBBlocks.METAL_TABLE_TOP.getDefaultState();
                BlockPos pos1 = pos.offset(facing, i).offset(facing.rotateY(), j);
                world.setBlockState(pos1, state);
                if(state.getBlock() == OWBBlocks.METAL_TABLE_TOP){
                    if(random.nextFloat()  > 0.5F){
                        EnumFacing cupFacing = EnumFacing.byHorizontalIndex(random.nextInt(3));
                        IBlockState cupState = null;
                        switch(random.nextInt(2)){
                            default:
                                cupState = OWBBlocks.COFFEE_CUP.getDefaultState().withProperty(BlockGenericDecoration.FACING, cupFacing);
                                break;
                            case 1:
                                cupState = OWBBlocks.EMPTY_BOTTLE.getDefaultState().withProperty(BlockGenericDecoration.FACING, cupFacing);
                                break;
                            case 2:
                                cupState = OWBBlocks.EMPTY_NUKA_COLA_BOTTLE.getDefaultState().withProperty(BlockGenericDecoration.FACING, cupFacing);
                                break;
                        }
                        if(random.nextInt(5) == 0){
                            if(random.nextBoolean()){
                                cupState = OWBBlocks.CARDBOARD_BOX.getDefaultState().withProperty(BlockCardboardBox.FACING, cupFacing).withProperty(BlockCardboardBox.OPEN, random.nextBoolean());
                            }else {
                                Block crate;
                                switch (random.nextInt(3)) {
                                    default:
                                        crate = OWBBlocks.VAULT_CRATE_BLUE;
                                        break;
                                    case 1:
                                        crate = OWBBlocks.VAULT_CRATE_BROWN;
                                        break;
                                    case 2:
                                        crate = OWBBlocks.VAULT_CRATE_RED;
                                        break;
                                    case 3:
                                        crate = OWBBlocks.VAULT_CRATE_WHITE;
                                        break;
                                }
                                cupState = crate.getDefaultState().withProperty(BlockVaultCrate.FACING, cupFacing);
                            }
                        }
                        world.setBlockState(pos1.up(), cupState);
                    }
                }
            }
        }
    }

    public static void generatePoster(World world, BlockPos pos, Random random){
        List<EnumFacing> possibleDirections = new ArrayList<>();
        for(EnumFacing facing : EnumFacing.HORIZONTALS){
            if(world.getBlockState(pos.offset(facing)).isOpaqueCube()){
                possibleDirections.add(facing.getOpposite());
            }
        }
        if(!possibleDirections.isEmpty()) {
            EnumFacing facing = possibleDirections.get(possibleDirections.size() > 1 ? random.nextInt(possibleDirections.size() - 1) : 0);
            EntityVaultTecPoster poster = new EntityVaultTecPoster(world, pos, facing);
            if (poster.onValidSurface()) {
                if (!world.isRemote) {
                    world.spawnEntity(poster);
                }
            }
        }
    }

    public static void generateComputers(World world, BlockPos pos, Random random){
        if(world.getBlockState(pos.down()).getBlock() instanceof BlockDeskTop || world.getBlockState(pos.down()).getBlock() instanceof BlockDeskDrawer) {
            List<EnumFacing> possibleDirections = new ArrayList<>();
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                if (world.getBlockState(pos.offset(facing)).isOpaqueCube()) {
                    possibleDirections.add(facing.getOpposite());
                }
            }
            EnumFacing facing;
            if(possibleDirections.isEmpty()){
                facing = EnumFacing.byHorizontalIndex(random.nextInt(3));
            }else{
                facing = possibleDirections.get(possibleDirections.size() > 1 ? random.nextInt(possibleDirections.size() - 1) : 0);

            }
            IBlockState computerState = null;
            if (random.nextFloat() > 0.7F) {
                computerState = OWBBlocks.COMPUTER.getDefaultState().withProperty(BlockGenericDecoration.FACING, facing);
            } else {
                switch (random.nextInt(4)) {
                    default:
                        computerState = OWBBlocks.COFFEE_CUP.getDefaultState().withProperty(BlockGenericDecoration.FACING, facing);
                        break;
                    case 1:
                        computerState = OWBBlocks.EMPTY_BOTTLE.getDefaultState().withProperty(BlockGenericDecoration.FACING, facing);
                        break;
                    case 2:
                        computerState = OWBBlocks.EMPTY_NUKA_COLA_BOTTLE.getDefaultState().withProperty(BlockGenericDecoration.FACING, facing);
                        break;
                    case 3:
                        computerState = OWBBlocks.CLIPBOARD.getDefaultState().withProperty(BlockGenericDecoration.FACING, facing);
                        break;
                    case 4:
                        computerState = OWBBlocks.DESK_FAN.getDefaultState().withProperty(BlockGenericDecoration.FACING, facing);
                        break;
                }
            }
            world.setBlockState(pos, computerState);
        }
    }
}
