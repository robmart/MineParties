package robmart.mods.mineparties.api.notification;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import robmart.mods.mineparties.api.reference.Reference;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

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
public class Notification {
    private static Map<String, Notification> notificationList = Maps.newHashMap();

    private String identifier;
    private EntityPlayer playerReceiver;
    private ITextComponent message;
    private boolean hasSentMessage = false;
    private Method method;
    private Object instance;
    private Object[] args;

    public Notification(EntityPlayer player, String message, Method method, Object instance, Object... args) {
        this(player, new TextComponentString(message), method, instance, args);
    }

    public Notification(EntityPlayer player, ITextComponent message, Method method, Object instance, Object... args) {
        this.playerReceiver = player;
        this.message = message;
        this.method = method;
        this.instance = instance;
        this.args = args;

        String uuid = "";
        while(uuid.equals("") || notificationList.keySet().contains(uuid)) {
            uuid = UUID.randomUUID().toString();
        }
        this.identifier = uuid;

        notificationList.put(this.identifier, this);
    }

    public static Map<String, Notification> getNotificationList() {
        return notificationList;
    }

    public void sendMessage() {
        message.setStyle(new Style().setUnderlined(true).setClickEvent(
                new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/notification " + getIdentifier())));

        playerReceiver.sendMessage(message);
        this.hasSentMessage = true;
    }

    public void execute() throws InvocationTargetException, IllegalAccessException {
        this.method.invoke(this.instance, this.args);
        notificationList.remove(this.identifier);
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public boolean hasSentMessage() {
        return this.hasSentMessage;
    }

    public void setHasSentMessage(boolean hasSentMessage) {
        this.hasSentMessage = hasSentMessage;
    }

    @Mod.EventBusSubscriber(modid = Reference.MOD_ID)
    public static class EventHandler{
        @SubscribeEvent()
        @SuppressWarnings("unused")
        public static void onServerTick(TickEvent.ServerTickEvent event) {
            for (Notification notification : getNotificationList().values()) {
                if (!notification.hasSentMessage)
                    notification.sendMessage();
            }
        }
    }
}
