package com.github.alexthe666.oldworldblues.message;

import com.github.alexthe666.oldworldblues.entity.VATSProperties;
import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.entity.EntityPropertiesHandler;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageVatsTarget extends AbstractMessage<MessageVatsTarget> {

    private boolean rightOrLeft;

    public MessageVatsTarget(){}

    public MessageVatsTarget(boolean rightOrLeft){
        this.rightOrLeft = rightOrLeft;
    }
    @Override
    public void onClientReceived(Minecraft client, MessageVatsTarget message, EntityPlayer player, MessageContext messageContext) {
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        properties.shiftTarget(message.rightOrLeft);
    }

    @Override
    public void onServerReceived(MinecraftServer server, MessageVatsTarget message, EntityPlayer player, MessageContext messageContext) {
        VATSProperties properties = EntityPropertiesHandler.INSTANCE.getProperties(player, VATSProperties.class);
        properties.shiftTarget(message.rightOrLeft);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        rightOrLeft = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(rightOrLeft);
    }
}
