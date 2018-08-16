package com.github.alexthe666.oldworldblues.block.entity;

import com.github.alexthe666.oldworldblues.block.BlockInteriorVaultDoor;
import com.github.alexthe666.oldworldblues.block.BlockVaultDoor;
import com.github.alexthe666.oldworldblues.init.OWBBlocks;
import com.github.alexthe666.oldworldblues.init.OWBSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class TileEntityInteriorVaultDoor extends TileEntity implements ITickable {
    public boolean open;
    public float openProgress;
    public Random rand = new Random();

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
        if (world.getBlockState(pos).getBlock() == OWBBlocks.INTERIOR_VAULT_DOOR) {
            BlockPos corner = pos.offset(world.getBlockState(pos).getValue(BlockVaultDoor.FACING).rotateYCCW(), 4).add(1, 4, 1);
            return new net.minecraft.util.math.AxisAlignedBB(pos.offset(world.getBlockState(pos).getValue(BlockInteriorVaultDoor.FACING).rotateYCCW(), -4), corner);
        }
        return super.getRenderBoundingBox();
    }

    @Override
    public void update() {
        if (open && openProgress < 10.0F) {
            openProgress += 0.5F;
        } else if (!open && openProgress > 0.0F) {
            openProgress -= 0.5F;
        }
        if(openProgress == 1 && open || !open && openProgress == 9){
            BlockPos center = pos.offset(world.getBlockState(pos).getValue(BlockVaultDoor.FACING).rotateYCCW(), 1).up(1);
            world.playSound(center.getX(), center.getY(), center.getZ(), OWBSounds.INTERIOR_VAULT_DOOR_OPEN, SoundCategory.BLOCKS, 1, 1, false);
        }
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new SPacketUpdateTileEntity(this.pos, 0, tag);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager netManager, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }
}
