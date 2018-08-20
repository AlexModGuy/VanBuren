package com.github.alexthe666.oldworldblues.structure;

import com.github.alexthe666.oldworldblues.block.BlockInteriorVaultDoor;
import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

import javax.annotation.Nullable;

public class OWBBlockProcessorVault extends OWBBlockProcessorLoot {
    private WorldGenVault vaultGen;
    private WorldGenVault.RoomType roomType;
    public OWBBlockProcessorVault(BlockPos pos, PlacementSettings settings, ResourceLocation loot, WorldGenVault vaultGen, WorldGenVault.RoomType roomType) {
        super(pos, settings, loot);
        this.vaultGen = vaultGen;
        this.roomType = roomType;
    }

    @Nullable
    public Template.BlockInfo processBlock(World worldIn, BlockPos pos, Template.BlockInfo blockInfoIn) {
        IBlockState currentState = worldIn.getBlockState(pos);
        if(currentState.getBlock() == OWBBlocks.INTERIOR_VAULT_DOOR || currentState.getBlock() == OWBBlocks.INTERIOR_VAULT_DOOR_FRAME || currentState.getBlock() == Blocks.DIAMOND_BLOCK){
            return null;
        }
        if(blockInfoIn.blockState.getBlock() == Blocks.EMERALD_BLOCK){
            return new Template.BlockInfo(pos, Blocks.AIR.getDefaultState(), null);
        }
        if(blockInfoIn.blockState.getBlock() == Blocks.PUMPKIN){
            if(worldIn.rand.nextInt(100) < vaultGen.roomChance) {
                EnumFacing facing = blockInfoIn.blockState.withRotation(this.rotation).getValue(BlockHorizontal.FACING);
                BlockPos tunnel_corner = pos.offset(facing.getOpposite(), 1).down(3).offset(facing.rotateY(), 3);
                switch(this.rotation){
                    case NONE://Green
                        break;
                    case CLOCKWISE_90://Red
                        break;
                    case COUNTERCLOCKWISE_90://Yellow
                        tunnel_corner = tunnel_corner.offset(facing.rotateY(), -6);
                        break;
                    case CLOCKWISE_180://Blue
                        tunnel_corner = tunnel_corner.offset(facing.rotateY(), -6);
                        break;
               }
                boolean canGenerateRoom = vaultGen.canGenerateRoom(worldIn, tunnel_corner, worldIn.rand, facing.getOpposite());
                if(canGenerateRoom) {
                    IBlockState doorState = OWBBlocks.INTERIOR_VAULT_DOOR.getDefaultState().withProperty(BlockInteriorVaultDoor.FACING, facing.getOpposite()); //OWBBlocks.INTERIOR_VAULT_DOOR.getDefaultState().withProperty(BlockInteriorVaultDoor.FACING, facing.rotateYCCW());
                    BlockPos door_corner = pos.offset(facing.rotateY(), -1).down();
                    worldIn.setBlockState(door_corner, doorState);
                    BlockInteriorVaultDoor.onStructureGen(worldIn, door_corner, doorState);

                    vaultGen.generateDoor(worldIn, tunnel_corner, worldIn.rand, facing.getOpposite());
                }else{
                    return new Template.BlockInfo(pos, roomType.wallState, null);
                }
            }else{
                return new Template.BlockInfo(pos, OWBBlocks.VAULT_WALL.getDefaultState(), null);
            }
            return null;
        }
        return super.processBlock(worldIn, pos, blockInfoIn);
    }
}