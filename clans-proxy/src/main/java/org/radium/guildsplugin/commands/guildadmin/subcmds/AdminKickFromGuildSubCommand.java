package org.radium.guildsplugin.commands.guildadmin.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.member.GuildMember;
import org.radium.guildsplugin.util.TextHelper;

public class AdminKickFromGuildSubCommand {
    private final String subCommandSection = "Command.GuildAdmin.SubCommands.GuildKickFromGuild.";

    public AdminKickFromGuildSubCommand(ProxiedPlayer player, String[] args) {
        if (args.length != 2) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
            return;
        }

        String playerName = args[1];
        ProxiedPlayer proxiedPlayer = Core.getInstance().getProxy().getPlayer(playerName);
        if (proxiedPlayer == null || !proxiedPlayer.isConnected()) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.PlayerNotFound"));
            return;
        }

        Guild guild = Core.getInstance().getGuildManager().getGuild(playerName);
        if (guild == null){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.PlayerNotInAGuild"));
            return;
        }

        if (!Core.getInstance().getGuildMemberManager().isInGuild(playerName)) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.PlayerNotInAGuild"));
            return;
        }


        GuildMember guildMember = Core.getInstance().getGuildMemberManager().getGuildMember(playerName);
        if (guildMember == null) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.PlayerNotInAGuild"));
            return;
        }

        if (guildMember.getGuildRank() == GuildRankType.MASTER){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "CantKickLeader"));
            return;
        }

        Core.getInstance().getGuildManager().kickPlayer(guildMember, guildMember.getGuildId());
        Core.getInstance().getGuildManager().sendPrefixedMessageToGuildMembers(guild.getId(), LanguageManager.getMsg("Command.Guild.SubCommands.GuildKick.ToGuildMessage")
                .replace("$player", guildMember.getPlayerName())
                .replace("$guild", guild.getSettings().getGuildName()));
        TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Kicked")
                .replace("$player", guildMember.getPlayerName())
                .replace("$guild", guild.getSettings().getGuildName()));
    }
}
