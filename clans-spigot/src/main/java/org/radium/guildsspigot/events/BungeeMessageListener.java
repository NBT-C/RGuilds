package org.radium.guildsspigot.events;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.nametagedit.plugin.NametagEdit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.radium.guildsspigot.Core;

public class BungeeMessageListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (!subchannel.equals("Guilds"))
            return;
        String command = in.readUTF();
        switch (command) {
            case "StorePlayerGuildInformation":
                String playerName = in.readUTF();
                String clanName = in.readUTF();
                String clanTag = in.readUTF();
                String clanColor = in.readUTF();
                String rank = in.readUTF();
                Core.getInstance().getUser(playerName).setGuildName(clanName);
                Core.getInstance().getUser(playerName).setGuildTag(clanTag);
                Core.getInstance().getUser(playerName).setGuildColor(clanColor);
                Core.getInstance().getUser(playerName).setGuildRank(rank);
                if (Core.getInstance().getServer().getPluginManager().isPluginEnabled("NametagEdit")) {
                    Player player1 = Core.getInstance().getServer().getPlayer(playerName);
                    if (player1 == null || !player1.isOnline()) return;
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            NametagEdit.getApi().reloadNametag(player);
                        }
                    }.runTaskLaterAsynchronously(Core.getInstance(), 3L);
                }
        }
    }
}
