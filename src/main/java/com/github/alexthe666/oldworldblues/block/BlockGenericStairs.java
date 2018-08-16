package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockGenericStairs extends BlockStairs {

	public BlockGenericStairs(IBlockState modelState, String name) {
		super(modelState);
		this.setLightOpacity(0);
		this.setCreativeTab(OldWorldBlues.TAB);
		this.setUnlocalizedName("oldworldblues." + name);
		this.setRegistryName(OldWorldBlues.MODID, name);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

}
