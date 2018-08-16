package com.github.alexthe666.oldworldblues.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;

public interface ISittable {

    void startRiding(EntityLivingBase rider, IBlockState blockState, BlockPos pos);
}
