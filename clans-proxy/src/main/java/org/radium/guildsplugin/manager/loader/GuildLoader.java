package org.radium.guildsplugin.manager.loader;

import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.guild.GuildSettings;
import org.radium.guildsplugin.manager.object.guild.GuildStats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class GuildLoader {
    public static void load(){
        try (final Connection connection = Core.getInstance().getDataManager().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + Guild.TABLE_NAME);
             ResultSet resultSet = statement.executeQuery()) {

            Core.getInstance().getLogger().log(Level.INFO, "Loading guilds...");
            long startTime = System.currentTimeMillis();

            while (resultSet.next()) {
                int guildID = resultSet.getInt("ID");
                int guildKills = resultSet.getInt("GUILD_KILLS");
                int guildDeaths = resultSet.getInt("GUILD_DEATHS");
                int guildPoints = resultSet.getInt("GUILD_POINTS");
                String guildName = resultSet.getString("GUILD_NAME");
                String guildTag = resultSet.getString("GUILD_TAG");
                String guildOwner = resultSet.getString("GUILD_OWNER");
                String guildColor = resultSet.getString("GUILD_COLOR");

                GuildStats guildStats = new GuildStats(guildKills, guildDeaths, guildPoints);
                GuildSettings guildSettings = new GuildSettings(guildStats, guildColor, guildName, guildTag, guildOwner);
                Guild guild = new Guild(guildID, guildSettings);

                Core.getInstance().getGuildManager().getGuildMap().put(guildID, guild);
            }

            long timeTaken = System.currentTimeMillis() - startTime;
            int loadedGuildsSize = Core.getInstance().getGuildManager().getGuildMap().size();
            Core.getInstance().getLogger().log(Level.INFO, "Loaded " + loadedGuildsSize + " guilds in " + timeTaken + "ms");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while loading guilds", e);
        }
    }
}
