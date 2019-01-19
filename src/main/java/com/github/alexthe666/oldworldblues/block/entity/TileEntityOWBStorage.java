package com.github.alexthe666.oldworldblues.block.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;

public class TileEntityOWBStorage extends TileEntityLockableLoot implements ITickable {


    private NonNullList<ItemStack> chestContents;
    private int ticksSinceSync;
    private int inventorySlots;

    public TileEntityOWBStorage(){
        inventorySlots = 27;
        chestContents = NonNullList.<ItemStack>withSize(inventorySlots, ItemStack.EMPTY);
    }

    public TileEntityOWBStorage(int slotCount){
        inventorySlots = slotCount;
        chestContents = NonNullList.<ItemStack>withSize(inventorySlots, ItemStack.EMPTY);
    }


    public int getSizeInventory() {
        return inventorySlots;
    }

    public boolean isEmpty() {
        for (ItemStack itemstack : this.chestContents) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public String getName() {
        Block block = world.getBlockState(this.pos).getBlock();
        return this.hasCustomName() ? this.customName : block.getTranslationKey() + ".name";
    }

    public static void registerFixesChest(DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityChest.class, new String[]{"Items"}));
    }

    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.inventorySlots = compound.getInteger("InventorySize");
        this.chestContents = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);

        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.loadAllItems(compound, this.chestContents);
        }

        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("InventorySize", this.inventorySlots);
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.saveAllItems(compound, this.chestContents);
        }

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }

        return compound;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        this.fillWithLoot(playerIn);
        return new ContainerChest(playerInventory, this, playerIn);
    }

    @Override
    public String getGuiID() {
        return "oldworldblues:storage";
    }

    protected NonNullList<ItemStack> getItems() {
        return this.chestContents;
    }

    @Override
    public void update() {

    }

    public void openInventory(EntityPlayer player)
    {
        player.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_SHULKER_OPEN, SoundCategory.BLOCKS, 1, 1, false);
    }

    public void closeInventory(EntityPlayer player)
    {
        player.world.playSound(this.pos.getX(), this.pos.getY(), this.pos.getZ(), SoundEvents.ENTITY_SHULKER_CLOSE, SoundCategory.BLOCKS, 1, 1, false);
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
