package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockVaultSupport extends BlockRotatedPillar {

    public BlockVaultSupport() {
        super(Material.ROCK);
        this.setHardness(5F);
        this.setSoundType(SoundType.STONE);
        this.setResistance(Float.MAX_VALUE);
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setUnlocalizedName("oldworldblues.vault_support");
        this.setRegistryName(OldWorldBlues.MODID, "vault_support");
    }
}
