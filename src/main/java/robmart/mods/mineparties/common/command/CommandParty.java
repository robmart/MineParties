package robmart.mods.mineparties.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import robmart.mods.mineparties.api.faction.FactionParty;
import robmart.mods.mineparties.api.reference.Reference;
import robmart.mods.targetingapi.api.Targeting;
import robmart.mods.targetingapi.api.faction.IFaction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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
public class CommandParty extends CommandBase {
    private static final String NAME = "party";
    private static final int    PERMISSION_LEVEL = 0;
    private static final String COMMAND_USAGE    = String.format("commands.%s.%s.usage", Reference.MOD_ID, NAME);

    private List<String> subcommands = new ArrayList<>();

    public CommandParty() {
        subcommands.add("create");
        subcommands.add("list");
    }

    /**
     * Gets the name of the command
     */
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return PERMISSION_LEVEL;
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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        Entity entitySender = sender.getCommandSenderEntity();
        if (args[0].equals("create")) {
            for (IFaction faction : Targeting.getFactionsFromEntity(entitySender)) {
                if (faction instanceof FactionParty)
                    throw new CommandException("Player is already in a party"); //TODO Loc
            }

            IFaction partyFaction = new FactionParty(String.format("%s's Party", sender.getName()));
            partyFaction.addMemberEntity(entitySender);
            Targeting.registerFaction(partyFaction);
        } else if (args[0].equals("list")) {
            for (IFaction faction : Targeting.getFactionsFromEntity(entitySender)) {
                if (faction instanceof FactionParty) {
                    StringBuilder message = new StringBuilder();
                    for (Object object : faction.getAllMembers()) {
                        if (object instanceof EntityPlayer)
                            message.append(((EntityPlayer) object).getName()).append(", ");
                        else
                            message.append(object.toString()).append(", ");
                    }
                    entitySender.sendMessage(new TextComponentString(message.toString()));
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletions(
            MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return args.length == 1 ? subcommands : null;
    }
}
