package com.github.alexthe666.oldworldblues.entity;

import com.github.alexthe666.oldworldblues.event.VATSServerEvents;
import com.google.common.collect.Maps;
import net.ilexiconn.llibrary.server.entity.EntityProperties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;

public class VATSProperties extends EntityProperties<EntityLivingBase> {

	private boolean isVatsTriggered;
	public boolean isCombatTriggered;
	public int combatIndex = 0;
	public int combatShotsTaken = 0;
	public int combatCooldown = 0;
	public boolean hasVatsChanged;
	public boolean isEntitySlowed;
	public int vatsTicks;
	public int ticksAfterVats;
	public Entity currentTarget;
	public VATSServerEvents.Sorter targetSorter = null;
	public int targetIndex = 0;
	public int possibleTargetLength = 0;
	public int frozenTicks = 0;
	public int maxAP = 100;
	public int currentAP = 0;
	public int previousViewPoint = 0;
	public Map<Entity, Integer> targetPoints = Maps.newHashMap();
	public Map<Entity, Integer> targetChances = Maps.newHashMap();

	public boolean isVatsTriggered(){
		return isVatsTriggered;
	}

	public void setVatsTriggered(boolean vats){
		if(isVatsTriggered && !vats){
		//	OldWorldBlues.PROXY.openVatsCombatGui();
		}
		if(!isVatsTriggered && vats){
			this.targetPoints.clear();
			this.targetChances.clear();
		}
		isVatsTriggered = vats;
		hasVatsChanged = true;
		vatsTicks = 0;
		ticksAfterVats = 0;
		frozenTicks = 0;
		currentTarget = null;
		possibleTargetLength = 0;
		targetIndex = 0;
	}

	@Override
	public int getTrackingTime() {
		return 20;
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		compound.setBoolean("VATS", isVatsTriggered);
		compound.setBoolean("VATSSlowedDown", isEntitySlowed);
		compound.setInteger("VATSTicks", vatsTicks);
		compound.setInteger("TicksAfterVATS", ticksAfterVats);
		compound.setInteger("FrozenTicks", frozenTicks);
		compound.setInteger("AP", currentAP);
		compound.setInteger("maxAP", maxAP);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		this.isVatsTriggered = compound.getBoolean("VATS");
		this.isEntitySlowed = compound.getBoolean("VATSSlowedDown");
		this.vatsTicks = compound.getInteger("VATSTicks");
		this.ticksAfterVats = compound.getInteger("TicksAfterVATS");
		this.frozenTicks = compound.getInteger("FrozenTicks");
		this.currentAP = compound.getInteger("AP");
		this.maxAP = compound.getInteger("maxAP");
	}

	@Override
	public void init() {
		isVatsTriggered = false;
		isCombatTriggered = false;
		hasVatsChanged = false;
		isEntitySlowed = false;
		vatsTicks = 0;
		ticksAfterVats = 0;
		maxAP = 100;
	}

	@Override
	public String getID() {
		return "Old World Blues - VATS Property Tracker";
	}

	@Override
	public Class<EntityLivingBase> getEntityClass() {
		return EntityLivingBase.class;
	}

    public void shiftTarget(boolean rightOrLeft) {
		int add = rightOrLeft ? 1 : -1;
		targetIndex += add;
		if(targetIndex > possibleTargetLength){
			targetIndex = 0;
		}
		if(targetIndex < 0){
			targetIndex = possibleTargetLength - 1;
		}
    }
}
