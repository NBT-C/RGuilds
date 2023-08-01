package org.radium.guildsplugin.commands.guild.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildPermissionType;
import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.member.GuildMember;
import org.radium.guildsplugin.util.TextHelper;

public class PromoteSubCommand {
    private final String subCommandSection = "Command.Guild.SubCommands.GuildPromote.";
    public PromoteSubCommand(ProxiedPlayer player, String[] args){
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

        GuildMember toPromoteMember = Core.getInstance().getGuildMemberManager().getGuildMember(memberName);

        if (toPromoteMember == null || !guild.getSettings().getGuildMemberList().contains(toPromoteMember)) {
            for (GuildMember member : guild.getSettings().getGuildMemberList()) {
                if (member.getPlayerName().equalsIgnoreCase(memberName)) {
                    toPromoteMember = member;
                    break;
                }
            }

            if (toPromoteMember == null || !guild.getSettings().getGuildMemberList().contains(toPromoteMember)) {
                TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.PlayerNotInYourGuild")
                        .replace("$player", memberName));
                return;
            }
        }

        if (guildMember.equals(toPromoteMember)){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "CantPromoteYourself"));
            return;
        }

        if (toPromoteMember.getGuildRank().getWeight() <= guildMember.getGuildRank().getWeight()
                || toPromoteMember.getGuildRank().getWeight()-1 == guildMember.getGuildRank().getWeight()) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "CantPromotePlayer"));
            return;
        }


        GuildRankType toPromoteRank = toPromoteMember.getGuildRank();
        GuildRankType nextPromoteRank = toPromoteRank.getNextRank();

        if (nextPromoteRank == null){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "CantPromotePlayer"));
            return;
        }

        Core.getInstance().getGuildManager().promotePlayer(toPromoteMember);
        Core.getInstance().getGuildManager().sendPrefixedMessageToGuildMembers(
                guild.getId(), LanguageManager.getMsg(subCommandSection + "ToGuildMessage")
                        .replace("$player", toPromoteMember.getPlayerName())
                        .replace("$rank", nextPromoteRank.getDisplayName())
        );
    }
}
