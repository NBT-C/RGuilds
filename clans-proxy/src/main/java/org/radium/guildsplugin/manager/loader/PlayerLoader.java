package org.radium.guildsplugin.manager.loader;

import net.md_5.bungee.api.ProxyServer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.member.GuildMember;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerLoader {
    public static void load() {
        try (Connection connection = Core.getInstance().getDataManager().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + GuildMember.TABLE_NAME);
             ResultSet resultSet = statement.executeQuery()) {

            long startTime = System.currentTimeMillis();

            while (resultSet.next()) {
                String uuid = resultSet.getString("UUID");
                String ign = resultSet.getString("NAME");
                int guildID = resultSet.getInt("GUILD_ID");
                String rank = resultSet.getString("GUILD_RANK");
                if (guildID == 0){
                    continue;
                }
                Core.getInstance().getGuildMemberManager().addMember(
                        ign, UUID.fromString(uuid), guildID, GuildRankType.valueOf(rank)
                );
            }

            long timeTaken = System.currentTimeMillis() - startTime;
            int loadedPlayersSize = Core.getInstance().getGuildMemberManager().getGuildMemberMap().size();
            Core.getInstance().getLogger().log(Level.INFO, "Loaded " + loadedPlayersSize + " guild members in " + timeTaken + "ms");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while loading user ", e);
        }
    }
}

