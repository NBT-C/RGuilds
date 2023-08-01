package org.radium.guildsspigot.events;

import org.radium.guildsspigot.manager.CommunicationManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if (player == null) {
            return;
        }
        CommunicationManager.addGuildKillAndDeath(player, event.getEntity());
    }
}
