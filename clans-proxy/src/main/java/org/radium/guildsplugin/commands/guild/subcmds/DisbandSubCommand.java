package org.radium.guildsplugin.commands.guild.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.member.GuildMember;
import org.radium.guildsplugin.util.TextHelper;

public class DisbandSubCommand {
    private final String subCommandSection = "Command.Guild.SubCommands.GuildDisband.";
    public DisbandSubCommand(ProxiedPlayer player, String[] args){
        if (args.length != 1){
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

        Core.getInstance().getGuildManager().sendPrefixedMessageToGuildMembers(
                guildMember.getGuildId(), LanguageManager.getMsg(subCommandSection + "ToGuildMessage")
        );


        Core.getInstance().getGuildManager().deleteGuild(guildMember.getGuildId());
        TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "GuildRemoved"));
    }
}
