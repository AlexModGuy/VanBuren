package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockGeneric extends Block {

    public BlockGeneric(String name, float hardness, float resistance, Material material, SoundType sound) {
        super(material);
        this.setHardness(hardness);
        this.setSoundType(sound);
        this.setResistance(resistance);
        this.setCreativeTab(OldWorldBlues.TAB);
        this.setTranslationKey("oldworldblues." + name);
        this.setRegistryName(OldWorldBlues.MODID, name);
    }
}
