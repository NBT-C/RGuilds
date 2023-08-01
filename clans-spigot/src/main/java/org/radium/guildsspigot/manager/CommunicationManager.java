package org.radium.guildsspigot.manager;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.radium.guildsspigot.Core;

public class CommunicationManager {
    public static void addGuildKillAndDeath(Player attacker, Player victim) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Guilds");
        out.writeUTF("UpdateGuildStats");;
        out.writeUTF(attacker.getName());
        out.writeUTF(victim.getName());
        attacker.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
    }
}
