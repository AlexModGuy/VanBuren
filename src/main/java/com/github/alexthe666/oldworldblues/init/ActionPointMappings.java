package com.github.alexthe666.oldworldblues.init;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;

import java.util.Map;

public enum ActionPointMappings {
    INSTANCE;

    private Map<ItemStack, Integer> apMap;
    private Map<ItemStack, ActionType> actionTypeMap;

    private ActionPointMappings(){}

    public void registerAP(ItemStack item, int ap) {
        if (!item.isEmpty()) {
            if (this.apMap == null) {
                this.apMap = Maps.newHashMap();
            }
            if (!this.apMap.containsKey(item)) {
                this.apMap.put(item, ap);
            }
        }
    }

    public int getItemAP(ItemStack item) {
        if (this.apMap == null) {
            this.apMap = Maps.newHashMap();
        }
        if(!this.apMap.isEmpty()) {
            for (Map.Entry<ItemStack, Integer> entry : this.apMap.entrySet()) {
                ItemStack compareStack = entry.getKey();
                if (item.isItemEqualIgnoreDurability(compareStack)) {
                    return this.apMap.get(compareStack);
                }
            }
        }
        return 0;
    }

    public void registerActionType(ItemStack item, ActionType actionType) {
        if (!item.isEmpty()) {
            if (this.actionTypeMap == null) {
                this.actionTypeMap = Maps.newHashMap();
            }
            if (!this.actionTypeMap.containsKey(item)) {
                this.actionTypeMap.put(item, actionType);
            }
        }
    }

    public ActionType getItemActionType(ItemStack item) {
        for (Map.Entry<ItemStack, ActionType> entry : this.actionTypeMap.entrySet()) {
            ItemStack compareStack = entry.getKey();
            if (ItemStack.areItemsEqualIgnoreDurability(item, compareStack)) {
                return this.actionTypeMap.get(compareStack);
            }
        }
        return ActionType.NONE;
    }

    public enum ActionType {
        NONE, BOW, MELEE, GUN, PROJECTILE;
    }
}
