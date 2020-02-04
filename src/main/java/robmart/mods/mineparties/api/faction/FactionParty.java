package robmart.mods.mineparties.api.faction;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import robmart.mods.targetingapi.api.faction.Faction;

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
public class FactionParty extends Faction {
    public FactionParty(String name) {
        super(name);
    }

    @Override
    public void addMemberEntity(Entity entityToAdd) {
        if (entityToAdd instanceof EntityPlayer) {
            for (Object member : getAllMembers()) {
                if (member instanceof EntityPlayer)
                    ((EntityPlayer) member).sendStatusMessage(new TextComponentTranslation("commands.mineparties.party.joined", entityToAdd.getName()), false);
            }
        }
        super.addMemberEntity(entityToAdd);
    }
}
