package com.github.alexthe666.oldworldblues.world;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.List;
import java.util.UUID;

public class OWBWorldData extends WorldSavedData {

    private static final String IDENTIFIER = "oldworldblues";

    private World world;
    private final List<BlockPos> vaultrPositionsList = Lists.<BlockPos>newArrayList();
    private final List<VaultData> vaultList = Lists.<VaultData>newArrayList();
    private int tickCounter;

    public OWBWorldData(String name){
            super(name);
    }

    public OWBWorldData(World world) {
        super(IDENTIFIER);
        this.world = world;
        this.markDirty();
    }

    public void setWorldsForAll(World worldIn) {
        this.world = worldIn;
    }

    public List<VaultData> getVaultList() {
        return this.vaultList;
    }

    public VaultData getNearestVault(BlockPos doorBlock, int radius) {
        VaultData vault = null;
        double d0 = 3.4028234663852886E38D;

        for (VaultData vault1 : this.vaultList) {
            double d1 = vault1.getEnterence().distanceSq(doorBlock);

            if (d1 < d0) {
                float f = (float) (radius + vault1.getVaultRadius());

                if (d1 <= (double) (f * f)) {
                    vault = vault1;
                    d0 = d1;
                }
            }
        }

        return vault;
    }

    private boolean positionInList(BlockPos pos) {
        for (BlockPos blockpos : this.vaultrPositionsList) {
            if (blockpos.equals(pos)) {
                return true;
            }
        }

        return false;
    }

    public void debug(){
        for (VaultData vault : this.vaultList) {
            System.out.println(vault);
        }
    }

    public void readFromNBT(NBTTagCompound nbt) {
        this.tickCounter = nbt.getInteger("Tick");
        NBTTagList nbttaglist = nbt.getTagList("vaults", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
            VaultData vault = new VaultData();
            vault.readVaultDataFromNBT(nbttagcompound);
            this.vaultList.add(vault);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("Tick", this.tickCounter);
        NBTTagList nbttaglist = new NBTTagList();

        for (VaultData vault : this.vaultList) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            vault.writeVaultDataToNBT(nbttagcompound);
            nbttaglist.appendTag(nbttagcompound);
        }

        compound.setTag("vaults", nbttaglist);
        return compound;
    }

    public static OWBWorldData get(World world) {
        MapStorage storage =  world.getPerWorldStorage();
        OWBWorldData instance = (OWBWorldData) storage.getOrLoadData(OWBWorldData.class, IDENTIFIER);

        if (instance == null) {
            instance = new OWBWorldData(world);
            storage.setData(IDENTIFIER, instance);
        }
        return instance;
    }

    public static void addVault(World world, VaultData vault){
        get(world).vaultList.add(vault);
    }

    public VaultData getVaultFromUUID(UUID id) {
        for (VaultData vault : this.vaultList) {
            if(vault.vaultUUID != null && vault.vaultUUID.equals(id)){
                return vault;
            }
        }
        return null;
    }
}
