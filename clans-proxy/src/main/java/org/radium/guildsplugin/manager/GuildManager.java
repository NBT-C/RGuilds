package org.radium.guildsplugin.manager;

import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.enums.TopType;
import org.radium.guildsplugin.manager.loader.GuildLoader;
import org.radium.guildsplugin.manager.loader.PlayerLoader;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.guild.GuildInvite;
import org.radium.guildsplugin.manager.object.guild.GuildSettings;
import org.radium.guildsplugin.manager.object.guild.GuildStats;
import org.radium.guildsplugin.manager.object.member.GuildMember;
import org.radium.guildsplugin.model.GuildModelImpl;
import org.radium.guildsplugin.util.TextHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GuildManager implements GuildModelImpl {
    public static boolean creationBegan = false;
    public final @Getter HashMap<Integer, Guild> guildMap = new HashMap<>();

    @Override
    public void createGuild(ProxiedPlayer owner, String name, String tag) {
        GuildStats guildStats = new GuildStats(0, 0, 0);
        GuildSettings guildSettings = new GuildSettings(guildStats, "7", name, tag, owner.getName());
        Guild guild = new Guild(IDGeneratorManager.getNextId(), guildSettings);
        guildMap.put(guild.getId(), guild);
        Core.getInstance().getGuildMemberManager().addMember(owner.getName(), owner.getUniqueId(), guild.getId(), GuildRankType.MASTER);
        guild.save();
        Core.getInstance().getProxy().getScheduler().schedule(Core.getInstance(), () -> {
            creationBegan = false;
        }, 1, TimeUnit.SECONDS);
    }

    @Override
    public void deleteGuild(int guildId) {
        Guild guild = getGuild(guildId);
        if (guild == null) {
            return;
        }
        for (GuildMember player : Core.getInstance().getGuildMemberManager().getGuildMemberMap().values()) {
            if (player.getGuildId() == guildId) {
                player.setGuildId(0);
                CommunicationManager.sendPlayerInformation(player.getPlayerName());
            }
        }
        ProxyServer.getInstance().getScheduler().runAsync(Core.getInstance(), () -> {
            try {
                try (Connection connection = Core.getInstance().getDataManager().getConnection();
                     PreparedStatement prepareStatement = connection.prepareStatement("DELETE FROM " + Guild.TABLE_NAME + " WHERE ID = " + guildId)) {
                    prepareStatement.executeUpdate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            guildMap.remove(guildId);
        });
    }


    @Override
    public void load(boolean guilds, boolean players) {
        if (guilds) GuildLoader.load();
        if (players) PlayerLoader.load();
    }

    @Override
    public boolean isGuildExists(int guildId) {
        return guildMap.get(guildId) != null;
    }

    @Override
    public boolean isGuildNameTaken(String guildName) {
        for (Guild guild : guildMap.values()) {
            if (guild.getSettings().getGuildName().equalsIgnoreCase(guildName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isGuildTagTaken(String guildTag) {
        for (Guild guild : guildMap.values()) {
            if (guild.getSettings().getGuildTag().equalsIgnoreCase(guildTag)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Guild getGuild(int guildId) {
        Guild guild = guildMap.get(guildId);
        if (guild == null) {
            return null;
        }
        return guild;
    }

    @Override
    public Guild getGuild(ProxiedPlayer player) {
        return getGuild(player.getDisplayName());
    }

    @Override
    public Guild getGuild(String playerName) {
        GuildMember guildMember = Core.getInstance().getGuildMemberManager().getGuildMember(playerName);
        if (guildMember == null){
            return null;
        }
        return getGuild(guildMember.getGuildId());
    }

    @Override
    public Guild getGuildByName(String guildName) {
        return guildMap.values().stream().filter(
                guild -> guild.getSettings().getGuildName().equals(guildName)).findFirst().orElse(null);
    }

    @Override
    public void sendMessageToGuildMembers(int guildId, String message) {
        for (Guild guild : guildMap.values()) {
            if (guild.getId() == guildId) {
                for (GuildMember guildMember : guild.getSettings().getGuildMemberList()) {
                    ProxiedPlayer player = Core.getInstance().getProxy().getPlayer(guildMember.getPlayerName());
                    if (player != null && player.isConnected()) {
                        TextHelper.sendMessage(player, message);
                    }
                }
            }
        }
    }

    @Override
    public void sendPrefixedMessageToGuildMembers(int guildId, String message) {
        for (Guild guild : guildMap.values()) {
            if (guild.getId() == guildId) {
                for (GuildMember guildMember : guild.getSettings().getGuildMemberList()) {
                    ProxiedPlayer player = Core.getInstance().getProxy().getPlayer(guildMember.getPlayerName());
                    if (player != null && player.isConnected()) {
                        TextHelper.sendPrefixedMessage(player, message);
                    }
                }
            }
        }
    }

    @Override
    public void invitePlayer(ProxiedPlayer player, int guildId) {
        Guild guild = getGuild(guildId);
        GuildInvite guildInvite = new GuildInvite(guild, player);
        guild.getInvites().add(guildInvite);
        Core.getInstance().getProxy().getScheduler().schedule(Core.getInstance(), () -> {
            if (guild.getInvites().contains(guildInvite)) {
                guild.getInvites().remove(guildInvite);
                if (!player.isConnected()) return;
                TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.Guild.SubCommands.GuildInvite.InviteExpired")
                        .replace("$guild", guild.getSettings().getGuildName()));
            }
        }, 30, TimeUnit.SECONDS);
    }

    @Override
    public GuildInvite getGuildInvite(Guild inviter, ProxiedPlayer invited) {
        for (GuildInvite invite : inviter.getInvites()) {
            if (invite.getInviter().equals(inviter) && invite.getInvited().equals(invited)) {
                return invite;
            }
        }
        return null;
    }

    @Override
    public void kickPlayer(GuildMember guildMember, int guildId) {
        if (guildMember == null){
            return;
        }
        Guild guild = getGuild(guildId);
        if (guild == null) {
            return;
        }
        Core.getInstance().getGuildMemberManager().removeMember(guildMember.getPlayerName());
        guildMember.setGuildId(0);
        ProxyServer.getInstance().getScheduler().runAsync(Core.getInstance(), () -> {
            try (Connection connection = Core.getInstance().getDataManager().getConnection();
                 PreparedStatement prepareStatement = connection.prepareStatement("DELETE FROM " + GuildMember.TABLE_NAME + " WHERE NAME = '" + guildMember.getPlayerName() + "'")) {
                prepareStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void promotePlayer(GuildMember guildMember) {
        if (guildMember == null) {
            return;
        }
        GuildRankType currentRank = guildMember.getGuildRank();
        GuildRankType nextRank = currentRank.getNextRank();
        if (nextRank == null) {
            return;
        }
        guildMember.setGuildRank(nextRank);
    }

    @Override
    public void demotePlayer(GuildMember guildMember) {
        if (guildMember == null) {
            return;
        }
        GuildRankType currentRank = guildMember.getGuildRank();
        GuildRankType previousRank = currentRank.getPreviousRank();
        if (previousRank == null) {
            return;
        }
        guildMember.setGuildRank(previousRank);
    }

    @Override
    public void transferLeader(int guildId, String from, String to) {
        Guild guild = getGuild(guildId);
        if (guild == null) {
            return;
        }
        guild.getSettings().setLeader(to);
        GuildMember toTransfer = Core.getInstance().getGuildMemberManager().getGuildMember(to);
        if (toTransfer == null) {
            return;
        }
        toTransfer.setGuildRank(GuildRankType.MASTER);
        GuildMember fromMember = Core.getInstance().getGuildMemberManager().getGuildMember(from);
        if (fromMember == null) {
            return;
        }
        fromMember.setGuildRank(GuildRankType.MEMBER);
    }

    @Override
    public void renameGuild(int guildId, String newName) {
        Guild guild = getGuild(guildId);
        if (guild == null) {
            return;
        }
        guild.getSettings().setGuildName(newName);
    }
    @Override
    public void setGuildTag(int guildId, String newTag) {
        Guild guild = getGuild(guildId);
        if (guild == null) {
            return;
        }
        guild.getSettings().setGuildTag(newTag);
    }
    @Override
    public void setGuildColor(int guildId, String newColor) {
        Guild guild = getGuild(guildId);
        if (guild == null) {
            return;
        }
        guild.getSettings().setGuildColor(newColor);
    }

    public HashMap<Guild, Integer> getTop(TopType type) {
        HashMap<Guild, Integer> top = new HashMap<>();
        switch (type) {
            case KILLS:
                guildMap.forEach((guildName, guild) -> top.put(guild, guild.getSettings().getGuildStats().getGlobalKills()));
                break;
            case DEATHS:
                guildMap.forEach((guildName, guild) -> top.put(guild, guild.getSettings().getGuildStats().getGlobalDeaths()));
                break;
            case POINTS:
                guildMap.forEach((guildName, guild) -> top.put(guild, guild.getSettings().getGuildStats().getGlobalPoints()));
                break;
            default:
                break;
        }

        List<Map.Entry<Guild, Integer>> list = new ArrayList<>(top.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        HashMap<Guild, Integer> sortedTop = new LinkedHashMap<>();
        for (Map.Entry<Guild, Integer> entry : list) {
            sortedTop.put(entry.getKey(), entry.getValue());
        }
        return sortedTop;
    }

}