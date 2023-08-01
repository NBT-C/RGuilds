package org.radium.guildsplugin.commands.guild.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.member.GuildMember;
import org.radium.guildsplugin.util.TextHelper;

import java.util.*;
import java.util.stream.Collectors;

public class ListSubCommand {
    private final String subCommandSection = "Command.Guild.SubCommands.GuildList.";
    public ListSubCommand(ProxiedPlayer player, String[] args) {
        if (args.length > 2) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
            return;
        }

        if (args.length == 1) {
            if (Core.getInstance().getGuildMemberManager().isInGuild(player.getName())) {
                GuildMember guildMember = Core.getInstance().getGuildMemberManager().getGuildMember(player.getName());
                if (guildMember == null) {
                    TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
                    return;
                }
                Guild guild = Core.getInstance().getGuildManager().getGuild(guildMember.getGuildId());
                if (guild == null) {
                    TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
                    return;
                }

                TextHelper.sendMessage(player, LanguageManager.getMsg(subCommandSection + "Message.Header")
                        .replace("$guildUppercase", guild.getSettings().getGuildName().toUpperCase()));

                int totalOnline = 0;
                int totalOffline = 0;

                List<GuildMember> members = guild.getSettings().getGuildMemberList();
                Map<GuildRankType, List<GuildMember>> membersByRank = members.stream()
                        .collect(Collectors.groupingBy(GuildMember::getGuildRank));
                for (GuildRankType rank : GuildRankType.values()) {
                    List<GuildMember> membersForRank = membersByRank.getOrDefault(rank, Collections.emptyList());
                    if (!membersForRank.isEmpty()) {
                        StringJoiner playerList = new StringJoiner("&7, ");
                        int online = 0;
                        int offline = 0;
                        for (GuildMember member : membersForRank) {
                            ProxiedPlayer player1 = Core.getInstance().getProxy().getPlayer(member.getPlayerName());
                            boolean isConnected = player1 != null && player1.isConnected();
                            offline++;
                            totalOffline++;
                            if (isConnected) {
                                online++;
                                totalOnline++;
                                playerList.add("&a● " + member.getPlayerName());
                            } else {
                                playerList.add("&c● " + member.getPlayerName());
                            }
                        }
                        TextHelper.sendMessage(player, LanguageManager.getMsg(subCommandSection + "Message.Rest")
                                .replace("$guildUppercase", guild.getSettings().getGuildName().toUpperCase())
                                .replace("$players", playerList.toString())
                                .replace("$rank", rank.toString()+"S")
                                .replace("$online", online+"")
                                .replace("$offline", offline-online+"")
                                .replace("$all", offline+online+"")
                                .replace("$guild", guild.getSettings().getGuildName()));

                    }
                }

                int offlinePlayers = totalOffline - totalOnline;

                TextHelper.sendMessage(player, LanguageManager.getMsg(subCommandSection + "Message.Footer")
                        .replace("$guildUppercase", guild.getSettings().getGuildName().toUpperCase())
                        .replace("$guild", guild.getSettings().getGuildName())
                        .replace("$totalonline", totalOnline+"")
                        .replace("$totaloffline", offlinePlayers+"")
                        .replace("$all", offlinePlayers+totalOnline+""));

            } else {
                TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
            }
            return;
        }

        String guildName = args[1];
        Guild guild = Core.getInstance().getGuildManager().getGuildByName(guildName);

        if (guild == null) {
            for (Guild guild1 : Core.getInstance().getGuildManager().getGuildMap().values()) {
                if (guild1.getSettings().getGuildName().equalsIgnoreCase(guildName)) {
                    guild = guild1;
                    break;
                }
            }

            if (guild == null) {
                TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.GuildNotFound"));
                return;
            }
        }

        TextHelper.sendMessage(player, LanguageManager.getMsg(subCommandSection + "Message.Header")
                .replace("$guildUppercase", guild.getSettings().getGuildName().toUpperCase()));

        int totalOnline = 0;
        int totalOffline = 0;

        List<GuildMember> members = guild.getSettings().getGuildMemberList();
        Map<GuildRankType, List<GuildMember>> membersByRank = members.stream()
                .collect(Collectors.groupingBy(GuildMember::getGuildRank));
        for (GuildRankType rank : GuildRankType.values()) {
            List<GuildMember> membersForRank = membersByRank.getOrDefault(rank, Collections.emptyList());
            if (!membersForRank.isEmpty()) {
                StringJoiner playerList = new StringJoiner("&7, ");
                int online = 0;
                int offline = 0;
                for (GuildMember member : membersForRank) {
                    ProxiedPlayer player1 = Core.getInstance().getProxy().getPlayer(member.getPlayerName());
                    boolean isConnected = player1 != null && player1.isConnected();
                    offline++;
                    totalOffline++;
                    if (isConnected) {
                        online++;
                        totalOnline++;
                        playerList.add("&a● " + member.getPlayerName());
                    } else {
                        playerList.add("&c● " + member.getPlayerName());
                    }
                }
                TextHelper.sendMessage(player, LanguageManager.getMsg(subCommandSection + "Message.Rest")
                        .replace("$guildUppercase", guild.getSettings().getGuildName().toUpperCase())
                        .replace("$players", playerList.toString())
                        .replace("$rank", rank.toString()+"S")
                        .replace("$online", online+"")
                        .replace("$offline", offline-online+"")
                        .replace("$all", offline+online+"")
                        .replace("$guild", guild.getSettings().getGuildName()));

            }
        }

        int offlinePlayers = totalOffline - totalOnline;

        TextHelper.sendMessage(player, LanguageManager.getMsg(subCommandSection + "Message.Footer")
                .replace("$guildUppercase", guild.getSettings().getGuildName().toUpperCase())
                .replace("$guild", guild.getSettings().getGuildName())
                .replace("$totalonline", totalOnline+"")
                .replace("$totaloffline", offlinePlayers+"")
                .replace("$all", offlinePlayers+totalOnline+""));

    }
}
