package com.github.alexthe666.oldworldblues.event;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.entity.VATSProperties;
import com.github.alexthe666.oldworldblues.util.VATSUtil;
import com.google.common.base.Predicate;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.*;

public class VATSServerEvents {

    public static final Predicate<Entity> LIVING_BASE = new Predicate<Entity>() {
        public boolean apply(@Nullable Entity p_apply_1_) {
            return p_apply_1_ instanceof EntityLivingBase;
        }
    };

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), VATSProperties.class);
        if(properties != null) {
            if (event.getEntityLiving() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                if (properties.combatCooldown > 0) {
                    properties.combatCooldown--;
                }
                if (properties.targetSorter == null) {
                    properties.targetSorter = new Sorter(player);
                }
                if ((properties.isVatsTriggered() || properties.isCombatTriggered) && !VATSUtil.isWearingPipBoy(player)) {
                    properties.setVatsTriggered(false);
                    properties.isCombatTriggered = false;
                    properties.combatIndex = 0;
                    properties.combatShotsTaken = 0;
                }
                if (properties.isCombatTriggered && !properties.targetPoints.isEmpty()) {
                    List<Entity> targets = new ArrayList<>();
                    for (Entity e : properties.targetPoints.keySet()) {
                        for (int i = 0; i < properties.targetPoints.get(e); i++) {
                            targets.add(e);
                        }
                    }
                    List<Entity> livingTargets = new ArrayList<>();
                    for (Entity entity : targets) {
                        if (!isEntityDead(entity)) {
                            livingTargets.add(entity);
                        }
                    }
                    if (properties.combatShotsTaken > livingTargets.size() - 1) {
                        properties.combatIndex = 0;
                        player.resetActiveHand();
                        properties.isCombatTriggered = false;
                        properties.combatShotsTaken = 0;
                    }
                    if (properties.combatCooldown == 0 && properties.combatIndex < livingTargets.size()) {
                        if (player.world.isRemote) {
                            OldWorldBlues.PROXY.updateClientViewPoint();
                        }
                        Entity target = livingTargets.get(properties.combatIndex);
                        this.shootItemAtEntity(player, target, properties.targetChances.getOrDefault(target, 100));
                        properties.combatCooldown = 22;
                    }
                }
                if (VATSUtil.isWearingPipBoy(player) && (properties.isVatsTriggered() || properties.hasVatsChanged)) {
                    List<Entity> listAllNearby = player.world.getEntitiesInAABBexcluding(player, player.getEntityBoundingBox().grow(OldWorldBlues.CONFIG.vatsEffectiveDistance, OldWorldBlues.CONFIG.vatsEffectiveDistance, OldWorldBlues.CONFIG.vatsEffectiveDistance), LIVING_BASE);
                    List<Entity> list = new ArrayList<Entity>();
                    for (Entity entity : listAllNearby) {
                        if (player.canEntityBeSeen(entity)) {
                            list.add(entity);
                        }
                    }
                    Collections.sort(list, properties.targetSorter);
                    properties.possibleTargetLength = list.size() - 1;

                    if (list.isEmpty()) {
                        properties.setVatsTriggered(false);
                        properties.ticksAfterVats = 10;
                    } else {
                        Entity target = null;
                        try {
                            target = properties.possibleTargetLength >= list.size() ? null : list.get(properties.targetIndex);
                        } catch (Exception e) {
                            System.out.println("VATS possible target length:" + properties.possibleTargetLength);
                            e.printStackTrace();
                        }
                        properties.currentTarget = target == null ? list.get(0) : target;
                        if (properties.currentTarget != null) {
                            faceEntity(player, properties.currentTarget, 20, 20);
                        }
                    }
                    for (Entity entity : list) {
                        if (properties.currentTarget != null && properties.currentTarget == entity && properties.isVatsTriggered()) {
                            //  entity.setGlowing(true);
                        } else {
                            //  entity.setGlowing(false);
                        }
                        if ((!(entity instanceof EntityPlayer) || OldWorldBlues.CONFIG.vatsSlowPlayers)) {
                            entity.motionX *= 0.25;
                            entity.motionZ *= 0.25;
                        }
                        if (entity instanceof EntityLivingBase) {
                            ((EntityLivingBase) entity).limbSwing *= 0.75F;
                            ((EntityLivingBase) entity).limbSwingAmount *= 0.5D;
                            ((EntityLivingBase) entity).setAIMoveSpeed(0.005F);
                            VATSProperties propertiesOfEntity = EntityPropertiesHandler.INSTANCE.getProperties(entity, VATSProperties.class);
                            if ((!(entity instanceof EntityPlayer) || OldWorldBlues.CONFIG.vatsSlowPlayers)) {
                                propertiesOfEntity.isEntitySlowed = true;
                            }
                            if (properties.hasVatsChanged) {
                                if (propertiesOfEntity != null) {
                                    propertiesOfEntity.isEntitySlowed = properties.isVatsTriggered();
                                }
                            }
                        }
                    }
                    if (properties.isVatsTriggered()) {
                        properties.vatsTicks++;
                    } else {
                        properties.vatsTicks = 0;
                    }
                }
                if (properties.currentAP < properties.maxAP) {
                    if (!properties.isVatsTriggered() || ((EntityPlayer) event.getEntityLiving()).ticksExisted % 10 == 0) {
                        properties.currentAP++;
                    }
                }
                if (properties.ticksAfterVats > 0) {
                    properties.ticksAfterVats--;
                }
                if (properties.hasVatsChanged) {
                    properties.targetIndex = 0;
                }
                if (properties.hasVatsChanged && !properties.isVatsTriggered()) {
                    properties.ticksAfterVats = 10;
                }
                if (properties.isVatsTriggered()) {
                    event.setCanceled(true);
                }
                properties.hasVatsChanged = false;
            } else {
                EntityLivingBase living = event.getEntityLiving();
                VATSProperties propertiesOfEntity = EntityPropertiesHandler.INSTANCE.getProperties(living, VATSProperties.class);
                if (propertiesOfEntity != null && propertiesOfEntity.isEntitySlowed) {
                    propertiesOfEntity.frozenTicks++;
                    if (propertiesOfEntity.frozenTicks % 2 != 0) {
                        event.setCanceled(true);
                    }
                    living.prevPosX = living.posX;
                    living.prevPosY = living.posY;
                    living.prevPosZ = living.posZ;
                    living.prevRotationPitch = living.rotationPitch;
                    living.prevRotationYaw = living.rotationYaw;
                } else {
                    propertiesOfEntity.frozenTicks = 0;
                }
            }
        }
    }

    private boolean isEntityDead(Entity entity){
        return entity == null || entity.isDead || entity instanceof EntityLivingBase && ((EntityLivingBase) entity).deathTime > 0;
    }

    private void shootItemAtEntity(EntityPlayer player, Entity target, Integer chance) {
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        if (!isEntityDead(target)) {
            if (player.getHeldItemMainhand().getItem() instanceof ItemBow) {
                ItemStack arrowStack = findArrowAmmo(player);
                if(arrowStack.getItem() instanceof ItemArrow){
                    player.setActiveHand(EnumHand.MAIN_HAND);
                    if(!player.isCreative()){
                        arrowStack.shrink(1);
                    }
                    this.faceEntity(player, target, 30, 30);
                    EntityArrow entityarrow = ((ItemArrow) arrowStack.getItem()).createArrow(player.world, arrowStack, player);
                    entityarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                    double d0 = target.posX - player.posX;
                    double d2 = target.posZ - player.posZ;
                    double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
                    double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 1.5F) - entityarrow.posY;

                    entityarrow.shoot(player, player.rotationPitch - (float)(d3 * 0.20000000298023224F), player.rotationYaw, 0.0F, 3.0F, 1 - chance * 0.01F);
                    player.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (player.getRNG().nextFloat() * 0.4F + 0.8F));
                    if(!player.world.isRemote){
                        player.world.spawnEntity(entityarrow);
                    }
                    properties.combatIndex++;
                    properties.combatShotsTaken++;
                }else{
                    properties.isCombatTriggered = false;
                    properties.combatIndex = 0;
                    properties.combatShotsTaken = 0;
                }

            }

        }
    }

    private ItemStack findArrowAmmo(EntityPlayer player) {
        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND))) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND))) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isArrow(itemstack)) {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }

    protected boolean isArrow(ItemStack stack) {
        return stack.getItem() instanceof ItemArrow;
    }

    @SubscribeEvent
    public void onLivingUseItem(LivingEntityUseItemEvent.Start event) {
        VATSProperties propertiesOfEntity = EntityPropertiesHandler.INSTANCE.getProperties(event.getEntityLiving(), VATSProperties.class);
        if (propertiesOfEntity.isEntitySlowed) {
            event.setDuration(event.getItem().getMaxItemUseDuration() / 2);
        }
    }

    public void faceEntity(Entity looker, Entity entityIn, float maxYawIncrease, float maxPitchIncrease) {
        double d0 = entityIn.posX - looker.posX;
        double d2 = entityIn.posZ - looker.posZ;
        double d1;

        if (entityIn instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase) entityIn;
            d1 = entitylivingbase.posY + (double) entitylivingbase.getEyeHeight() - (looker.posY + (double) looker.getEyeHeight());
        } else {
            d1 = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0D - (looker.posY + (double) looker.getEyeHeight());
        }

        double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
        float f = (float) (MathHelper.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;
        float f1 = (float) (-(MathHelper.atan2(d1, d3) * (180D / Math.PI)));
        looker.rotationPitch = this.updateRotation(looker.rotationPitch, f1, maxPitchIncrease);
        looker.rotationYaw = this.updateRotation(looker.rotationYaw, f, maxYawIncrease);
    }

    private float updateRotation(float angle, float targetAngle, float maxIncrease) {
        float f = MathHelper.wrapDegrees(targetAngle - angle);
        if (f > maxIncrease) {
            f = maxIncrease;
        }
        if (f < -maxIncrease) {
            f = -maxIncrease;
        }
        return angle + f;
    }

    public static class Sorter implements Comparator<Entity> {
        private final EntityPlayer player;

        public Sorter(EntityPlayer player) {
            this.player = player;
        }

        public int compare(Entity e1, Entity e2) {
            double d0 = this.player.getDistanceSq(e1);
            double d1 = this.player.getDistanceSq(e2);
            boolean e1Monster = e1 instanceof EntityMob || e1 instanceof EntityLiving && ((EntityLiving) e1).getAttackTarget() != null && ((EntityLiving) e2).getAttackTarget() == player || !e1.isNonBoss();
            boolean e2Monster = e2 instanceof EntityMob || e2 instanceof EntityLiving && ((EntityLiving) e2).getAttackTarget() != null && ((EntityLiving) e2).getAttackTarget() == player || !e2.isNonBoss();
            if (e1Monster && !e2Monster) {
                return -1;
            }
            if (!e1Monster && e2Monster) {
                return 1;
            }
            if (!e1Monster && !e2Monster) {
                if (d0 < d1) {
                    if (e1Monster) {
                        return -1;
                    } else {
                        return d0 > d1 && e2Monster && !e1Monster ? 1 : 0;
                    }
                }
            }
            return 0;
        }
    }
}
