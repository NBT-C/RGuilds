package org.radium.guildsplugin.manager.object.member;

import lombok.Data;
import net.md_5.bungee.api.ProxyServer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildRankType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Data
public class GuildMember {
    private final String playerName;
    private final UUID uuid;
    private int guildId;
    private GuildRankType guildRank;
    public static final String TABLE_NAME = "core_players_guilds";

    public GuildMember(String playerName, UUID uuid, int guildId, GuildRankType guildRank) {
        this.playerName = playerName;
        this.uuid = uuid;
        this.guildRank = guildRank;
        this.guildId = guildId;
    }

    public void save() {
        try (Connection connection = Core.getInstance().getDataManager().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE uuid=?")) {
            preparedStatement.setString(1, uuid + "");
            try (ResultSet resultSet = preparedStatement.executeQuery();
                 PreparedStatement ps = connection.prepareStatement(resultSet.next() ? "" +
                         handleReplace("UPDATE " + TABLE_NAME + " SET NAME='{name}', GUILD_ID={id}, GUILD_RANK='{rank}' WHERE UUID='{uuid}'") :
                         handleReplace("INSERT INTO " + TABLE_NAME + " (UUID, NAME, GUILD_ID, GUILD_RANK) VALUES ('{uuid}', '{name}', {id}, '{rank}')"))) {
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private String handleReplace(String s) {
        return s
                .replace("{id}", guildId + "")
                .replace("{name}", playerName)
                .replace("{rank}", guildRank.name())
                .replace("{uuid}", uuid + "");

    }
}
