package com.github.alexthe666.oldworldblues.entity;

import com.github.alexthe666.oldworldblues.init.OWBSounds;
import com.github.alexthe666.oldworldblues.init.OWBVillagers;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.world.World;

public class EntitySalesmanVillager extends EntityVillager {

    private boolean hasJustMadeSound = false;
    private int soundTick = 0;

    public EntitySalesmanVillager(World worldIn) {
        super(worldIn);
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if(soundTick > 0){
            soundTick--;
        }
    }
    public void verifySellingItem(ItemStack stack) {
        if (!this.world.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20) {
            this.livingSoundTime = -this.getTalkInterval();
        }
    }

    public void useRecipe(MerchantRecipe recipe) {
        this.livingSoundTime = -this.getTalkInterval();
        this.playSound(OWBSounds.SALESMAN_VILLAGER_TRADE, this.getSoundVolume(), this.getSoundPitch());
        hasJustMadeSound = true;
        super.useRecipe(recipe);
        hasJustMadeSound = false;
    }

    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        if (!this.isSilent() && soundTick == 0 && soundIn != OWBSounds.SALESMAN_VILLAGER_DIE){
            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, soundIn, this.getSoundCategory(), volume, pitch);
            soundTick = 20;
        }
    }

    public boolean isSilent() {
        return hasJustMadeSound || super.isSilent();
    }

    protected SoundEvent getAmbientSound() {
        return this.isTrading() ? OWBSounds.SALESMAN_VILLAGER_TRADE : OWBSounds.SALESMAN_VILLAGER_IDLE;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return OWBSounds.SALESMAN_VILLAGER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return OWBSounds.SALESMAN_VILLAGER_DIE;
    }


    public void setProfession(net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession prof) {
    }

    public net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession getProfessionForge() {
        return OWBVillagers.SALESMAN_PROFESSION;
    }

    protected float getSoundPitch() {
        return this.isChild() ? 0.75F : 1F;
    }

    public EntityVillager createChild(EntityAgeable ageable) {
        EntitySalesmanVillager entityvillager = new EntitySalesmanVillager(this.world);
        entityvillager.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityvillager)), (IEntityLivingData) null);
        return entityvillager;
    }

}
