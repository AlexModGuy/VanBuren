package com.github.alexthe666.oldworldblues.block;

import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import com.github.alexthe666.oldworldblues.item.ItemBlockGenericSlab;
import net.minecraft.block.SoundType;
import net.minecraft.item.ItemBlock;

public abstract class BlockBlastConcreteSlab extends BlockGenericSlab {
    public BlockBlastConcreteSlab(String name, float hardness, float resistance, SoundType soundType) {
        super(name, hardness, resistance, soundType, OWBBlocks.BLAST_CONCRETE_SINGLESLAB);
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockGenericSlab(this, OWBBlocks.BLAST_CONCRETE_SINGLESLAB, OWBBlocks.BLAST_CONCRETE_DOUBLESLAB);
    }

    public static class Double extends BlockGenericSlab {
        public Double(String name, float hardness, float resistance, SoundType soundType) {
            super(name, hardness, resistance, soundType, OWBBlocks.BLAST_CONCRETE_SINGLESLAB);
        }

        @Override
        public ItemBlock getItemBlock() {
            return new ItemBlockGenericSlab(this, OWBBlocks.BLAST_CONCRETE_SINGLESLAB, OWBBlocks.BLAST_CONCRETE_DOUBLESLAB);
        }

        @Override
        public boolean isDouble() {
            return true;
        }
    }

    public static class Half extends BlockGenericSlab {
        public Half(String name, float hardness, float resistance, SoundType soundType) {
            super(name, hardness, resistance, soundType, OWBBlocks.BLAST_CONCRETE_SINGLESLAB);
        }

        @Override
        public ItemBlock getItemBlock() {
            return new ItemBlockGenericSlab(this, OWBBlocks.BLAST_CONCRETE_SINGLESLAB, OWBBlocks.BLAST_CONCRETE_DOUBLESLAB);
        }

        @Override
        public boolean isDouble() {
            return false;
        }
    }
}
