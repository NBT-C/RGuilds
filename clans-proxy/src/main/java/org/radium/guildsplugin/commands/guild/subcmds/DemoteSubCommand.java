package org.radium.guildsplugin.commands.guild.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildPermissionType;
import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.member.GuildMember;
import org.radium.guildsplugin.util.TextHelper;

public class DemoteSubCommand {
    private final String subCommandSection = "Command.Guild.SubCommands.GuildDemote.";
    public DemoteSubCommand(ProxiedPlayer player, String[] args){
        if (args.length != 2){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
            return;
        }

        if (!Core.getInstance().getGuildMemberManager().isInGuild(player.getName())){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.NotInGuild"));
            return;
        }

        GuildMember guildMember = Core.getInstance().getGuildMemberManager().getGuildMember(player.getName());

        if (!guildMember.getGuildRank().getGuildPermissionType().contains(GuildPermissionType.PROMOTE)){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.NoGuildPermission"));
            return;
        }

        Guild guild = Core.getInstance().getGuildManager().getGuild(guildMember.getGuildId());
        String memberName = args[1];

        GuildMember toDemoteMember = Core.getInstance().getGuildMemberManager().getGuildMember(memberName);

        if (toDemoteMember == null || !guild.getSettings().getGuildMemberList().contains(toDemoteMember)) {
            for (GuildMember member : guild.getSettings().getGuildMemberList()) {
                if (member.getPlayerName().equalsIgnoreCase(memberName)) {
                    toDemoteMember = member;
                    break;
                }
            }

            if (toDemoteMember == null || !guild.getSettings().getGuildMemberList().contains(toDemoteMember)) {
                TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.PlayerNotInYourGuild")
                        .replace("$player", memberName));
                return;
            }
        }

        if (guildMember.equals(toDemoteMember)){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "CantDemoteYourself"));
            return;
        }

        if (toDemoteMember.getGuildRank().getWeight() <= guildMember.getGuildRank().getWeight()) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "CantDemotePlayer"));
            return;
        }

        GuildRankType toDemoteRank = toDemoteMember.getGuildRank();
        GuildRankType previousDemoteRank = toDemoteRank.getPreviousRank();

        if (previousDemoteRank == null){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "CantDemotePlayer"));
            return;
        }

        Core.getInstance().getGuildManager().demotePlayer(toDemoteMember);
        Core.getInstance().getGuildManager().sendPrefixedMessageToGuildMembers(
                guild.getId(), LanguageManager.getMsg(subCommandSection + "ToGuildMessage")
                        .replace("$player", toDemoteMember.getPlayerName())
                        .replace("$rank", previousDemoteRank.getDisplayName())
        );
    }
}
