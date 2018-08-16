package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import net.minecraft.block.SoundType;
import net.minecraft.item.ItemBlock;

public abstract class BlockVaultMetalPlatingSlab extends BlockGenericSlab {
    public BlockVaultMetalPlatingSlab(String name, float hardness, float resistance, SoundType soundType) {
        super(name, hardness, resistance, soundType, OWBBlocks.VAULT_METAL_PLATING_SINGLESLAB);
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockGenericSlab(this, OWBBlocks.VAULT_METAL_PLATING_SINGLESLAB, OWBBlocks.VAULT_METAL_PLATING_DOUBLESLAB);
    }

    public static class Double extends BlockGenericSlab {
        public Double(String name, float hardness, float resistance, SoundType soundType) {
            super(name, hardness, resistance, soundType, OWBBlocks.VAULT_METAL_PLATING_SINGLESLAB);
        }

        @Override
        public ItemBlock getItemBlock() {
            return new ItemBlockGenericSlab(this, OWBBlocks.VAULT_METAL_PLATING_SINGLESLAB, OWBBlocks.VAULT_METAL_PLATING_DOUBLESLAB);
        }

        @Override
        public boolean isDouble() {
            return true;
        }
    }

    public static class Half extends BlockGenericSlab {
        public Half(String name, float hardness, float resistance, SoundType soundType) {
            super(name, hardness, resistance, soundType, OWBBlocks.VAULT_METAL_PLATING_SINGLESLAB);
        }

        @Override
        public ItemBlock getItemBlock() {
            return new ItemBlockGenericSlab(this, OWBBlocks.VAULT_METAL_PLATING_SINGLESLAB, OWBBlocks.VAULT_METAL_PLATING_DOUBLESLAB);
        }

        @Override
        public boolean isDouble() {
            return false;
        }
    }
}
