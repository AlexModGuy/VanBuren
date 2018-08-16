package com.github.alexthe666.oldworldblues.message;

import com.github.alexthe666.oldworldblues.entity.VATSProperties;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageTriggerVats extends AbstractMessage<MessageTriggerVats> {

    private boolean on;
    private boolean combatTriggered;

    public MessageTriggerVats(){}

    public MessageTriggerVats(boolean on, boolean combatTriggered){
        this.on = on;
        this.combatTriggered = combatTriggered;
    }

    @Override
    public void onClientReceived(Minecraft client, MessageTriggerVats message, EntityPlayer player, MessageContext messageContext) {
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        properties.setVatsTriggered(message.on);
        properties.isCombatTriggered = message.combatTriggered;
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageTriggerVats message, EntityPlayer player, MessageContext messageContext) {
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        properties.setVatsTriggered(message.on);
        properties.isCombatTriggered = message.combatTriggered;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        on = buf.readBoolean();
        combatTriggered = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(on);
        buf.writeBoolean(combatTriggered);
    }
}
