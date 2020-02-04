package robmart.mods.mineparties.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import robmart.mods.mineparties.api.notification.Notification;
import robmart.mods.mineparties.api.reference.Reference;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Robmart.
 * <p>
 * This software is a modification for the game Minecraft, intended to give the game RPG party features.
 * Copyright (C) 2020 Robmart
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
public class CommandNotification extends CommandBase {
    private static final String NAME = "notification";
    private static final int    PERMISSION_LEVEL = 0;
    private static final String COMMAND_USAGE    = String.format("commands.%s.%s.usage", Reference.MOD_ID, NAME);

    /**
     * Gets the name of the command
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender
     */
    @Override
    public String getUsage(ICommandSender sender) {
        return COMMAND_USAGE;
    }

    /**
     * Callback for when the command is executed
     *
     * @param server
     * @param sender
     * @param args
     */
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException { //TODO: Exception when notification doesn't exist
        Notification notification = Notification.getNotificationList().get(args[0]);
        try {
            notification.execute();
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
