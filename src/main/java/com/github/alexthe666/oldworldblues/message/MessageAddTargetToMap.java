package com.github.alexthe666.oldworldblues.message;

import com.github.alexthe666.oldworldblues.entity.VATSProperties;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageAddTargetToMap extends AbstractMessage<MessageAddTargetToMap> {

    private int entityId;
    private int targetCount;
    private int targetChance;

    public MessageAddTargetToMap(){}

    public MessageAddTargetToMap(int entityId, int targetCount, int targetChance){
        this.entityId = entityId;
        this.targetCount = targetCount;
        this.targetChance = targetChance;
    }

    @Override
    public void onClientReceived(Minecraft client, MessageAddTargetToMap message, EntityPlayer player, MessageContext messageContext) {
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        Entity entity = player.world.getEntityByID(message.entityId);
        if(entity != null){
            properties.targetPoints.put(entity, message.targetCount);
            properties.targetChances.put(entity, message.targetChance);
        }
        properties.combatIndex = 0;
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageAddTargetToMap message, EntityPlayer player, MessageContext messageContext) {
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        Entity entity = player.world.getEntityByID(message.entityId);
        if(entity != null){
            properties.targetPoints.put(entity, message.targetCount);
            properties.targetChances.put(entity, message.targetChance);
        }
        properties.combatIndex = 0;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        targetCount = buf.readInt();
        targetChance = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(targetCount);
        buf.writeInt(targetChance);
    }
}
