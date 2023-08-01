package org.radium.guildsplugin.commands.guild.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.member.GuildMember;
import org.radium.guildsplugin.util.TextHelper;

public class LeaveSubCommand {
    private final String subCommandSection = "Command.Guild.SubCommands.GuildLeave.";
    public LeaveSubCommand(ProxiedPlayer player, String[] args){
        if (args.length != 1){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
            return;
        }

        if (!Core.getInstance().getGuildMemberManager().isInGuild(player.getName())){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.NotInGuild"));
            return;
        }

        GuildMember guildMember = Core.getInstance().getGuildMemberManager().getGuildMember(player.getName());
        if (guildMember == null){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.NotInGuild"));
            return;
        }

        if (guildMember.getGuildRank() == GuildRankType.MASTER){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "LeaderOfGuild"));
            return;
        }

        Guild guild = Core.getInstance().getGuildManager().getGuild(guildMember.getGuildId());

        if (guild == null){
            return;
        }

        Core.getInstance().getGuildManager().kickPlayer(guildMember, guild.getId());

        TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Left")
                .replace("$guild", guild.getSettings().getGuildName()));
        Core.getInstance().getGuildManager().sendPrefixedMessageToGuildMembers(guild.getId(),
                LanguageManager.getMsg(subCommandSection + "ToGuildMessage")
                        .replace("$player", player.getName()));
    }
}
