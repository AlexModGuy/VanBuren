package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.item.ItemBlockGenericSlab;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemStack;

public class ItemBlockMetalSlab extends ItemBlockGenericSlab {
    String name;

    public ItemBlockMetalSlab(String name, Block block, BlockSlab singleBlock, BlockSlab doubleBlock) {
        super(block, singleBlock, doubleBlock);
        this.name = name;
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return this.name;
    }
}
