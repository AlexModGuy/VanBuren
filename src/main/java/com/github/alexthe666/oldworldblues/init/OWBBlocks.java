package com.github.alexthe666.oldworldblues.init;

import com.github.alexthe666.oldworldblues.block.BlockMetalTableSeat;
import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.block.*;
import com.github.alexthe666.oldworldblues.block.BlockVaultFloorMat;
import com.github.alexthe666.oldworldblues.block.entity.TileEntityLocker;
import com.github.alexthe666.oldworldblues.block.entity.TileEntityOWBStorage;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class OWBBlocks {

    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_door")
    public static final Block VAULT_DOOR = new BlockVaultDoor();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_door_frame")
    public static final Block VAULT_DOOR_FRAME = new BlockVaultDoorFrame();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":blast_concrete")
    public static final Block BLAST_CONCRETE = new BlockGenericVault("blast_concrete");
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":blast_concrete_stairs")
    public static final Block BLAST_CONCRETE_STAIRS = new BlockGenericStairs(BLAST_CONCRETE.getDefaultState(), "blast_concrete_stairs");
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":blast_concrete_double_slab")
    public static final BlockGenericSlab BLAST_CONCRETE_DOUBLESLAB = new BlockBlastConcreteSlab.Double("blast_concrete_slab", 4F, Float.MAX_VALUE, SoundType.STONE);
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":blast_concrete_slab")
    public static final BlockGenericSlab BLAST_CONCRETE_SINGLESLAB = new BlockBlastConcreteSlab.Half("blast_concrete_slab", 4F, Float.MAX_VALUE, SoundType.STONE);
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":grating")
    public static final Block GRATING = new BlockGrating();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":grating_stairs")
    public static final Block GRATING_STAIRS = new BlockGratingStairs(GRATING.getDefaultState(), "grating_stairs");
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":grating_double_slab")
    public static final BlockGenericSlab GRATING_DOUBLESLAB = new BlockGratingSlab.Double("grating_slab", 4F, 15F, SoundType.METAL);
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":grating_slab")
    public static final BlockGenericSlab GRATING_SINGLESLAB = new BlockGratingSlab.Half("grating_slab", 4F, 15F, SoundType.METAL);
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_wall")
    public static final Block VAULT_WALL = new BlockVaultWall();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_support")
    public static final Block VAULT_SUPPORT = new BlockVaultSupport();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_beam")
    public static final Block VAULT_BEAM = new BlockVaultBeam();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_lighting")
    public static final Block VAULT_LIGHTING = new BlockVaultLighting();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_floor_tiling")
    public static final Block VAULT_FLOOR_TILING = new BlockGenericVaultConnectedTextures("vault_floor_tiling");
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_metal_plating")
    public static final Block VAULT_METAL_PLATING = new BlockGenericVaultConnectedTextures("vault_metal_plating");
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_metal_plating_stairs")
    public static final Block VAULT_METAL_PLATING_STAIRS = new BlockGenericStairs(VAULT_METAL_PLATING.getDefaultState(), "vault_metal_plating_stairs");
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_metal_plating_double_slab")
    public static final BlockGenericSlab VAULT_METAL_PLATING_DOUBLESLAB = new BlockVaultMetalPlatingSlab.Double("vault_metal_plating_slab", 4F, Float.MAX_VALUE, SoundType.STONE);
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_metal_plating_slab")
    public static final BlockGenericSlab VAULT_METAL_PLATING_SINGLESLAB = new BlockVaultMetalPlatingSlab.Half("vault_metal_plating_slab", 4F, Float.MAX_VALUE, SoundType.STONE);
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_floor_mat")
    public static final Block VAULT_FLOOR_MAT = new BlockVaultFloorMat();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":rust_stain")
    public static final Block RUST_STAIN = new BlockRustStain();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_railing")
    public static final Block VAULT_RAILING = new BlockVaultRailing();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_crate_brown")
    public static final Block VAULT_CRATE_BROWN = new BlockVaultCrate("brown");
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_crate_red")
    public static final Block VAULT_CRATE_RED = new BlockVaultCrate("red");
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_crate_white")
    public static final Block VAULT_CRATE_WHITE = new BlockVaultCrate("white");
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_crate_blue")
    public static final Block VAULT_CRATE_BLUE = new BlockVaultCrate("blue");
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":cardboard_box")
    public static final Block CARDBOARD_BOX = new BlockCardboardBox();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":metal_table_top")
    public static final Block METAL_TABLE_TOP = new BlockMetalTableTop();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":metal_table_seat")
    public static final Block METAL_TABLE_SEAT = new BlockMetalTableSeat();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":interior_vault_door")
    public static final Block INTERIOR_VAULT_DOOR = new BlockInteriorVaultDoor();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":interior_vault_door_frame")
    public static final Block INTERIOR_VAULT_DOOR_FRAME = new BlockInteriorVaultDoorFrame();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":desk_drawer")
    public static final Block DESK_DRAWER = new BlockDeskDrawer();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":desk_top")
    public static final Block DESK_TOP = new BlockDeskTop();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":office_chair")
    public static final Block OFFICE_CHAIR = new BlockOfficeChair();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":computer")
    public static final Block COMPUTER = new BlockComputer();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":coffee_cup")
    public static final Block COFFEE_CUP = new BlockGenericDecoration("coffee_cup", Material.CIRCUITS, SoundType.STONE, true, generateAABBFromPixels(6F, 0F, 6F, 10F, 5F, 10F));
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":clipboard")
    public static final Block CLIPBOARD = new BlockGenericDecoration("clipboard", Material.WOOD, SoundType.WOOD, true, generateAABBFromPixels(5F, 0F, 3F, 11F, 2F, 13F));
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":locker")
    public static final Block LOCKER_BOTTOM = new BlockLockerBottom();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":locker_top")
    public static final Block LOCKER_TOP = new BlockLockerTop();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":empty_nuka_cola_bottle")
    public static final Block EMPTY_NUKA_COLA_BOTTLE = new BlockGenericDecoration("empty_nuka_cola_bottle", Material.GLASS, SoundType.GLASS, true, generateAABBFromPixels(5F, 0F, 5F, 11F, 11F, 11F)).setRenderLayer(BlockRenderLayer.TRANSLUCENT);
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":desk_fan")
    public static final Block DESK_FAN = new BlockGenericDecoration("desk_fan", Material.IRON, SoundType.METAL, true, generateAABBFromPixels(3F, 0F, 3F, 13F, 11F, 13F)).setRenderLayer(BlockRenderLayer.TRANSLUCENT);
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":empty_bottle")
    public static final Block EMPTY_BOTTLE = new BlockGenericDecoration("empty_bottle", Material.GLASS, SoundType.GLASS, true, generateAABBFromPixels(6F, 0F, 6F, 11F, 10F, 10F)).setRenderLayer(BlockRenderLayer.TRANSLUCENT);
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":toolbox")
    public static final Block TOOLBOX = new BlockToolbox();
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":shelf_trolley")
    public static final Block SHELF_TROLLEY = new BlockGenericDecoration("shelf_trolley", Material.IRON, SoundType.METAL, false, generateAABBFromPixels(0F, 0F, 3F, 16F, 16F, 13F)).setRenderLayer(BlockRenderLayer.CUTOUT);
    @GameRegistry.ObjectHolder(OldWorldBlues.MODID + ":vault_door_access")
    public static final Block VAULT_DOOR_ACCESS = new BlockVaultDoorAccess();

    static{
        GameRegistry.registerTileEntity(TileEntityOWBStorage.class, "OWBStorage");
        GameRegistry.registerTileEntity(TileEntityLocker.class, "OWBLocker");
    }

    public static AxisAlignedBB generateAABBFromPixels(float minXPixels, float minYPixels, float minZPixels, float maxXPixels, float maxYPixels, float maxZPixels){
        return new AxisAlignedBB(minXPixels / 16F, minYPixels / 16F, minZPixels / 16F, maxXPixels / 16F, maxYPixels / 16F, maxZPixels / 16F);
    }
}
