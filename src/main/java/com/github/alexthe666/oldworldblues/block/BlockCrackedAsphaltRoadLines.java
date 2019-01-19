package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockCrackedAsphaltRoadLines extends Block implements IRoad {

    public static final PropertyEnum<BlockAsphaltRoadLines.EnumAxis> E_AXIS = PropertyEnum.<BlockAsphaltRoadLines.EnumAxis>create("line_axis", BlockAsphaltRoadLines.EnumAxis.class);
    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 4);

    public BlockCrackedAsphaltRoadLines() {
        super(Material.ROCK);
        this.setHardness(2.5F);
        this.setSoundType(SoundType.STONE);
        this.setResistance(15F);
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setTranslationKey("oldworldblues.asphalt_cracked_road_lines");
        this.setRegistryName(OldWorldBlues.MODID, "asphalt_cracked_road_lines");
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, 0).withProperty(E_AXIS, BlockAsphaltRoadLines.EnumAxis.Z));
    }

    public IBlockState withRotation(IBlockState state, Rotation rot) {
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:

                switch ((BlockAsphaltRoadLines.EnumAxis) state.getValue(E_AXIS)) {
                    case X:
                        return state.withProperty(E_AXIS, BlockAsphaltRoadLines.EnumAxis.Z);
                    case Z:
                        return state.withProperty(E_AXIS, BlockAsphaltRoadLines.EnumAxis.X);
                    default:
                        return state;
                }

            default:
                return state;
        }
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(E_AXIS, BlockAsphaltRoadLines.EnumAxis.fromFacingAxis(placer.getHorizontalFacing().getAxis()));
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.setBlockState(pos, state.withProperty(VARIANT, worldIn.rand.nextInt(4)));
    }

    public int getMetaFromState(IBlockState state) {
        int variant = ((Integer) state.getValue(VARIANT)).intValue();
        int axis = state.getValue(E_AXIS).ordinal();
        int i = variant;
        if (axis == 1) {
            i += 5;
        }
        return i;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public IBlockState getStateFromMeta(int meta) {
        BlockAsphaltRoadLines.EnumAxis axis;
        if (meta >= 5) {
            axis = BlockAsphaltRoadLines.EnumAxis.X;
        } else {
            axis = BlockAsphaltRoadLines.EnumAxis.Z;
        }
        int variant = meta % 5;
        return this.getDefaultState().withProperty(E_AXIS, axis).withProperty(VARIANT, variant);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{E_AXIS, VARIANT});
    }


}
