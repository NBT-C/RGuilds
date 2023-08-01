package org.radium.guildsplugin.manager;

import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.member.GuildMember;
import org.radium.guildsplugin.model.GuildMemberImpl;

import java.util.HashMap;
import java.util.UUID;

public class GuildMemberManager implements GuildMemberImpl {
    public final @Getter HashMap<String, GuildMember> guildMemberMap = new HashMap<>();
    @Override
    public void addMember(String playerName, UUID uuid, int guildId, GuildRankType guildRankType) {
        Guild guild = Core.getInstance().getGuildManager().getGuild(guildId);
        GuildMember guildMember = new GuildMember(playerName, uuid, guildId, guildRankType);
        guild.getSettings().getGuildMemberList().add(guildMember);
        guildMemberMap.put(playerName, guildMember);
        ProxiedPlayer player = Core.getInstance().getProxy().getPlayer(playerName);
        if (player != null && player.isConnected()) {
            CommunicationManager.sendPlayerInformation(player);
        }
    }
    @Override
    public void removeMember(String playerName, int guildId) {
        Guild guild = Core.getInstance().getGuildManager().getGuild(guildId);
        GuildMember guildMember = getGuildMember(playerName);
        guild.getSettings().getGuildMemberList().remove(guildMember);
        guildMemberMap.remove(playerName);
        ProxiedPlayer player = Core.getInstance().getProxy().getPlayer(playerName);
        if (player != null && player.isConnected()) {
            CommunicationManager.sendPlayerInformation(player);
        }
    }
    @Override
    public void removeMember(String playerName) {
        GuildMember guildMember = getGuildMember(playerName);
        int goldId = guildMember.getGuildId();
        Guild guild = Core.getInstance().getGuildManager().getGuild(goldId);
        guild.getSettings().getGuildMemberList().remove(guildMember);
        guildMemberMap.remove(playerName);
        ProxiedPlayer player = Core.getInstance().getProxy().getPlayer(playerName);
        if (player != null && player.isConnected()) {
            CommunicationManager.sendPlayerInformation(player);
        }
    }

    @Override
    public boolean isInGuild(String playerName) {
        if (!guildMemberMap.containsKey(playerName)){
            return false;
        }
        return getGuildMember(playerName).getGuildId() != 0;
    }
    @Override
    public GuildMember getGuildMember(String playerName) {
        return guildMemberMap.get(playerName);
    }

    @Override
    public void setMemberRank(String playerName, GuildRankType guildRank) {
        getGuildMember(playerName).setGuildRank(guildRank);
    }
}
