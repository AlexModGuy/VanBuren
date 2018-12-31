package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class BlockGenericSlab extends BlockSlab {

	private final Block baseBlock;

	public BlockGenericSlab(String name, float hardness, float resistance, SoundType soundType, Block baseBlock) {
		super(Material.ROCK);
		IBlockState iblockstate = this.blockState.getBaseState();
		this.baseBlock = baseBlock;
		this.setLightOpacity(0);
		this.useNeighborBrightness = true;
		setHardness(hardness);
		setResistance(resistance);
		setSoundType(soundType);
		if (this.isDouble()) {
			setUnlocalizedName("oldworldblues." + name + "_double");
			this.setRegistryName(OldWorldBlues.MODID, name + "_double");
		} else {
			iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
			setUnlocalizedName("oldworldblues." + name);
			this.setRegistryName(OldWorldBlues.MODID, name);
			setCreativeTab(OldWorldBlues.TAB);
		}
	}

	@SideOnly(Side.CLIENT)
	protected static boolean isHalfSlab(IBlockState state) {
		return state.getBlock() instanceof BlockGenericSlab && !((BlockGenericSlab) state.getBlock()).isDouble();
	}

	public abstract ItemBlock getItemBlock();

	@Override
    @Nullable
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(baseBlock);
	}

	@Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(baseBlock);
	}

	@SuppressWarnings("deprecation")
	@Override
    public IBlockState getStateFromMeta(int meta) {
		IBlockState iblockstate = this.getDefaultState();
		if (!this.isDouble()) {
			return iblockstate.withProperty(HALF, meta == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
		} else {
			return iblockstate;
		}
	}

	@Override
    public int getMetaFromState(IBlockState state) {
		int i = 0;

		if (!this.isDouble() && state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP) {
			i = 1;
		}

		return i;

	}

	@Override
    protected BlockStateContainer createBlockState() {
		return this.isDouble() ? super.createBlockState() : new BlockStateContainer(this, HALF);
	}

	@Override
	public String getUnlocalizedName(int meta) {
		return super.getUnlocalizedName();
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return null;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return Variant.DEFAULT;
	}

	public enum Variant implements IStringSerializable {
		DEFAULT;

		@Override
        public String getName() {
			return "default";
		}
	}

	public abstract static class Double extends BlockGenericSlab {
		public Double(String name, float hardness, float resistance, SoundType soundType, Block baseBlock) {
			super(name, hardness, resistance, soundType, baseBlock);
		}

		@Override
        public boolean isDouble() {
			return true;
		}
	}

	public abstract static class Half extends BlockGenericSlab {
		public Half(String name, float hardness, float resistance, SoundType soundType, Block baseBlock) {
			super(name, hardness, resistance, soundType, baseBlock);
		}

		@Override
        public boolean isDouble() {
			return false;
		}

	}
}
