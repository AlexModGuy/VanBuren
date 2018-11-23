package com.github.alexthe666.oldworldblues.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public class VaultData {
    private BlockPos enterance;
    private BlockPos atriumCenter;
    public UUID vaultUUID;

    public VaultData(){
        this.vaultUUID = UUID.randomUUID();
    }

    public VaultData(BlockPos enterance, BlockPos atriumCenter){
        this.enterance = enterance;
        this.atriumCenter = atriumCenter;
        this.vaultUUID = UUID.randomUUID();
    }

    public void readVaultDataFromNBT(NBTTagCompound compound) {
        this.enterance = new BlockPos(compound.getInteger("EX"), compound.getInteger("EY"), compound.getInteger("EZ"));
        compound.setUniqueId("VaultUUID", this.vaultUUID);
    }

    public void writeVaultDataToNBT(NBTTagCompound compound) {
        compound.setInteger("EX", this.enterance.getX());
        compound.setInteger("EY", this.enterance.getY());
        compound.setInteger("EZ", this.enterance.getZ());
        vaultUUID = compound.getUniqueId("VaultUUID");
        if(vaultUUID == null){
            vaultUUID = UUID.randomUUID();
        }
    }

    public BlockPos getEnterence(){
        return enterance;
    }

    public void setEnterence(BlockPos enterance){
        this.enterance = enterance;
    }

    public int getVaultRadius() {
        return 50;
    }

    public String toString(){
        return "Vault at: " + getEnterence() + " with UUID: " + vaultUUID;
    }
}
