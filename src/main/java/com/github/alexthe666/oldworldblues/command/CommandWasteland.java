package com.github.alexthe666.oldworldblues.command;

import com.github.alexthe666.oldworldblues.OldWorldBlues;
import com.github.alexthe666.oldworldblues.world.TeleporterWasteland;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandWasteland extends CommandBase
{

    public String getName()
    {
        return "wasteland";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getUsage(ICommandSender sender)
    {
        return "commands.wasteland.usage";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length > 1 && args.length < 4)
        {
            throw new WrongUsageException("commands.wasteland.usage", new Object[0]);
        }
        else
        {
            EntityPlayerMP player = args.length > 0 ? getPlayer(server, sender, args[0]) : getCommandSenderAsPlayer(sender);
            BlockPos blockpos = args.length > 3 ? parseBlockPos(sender, args, 1, true) : player.getPosition();

            if (player.world != null)
            {
                player.setSpawnPoint(blockpos, true);
                if (player.timeUntilPortal > 0) {
                    player.timeUntilPortal = 10;
                } else if (player.dimension != OldWorldBlues.CONFIG.wastelandDimensionID) {
                    player.timeUntilPortal = 10;
                    player.mcServer.getPlayerList().transferPlayerToDimension(player, OldWorldBlues.CONFIG.wastelandDimensionID, new TeleporterWasteland(player.mcServer.getWorld(OldWorldBlues.CONFIG.wastelandDimensionID), blockpos));
                } else if (player.dimension == OldWorldBlues.CONFIG.wastelandDimensionID) {
                    player.timeUntilPortal = 10;
                    player.mcServer.getPlayerList().transferPlayerToDimension(player, 0, new TeleporterWasteland(player.mcServer.getWorld(0), blockpos));
                }
                notifyCommandListener(sender, this, "commands.wasteland.success", new Object[] {player.getName(), blockpos.getX(), blockpos.getY(), blockpos.getZ()});
            }
        }
    }

    /**
     * Get a list of options for when the user presses the TAB key
     */
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        else
        {
            return args.length > 1 && args.length <= 4 ? getTabCompletionCoordinate(args, 1, targetPos) : Collections.emptyList();
        }
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}