package com.github.alexthe666.oldworldblues.world;

import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.gen.MapGenCaves;

public class MapGenWastelandCaves extends MapGenCaves {

    protected boolean canReplaceBlock(IBlockState state1, IBlockState state2) {
        Block block = state1.getBlock();
        return super.canReplaceBlock(state1, state2) || block == OWBBlocks.IRRADIATED_STONE || block == OWBBlocks.IRRADIATED_SOIL || block == OWBBlocks.ROCKY_IRRADIATED_SOIL;
    }
}
