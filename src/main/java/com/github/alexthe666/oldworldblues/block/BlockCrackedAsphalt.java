package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCrackedAsphalt extends Block implements IRoad {
    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 4);
    public BlockCrackedAsphalt() {
        super(Material.ROCK);
        this.setHardness(2.5F);
        this.setSoundType(SoundType.STONE);
        this.setResistance(15F);
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setUnlocalizedName("oldworldblues.asphalt_cracked");
        this.setRegistryName(OldWorldBlues.MODID, "asphalt_cracked");
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, 0));
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, Integer.valueOf(meta));
    }

    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(VARIANT)).intValue();
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        worldIn.setBlockState(pos, OWBBlocks.CRACKED_ASPHALT.getDefaultState().withProperty(VARIANT, worldIn.rand.nextInt(4)));
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{VARIANT});
    }

}
