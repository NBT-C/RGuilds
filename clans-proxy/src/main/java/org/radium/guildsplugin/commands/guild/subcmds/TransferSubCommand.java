package org.radium.guildsplugin.commands.guild.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.member.GuildMember;
import org.radium.guildsplugin.util.TextHelper;

public class TransferSubCommand {
    private final String subCommandSection = "Command.Guild.SubCommands.GuildTransfer.";
    public TransferSubCommand(ProxiedPlayer player, String[] args){
        if (args.length != 2){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
            return;
        }

        if (!Core.getInstance().getGuildMemberManager().isInGuild(player.getName())){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.NotInGuild"));
            return;
        }

        GuildMember guildMember = Core.getInstance().getGuildMemberManager().getGuildMember(player.getName());

        if (guildMember.getGuildRank() != GuildRankType.MASTER){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.NotTheGuildLeader"));
            return;
        }

        Guild guild = Core.getInstance().getGuildManager().getGuild(guildMember.getGuildId());
        String memberName = args[1];

        GuildMember toTransfer = Core.getInstance().getGuildMemberManager().getGuildMember(memberName);

        if (toTransfer == null || !guild.getSettings().getGuildMemberList().contains(toTransfer)) {
            for (GuildMember member : guild.getSettings().getGuildMemberList()) {
                if (member.getPlayerName().equalsIgnoreCase(memberName)) {
                    toTransfer = member;
                    break;
                }
            }

            if (toTransfer == null || !guild.getSettings().getGuildMemberList().contains(toTransfer)) {
                TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.PlayerNotInYourGuild")
                        .replace("$player", memberName));
                return;
            }
        }

        if (toTransfer.getPlayerName().equals(player.getName())){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "SelfTransfer"));
            return;
        }

        Core.getInstance().getGuildManager().transferLeader(guild.getId(), player.getName(), toTransfer.getPlayerName());
        Core.getInstance().getGuildManager().sendPrefixedMessageToGuildMembers(
                guild.getId(), LanguageManager.getMsg(subCommandSection + "ToGuildMessage")
                        .replace("$player", toTransfer.getPlayerName()));
    }
}
