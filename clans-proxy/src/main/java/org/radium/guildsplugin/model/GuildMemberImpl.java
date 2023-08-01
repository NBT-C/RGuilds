package org.radium.guildsplugin.model;

import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.member.GuildMember;

import java.util.UUID;

public interface GuildMemberImpl {
    void addMember(String playerName, UUID uuid, int guildId, GuildRankType guildRank);
    void removeMember(String playerName);
    void removeMember(String playerName, int goldId);
    boolean isInGuild(String playerName);
    GuildMember getGuildMember(String playerName);
    void setMemberRank(String playerName, GuildRankType guildRank);
}
