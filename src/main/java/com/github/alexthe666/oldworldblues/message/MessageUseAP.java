package com.github.alexthe666.oldworldblues.message;

import com.github.alexthe666.oldworldblues.entity.VATSProperties;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageUseAP extends AbstractMessage<MessageUseAP> {

    private int usedAP;

    public MessageUseAP(){}

    public MessageUseAP(int usedAP){
        this.usedAP = usedAP;
    }
    @Override
    public void onClientReceived(Minecraft client, MessageUseAP message, EntityPlayer player, MessageContext messageContext) {
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        //properties.currentAP -= message.usedAP;
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageUseAP message, EntityPlayer player, MessageContext messageContext) {
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        properties.currentAP -= message.usedAP;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        usedAP = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(usedAP);
    }
}
