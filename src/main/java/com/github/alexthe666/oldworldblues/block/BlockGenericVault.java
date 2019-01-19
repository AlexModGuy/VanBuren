package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockGenericVault extends Block {

    public BlockGenericVault(String name) {
        super(Material.ROCK);
        this.setHardness(5F);
        this.setSoundType(SoundType.STONE);
        this.setResistance(Float.MAX_VALUE);
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setTranslationKey("oldworldblues." + name);
        this.setRegistryName(OldWorldBlues.MODID, name);
    }
}
