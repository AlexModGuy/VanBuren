package com.github.alexthe666.oldworldblues.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAsphaltRoadLines extends BlockGeneric implements IRoad {
    public static final PropertyEnum<BlockAsphaltRoadLines.EnumAxis> E_AXIS = PropertyEnum.<BlockAsphaltRoadLines.EnumAxis>create("line_axis", BlockAsphaltRoadLines.EnumAxis.class);

    public BlockAsphaltRoadLines() {
        super("asphalt_road_lines", 2.5F, 15F, Material.ROCK, SoundType.STONE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(E_AXIS, EnumAxis.Z));
    }

    public IBlockState withRotation(IBlockState state, Rotation rot) {
        switch (rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:

                switch ((EnumAxis) state.getValue(E_AXIS)) {
                    case X:
                        return state.withProperty(E_AXIS, EnumAxis.Z);
                    case Z:
                        return state.withProperty(E_AXIS, EnumAxis.X);
                    default:
                        return state;
                }

            default:
                return state;
        }
    }

    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(E_AXIS, EnumAxis.fromFacingAxis(placer.getHorizontalFacing().getAxis()));
    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(E_AXIS, EnumAxis.values()[meta]);
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(E_AXIS).ordinal();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{E_AXIS});
    }


    public static enum EnumAxis implements IStringSerializable {
        X("x"),
        Z("z");

        private final String name;

        private EnumAxis(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public static EnumAxis fromFacingAxis(EnumFacing.Axis axis) {
            switch (axis) {
                case X:
                    return X;
                default:
                    return Z;
            }
        }

        public String getName() {
            return this.name;
        }
    }
}
