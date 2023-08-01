package org.radium.guildsplugin.manager.object.guild;

import lombok.Data;
import org.radium.guildsplugin.Core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Data
public class Guild {
    private final int id;
    private final GuildSettings settings;
    private Set<GuildInvite> invites;
    public static final String TABLE_NAME = "core_guilds";

    public Guild(int guildId, GuildSettings guildSettings) {
        this.settings = guildSettings;
        this.id = guildId;
        this.invites = new HashSet<>();
    }

    public void save() {
        final String query = "select * from " + TABLE_NAME + " where ID=" + id;
        final String updateQuery = handleReplace("update " + TABLE_NAME + " " +
                "set GUILD_NAME='{name}', GUILD_TAG='{tag}', GUILD_COLOR='{color}', GUILD_OWNER='{owner}', GUILD_KILLS={kills}, GUILD_DEATHS={deaths}, GUILD_POINTS={points} where ID={id}");
        final String insertQuery = handleReplace("insert into " + TABLE_NAME + " (ID, GUILD_NAME, GUILD_TAG, GUILD_COLOR, GUILD_OWNER, GUILD_KILLS, GUILD_DEATHS, GUILD_POINTS) values " +
                "({id}, '{name}', '{tag}', '{color}', '{owner}', {kills}, {deaths}, {points})");

        try (final Connection connection = Core.getInstance().getDataManager().getConnection();
             final PreparedStatement preparedStatement = connection.prepareStatement(query);
             final ResultSet resultSet = preparedStatement.executeQuery();
             final PreparedStatement ps = connection.prepareStatement(resultSet.next() ? updateQuery : insertQuery)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("MySQL error while saving: " + e.getMessage());
        }
    }
    private String handleReplace(String s){
        return s
                .replace("{id}", id+"")
                .replace("{name}", getSettings().getGuildName())
                .replace("{tag}", getSettings().getGuildTag())
                .replace("{color}", getSettings().getGuildColor())
                .replace("{kills}", getSettings().getGuildStats().getGlobalKills()+"")
                .replace("{deaths}", getSettings().getGuildStats().getGlobalDeaths()+"")
                .replace("{points}", getSettings().getGuildStats().getGlobalPoints()+"")
                .replace("{owner}", getSettings().getLeader());
    }
}
