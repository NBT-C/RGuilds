package org.radium.guildsplugin.listener;

import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.manager.CommunicationManager;

import java.util.concurrent.TimeUnit;

public class PlayerConnectListener implements Listener {
    @EventHandler
    public void onConnect(ServerConnectedEvent event){
        Core.getInstance().getProxy().getScheduler().schedule(Core.getInstance(), () -> {
            CommunicationManager.sendPlayerInformation(event.getPlayer());
        }, 3L, TimeUnit.MILLISECONDS);
    }

}
