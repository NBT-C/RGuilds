package org.radium.guildsplugin.model;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.enums.TopType;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.guild.GuildInvite;
import org.radium.guildsplugin.manager.object.guild.GuildSettings;
import org.radium.guildsplugin.manager.object.member.GuildMember;

import java.util.HashMap;

public interface GuildModelImpl {
    void createGuild(ProxiedPlayer owner, String name, String tag);
    void deleteGuild(int guildId);
    void load(boolean guilds, boolean players);
    boolean isGuildExists(int guildId);
    boolean isGuildNameTaken(String guildName);
    boolean isGuildTagTaken(String guildTag);
    Guild getGuild(int guildId);
    Guild getGuild(ProxiedPlayer player);
    Guild getGuild(String playerName);
    Guild getGuildByName(String guildName);
    void sendMessageToGuildMembers(int guildId, String message);
    void sendPrefixedMessageToGuildMembers(int guildId, String message);
    void invitePlayer(ProxiedPlayer player, int guildId);
    GuildInvite getGuildInvite(Guild inviter, ProxiedPlayer invited);
    void kickPlayer(GuildMember guildMember, int guildId);
    void promotePlayer(GuildMember guildMember);
    void demotePlayer(GuildMember guildMember);
    void transferLeader(int guildId, String from, String to);
    void renameGuild(int guildId, String newName);
    void setGuildTag(int guildId, String newTag);
    void setGuildColor(int guildId, String newColor);
    HashMap<Guild, Integer> getTop(TopType type);
}
