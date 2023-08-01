package org.radium.guildsplugin.commands.guildadmin.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.member.GuildMember;
import org.radium.guildsplugin.util.TextHelper;

public class AdminSetLeaderSubCommand {
    private final String subCommandSection = "Command.GuildAdmin.SubCommands.GuildSetLeader.";

    public AdminSetLeaderSubCommand(ProxiedPlayer player, String[] args) {
        /*

        if (args.length != 3) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
            return;
        }

        Guild guild = Core.getInstance().getGuildManager().getGuildByName(args[1]);
        if (guild == null) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.GuildNotFound"));
            return;
        }

        String playerName = args[2];
        ProxiedPlayer proxiedPlayer = Core.getInstance().getProxy().getPlayer(playerName);
        if (proxiedPlayer == null || !proxiedPlayer.isConnected()) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.PlayerNotFound"));
            return;
        }

        if (Core.getInstance().getGuildMemberManager().isInGuild(proxiedPlayer.getName())){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "PlayerInAnotherGuild"));
            return;
        }

        GuildMember guildMember = Core.getInstance().getGuildMemberManager().getGuildMember(proxiedPlayer.getName());
        if (guildMember != null){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "PlayerInAnotherGuild"));
            return;
        }

        String from = guild.getSettings().getLeader();
        Core.getInstance().getGuildMemberManager().addMember(proxiedPlayer.getName(), proxiedPlayer.getUniqueId(), guild.getId(), GuildRankType.MEMBER);
        GuildMember toTransfer = Core.getInstance().getGuildMemberManager().getGuildMember(proxiedPlayer.getName());
        Core.getInstance().getGuildManager().transferLeader(guild.getId(), from, toTransfer.getPlayerName());
        toTransfer.setGuildRank(GuildRankType.MASTER);
        Core.getInstance().getGuildMemberManager().getGuildMember(from).setGuildRank(GuildRankType.MEMBER);
        Core.getInstance().getGuildManager().sendPrefixedMessageToGuildMembers(guild.getId(), LanguageManager.getMsg(subCommandSection + "ToGuildMessage")
                .replace("{from}", from)
                .replace("{to}", proxiedPlayer.getName())
                .replace("$guild", guild.getSettings().getGuildName()));
        TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "GuildLeaderChanged")
                .replace("{from}", from)
                .replace("{to}", proxiedPlayer.getName())
                .replace("$guild", guild.getSettings().getGuildName()));
    */
    }
}
