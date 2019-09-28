package com.github.alexthe666.oldworldblues.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public class BlockIrradiatedSoil extends BlockGeneric {
    public static final PropertyBool GROWTH = PropertyBool.create("growth");

    public BlockIrradiatedSoil() {
        super("irradiated_soil", 0.5F, 0F, Material.GROUND, SoundType.GROUND);
        this.setDefaultState(this.blockState.getBaseState().withProperty(GROWTH, Boolean.valueOf(true)));
    }
    public IBlockState getStateFromMeta(int meta){
        return this.getDefaultState().withProperty(GROWTH, Boolean.valueOf((meta & 1) == 0));
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((Boolean)state.getValue(GROWTH)).booleanValue() ? 0 : 1;
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {GROWTH});
    }


}
