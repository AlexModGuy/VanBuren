package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
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

	class ItemBlockGenericSlab extends ItemBlock {
		private final BlockSlab singleSlab;
		private final BlockSlab doubleSlab;

		public ItemBlockGenericSlab(Block block, BlockSlab singleSlab, BlockSlab doubleSlab) {
			super(block);
			this.singleSlab = singleSlab;
			this.doubleSlab = doubleSlab;
			this.setMaxDamage(0);
			this.setHasSubtypes(true);
		}

		@Override
        public int getMetadata(int damage) {
			return damage;
		}

		@Override
        public String getUnlocalizedName(ItemStack stack) {
			return this.singleSlab.getUnlocalizedName(stack.getMetadata());
		}

		@Override
        public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
			ItemStack stack = playerIn.getHeldItem(hand);
			if(stack.getItem() == Item.getItemFromBlock(doubleSlab)){
				return EnumActionResult.SUCCESS;
			}
			if (stack.getCount() != 0 && playerIn.canPlayerEdit(pos.offset(facing), facing, stack)) {
				Comparable<?> comparable = this.singleSlab.getTypeForItem(stack);
				IBlockState iblockstate = worldIn.getBlockState(pos);
				if (iblockstate.getBlock() == this.singleSlab) {
					BlockSlab.EnumBlockHalf blockslab$enumblockhalf = iblockstate.getValue(BlockSlab.HALF);
					if ((facing == EnumFacing.UP && blockslab$enumblockhalf == BlockSlab.EnumBlockHalf.BOTTOM || facing == EnumFacing.DOWN && blockslab$enumblockhalf == BlockSlab.EnumBlockHalf.TOP)) {
						IBlockState iblockstate1 = this.doubleSlab.getDefaultState();
						AxisAlignedBB axisalignedbb = iblockstate1.getCollisionBoundingBox(worldIn, pos);
						if (axisalignedbb != Block.NULL_AABB && worldIn.checkNoEntityCollision(axisalignedbb.offset(pos)) && worldIn.setBlockState(pos, iblockstate1, 11)) {
							SoundType soundtype = this.doubleSlab.getSoundType(iblockstate1, worldIn, pos, playerIn);
							worldIn.playSound(playerIn, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
							stack.shrink(1);
						}
						return EnumActionResult.SUCCESS;
					}
				}
				return this.tryPlace(playerIn, stack, worldIn, pos.offset(facing), comparable) ? EnumActionResult.SUCCESS : super.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
			} else {
				return EnumActionResult.FAIL;
			}
		}

		@Override
        @SideOnly(Side.CLIENT)
		public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
			BlockPos blockpos = pos;
			IBlockState iblockstate = worldIn.getBlockState(pos);

			if (iblockstate.getBlock() == this.singleSlab) {
				boolean flag = iblockstate.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;
				if ((side == EnumFacing.UP && !flag || side == EnumFacing.DOWN && flag)) {
					return true;
				}
			}
			pos = pos.offset(side);
			IBlockState iblockstate1 = worldIn.getBlockState(pos);
			return iblockstate1.getBlock() == this.singleSlab || super.canPlaceBlockOnSide(worldIn, blockpos, side, player, stack);
		}

		private boolean tryPlace(EntityPlayer player, ItemStack stack, World worldIn, BlockPos pos, Object itemSlabType) {
			IBlockState iblockstate = worldIn.getBlockState(pos);
			if (iblockstate.getBlock() == this.singleSlab) {
				IBlockState iblockstate1 = this.doubleSlab.getDefaultState();
				AxisAlignedBB axisalignedbb = iblockstate1.getCollisionBoundingBox(worldIn, pos);
				if (axisalignedbb != Block.NULL_AABB && worldIn.checkNoEntityCollision(axisalignedbb.offset(pos)) && worldIn.setBlockState(pos, iblockstate1, 11)) {
					SoundType soundtype = this.doubleSlab.getSoundType(iblockstate1, worldIn, pos, player);
					worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					stack.shrink(1);
				}
				return true;
			}

			return false;
		}
	}
}