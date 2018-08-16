package com.github.alexthe666.oldworldblues.block.entity;

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

public class TileEntityVaultDoor extends TileEntity implements ITickable {
    public boolean open;
    public float openProgress;
    public Random rand = new Random();
    public int number;


    public void readFromNBT(NBTTagCompound compound) {
            super.readFromNBT(compound);
            open = compound.getBoolean("Open");
            openProgress = compound.getFloat("OpenProgress");
            number = compound.getInteger("Number");

    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("Open", open);
        compound.setFloat("OpenProgress", openProgress);
        compound.setInteger("Number", number);
        return super.writeToNBT(compound);
    }

    @SideOnly(Side.CLIENT)
    public net.minecraft.util.math.AxisAlignedBB getRenderBoundingBox() {
        BlockPos pos = getPos();
        if (world.getBlockState(pos).getBlock() == OWBBlocks.VAULT_DOOR) {
            BlockPos corner = pos.offset(world.getBlockState(pos).getValue(BlockVaultDoor.FACING).rotateYCCW(), 6).add(1, 7, 1);
            return new net.minecraft.util.math.AxisAlignedBB(pos.offset(world.getBlockState(pos).getValue(BlockVaultDoor.FACING).rotateYCCW(), -6), corner);
        }
        return super.getRenderBoundingBox();
    }

    @Override
    public void update() {
        if (open && openProgress < 40.0F) {
            openProgress += 0.5F;
        } else if (!open && openProgress > 0.0F) {
            openProgress -= 0.5F;
        }
        if(openProgress > 5 && openProgress < 15){
            spawnGearEffects();
        }
        if(openProgress == 1 && open || !open && openProgress == 16){
            BlockPos center = pos.offset(world.getBlockState(pos).getValue(BlockVaultDoor.FACING).rotateYCCW(), 3).up(3);
            world.playSound(center.getX(), center.getY(), center.getZ(), OWBSounds.VAULT_DOOR_OPEN, SoundCategory.BLOCKS, 1, 1, false);
        }
        if(openProgress == 5 && open || !open && openProgress == 39){
            BlockPos center = pos.offset(world.getBlockState(pos).getValue(BlockVaultDoor.FACING).rotateYCCW(), 3).up(3);
            world.playSound(center.getX(), center.getY(), center.getZ(), OWBSounds.VAULT_DOOR_SLIDE, SoundCategory.BLOCKS, 1, 1, false);
        }
    }


    public void spawnGearEffects() {
        IBlockState iblockstate = Blocks.GRAVEL.getDefaultState();
        EnumFacing facing = world.getBlockState(pos).getValue(BlockVaultDoor.FACING).rotateYCCW();
        for (int i1 = 0; i1 < 20; i1++) {
            double motionX = rand.nextGaussian() * 0.07D;
            double motionY = rand.nextGaussian() * 0.07D;
            double motionZ = rand.nextGaussian() * 0.07D;
            float radius = 2.3F;
            float angle = i1;
            double extraX = (double) (radius * MathHelper.sin((float) (Math.PI + angle)));
            double extraY = (double) (radius * MathHelper.cos(angle)) + 3;

            if (iblockstate.getMaterial() != Material.AIR) {
                if (world.isRemote) {
                    if(facing == EnumFacing.NORTH){
                        world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, true, this.pos.getX() + 0.5, this.pos.getY() + extraY, this.pos.getZ() + extraX - 2, motionX, motionY, motionZ, new int[]{Block.getStateId(iblockstate)});
                    }else if(facing ==EnumFacing.SOUTH){
                        world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, true, this.pos.getX() + 0.5, this.pos.getY() + extraY, this.pos.getZ() + extraX + 3, motionX, motionY, motionZ, new int[]{Block.getStateId(iblockstate)});
                    }else if(facing == EnumFacing.WEST){
                        world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, true, this.pos.getX() + extraX - 2, this.pos.getY() + extraY, this.pos.getZ() + 0.5, motionX, motionY, motionZ, new int[]{Block.getStateId(iblockstate)});
                    }else{
                        world.spawnParticle(EnumParticleTypes.BLOCK_CRACK, true, this.pos.getX() + extraX + 3, this.pos.getY() + extraY, this.pos.getZ() + 0.5, motionX, motionY, motionZ, new int[]{Block.getStateId(iblockstate)});
                    }
                }
            }
        }
    }

    private double effectiveXorZ(double x, double z){
        return world.getBlockState(pos).getValue(BlockVaultDoor.FACING).rotateYCCW().getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? x : z;
    }

    private double effectiveZorX(double x, double z){
        return world.getBlockState(pos).getValue(BlockVaultDoor.FACING).rotateYCCW().getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? z : x;
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
