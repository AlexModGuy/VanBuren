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

public class OWBBlockProcessorVaultDeco extends OWBBlockProcessorLoot {
    private WorldGenVault vaultGen;

    public OWBBlockProcessorVaultDeco(BlockPos pos, PlacementSettings settings, ResourceLocation loot, WorldGenVault vaultGen) {
        super(pos, settings, loot);
        this.vaultGen = vaultGen;
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
            EnumFacing facing = blockInfoIn.blockState.withRotation(this.rotation).getValue(BlockHorizontal.FACING);
            IBlockState doorState = OWBBlocks.INTERIOR_VAULT_DOOR.getDefaultState().withProperty(BlockInteriorVaultDoor.FACING, facing.getOpposite()); //OWBBlocks.INTERIOR_VAULT_DOOR.getDefaultState().withProperty(BlockInteriorVaultDoor.FACING, facing.rotateYCCW());
            worldIn.setBlockState(pos.offset(facing.rotateY(), -1).down(), doorState);
            BlockInteriorVaultDoor.onStructureGen(worldIn, pos.offset(facing.rotateY(), -1).down(), doorState);
            return null;
        }
        return super.processBlock(worldIn, pos, blockInfoIn);
    }
}