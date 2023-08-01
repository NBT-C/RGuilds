package org.radium.guildsplugin.listener;

import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.member.GuildMember;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PlayerDisconnectListener implements Listener {
    @EventHandler
    public void onDisconnect(ServerDisconnectEvent event) {
        GuildMember guildMember = Core.getInstance().getGuildMemberManager().getGuildMember(event.getPlayer().getName());
        if (guildMember == null) {
            System.out.println("yes");
            return;
        }
        guildMember.save();
        if (guildMember.getGuildId() == 0) {
            try {
                try (Connection connection = Core.getInstance().getDataManager().getConnection();
                     PreparedStatement prepareStatement = connection.prepareStatement("DELETE FROM " + GuildMember.TABLE_NAME + " WHERE NAME = '" + guildMember.getPlayerName() + "'")) {
                    prepareStatement.executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Guild guild = Core.getInstance().getGuildManager().getGuild(guildMember.getGuildId());
        if (guild == null){
            return;
        }
        if (guildMember.getGuildRank().equals(GuildRankType.MASTER)) {
            guild.save();
        }
    }
}
