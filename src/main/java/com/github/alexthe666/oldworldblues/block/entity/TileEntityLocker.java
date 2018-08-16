package com.github.alexthe666.oldworldblues.block.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityLocker extends TileEntityOWBStorage {
    boolean open;
    public float openProgress;
    private float prevOpenProgress;

    public TileEntityLocker() {
        super(54);
    }

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        open = compound.getBoolean("Open");
        openProgress = compound.getFloat("OpenProgress");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("Open", open);
        compound.setFloat("OpenProgress", openProgress);
        return super.writeToNBT(compound);
    }

    @SideOnly(Side.CLIENT)
    public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox() {
        BlockPos pos = getPos();
        return new net.minecraft.util.math.AxisAlignedBB(pos, pos.add(1, 2, 1));
    }

    public void openInventory(EntityPlayer player) {
        open = true;
        player.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.BLOCK_IRON_DOOR_OPEN, SoundCategory.BLOCKS, 1, 1, false);
    }

    public void closeInventory(EntityPlayer player) {
        if(openProgress > 1) {
            open = false;
        }
        player.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.BLOCK_IRON_DOOR_CLOSE, SoundCategory.BLOCKS, 1, 1, false);
    }

    @Override
    public void update() {
        prevOpenProgress = openProgress;
        if (open && openProgress < 5.0F) {
            openProgress += 0.5F;
        } else if (!open && openProgress > 0.0F) {
            openProgress -= 0.5F;
        }
    }
}
