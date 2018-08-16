package com.github.alexthe666.oldworldblues.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class EntitySeat extends Entity {

    public EntitySeat(World worldIn) {
        super(worldIn);
        this.setSize(0.75F, 0.75F);
        this.noClip = true;
    }

    @Override
    protected void entityInit() {

    }

    @Nullable
    public Entity getControllingPassenger() {
        for (Entity passenger : this.getPassengers()) {
            if (passenger instanceof EntityPlayer) {
                return passenger;
            }
        }
        return null;
    }

    public void onUpdate(){
        super.onUpdate();
        this.motionX *= 0D;
        this.motionY *= 0D;
        this.motionZ *= 0D;
        if(this.getControllingPassenger() != null && this.getControllingPassenger() instanceof EntityLivingBase){
            if(this.getControllingPassenger().isSneaking()){
                this.getControllingPassenger().setPosition(this.posX, this.posY + 1, this.posZ);
                this.getControllingPassenger().dismountRidingEntity();
                this.setDead();
            }
        }
    }

    @Override
    protected void removePassenger(Entity passenger){
        super.removePassenger(passenger);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {}

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {}
}
