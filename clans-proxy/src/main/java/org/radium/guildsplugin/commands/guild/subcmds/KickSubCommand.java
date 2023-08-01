package org.radium.guildsplugin.commands.guild.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildPermissionType;
import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.member.GuildMember;
import org.radium.guildsplugin.util.TextHelper;

public class KickSubCommand {
    private final String subCommandSection = "Command.Guild.SubCommands.GuildKick.";
    public KickSubCommand(ProxiedPlayer player, String[] args){
        if (args.length != 2){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
            return;
        }

        if (!Core.getInstance().getGuildMemberManager().isInGuild(player.getName())){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.NotInGuild"));
            return;
        }

        GuildMember guildMember = Core.getInstance().getGuildMemberManager().getGuildMember(player.getName());

        if (!guildMember.getGuildRank().getGuildPermissionType().contains(GuildPermissionType.KICK)){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.NoGuildPermission"));
            return;
        }

        Guild guild = Core.getInstance().getGuildManager().getGuild(guildMember.getGuildId());
        String memberName = args[1];

        GuildMember guildKickedMember = Core.getInstance().getGuildMemberManager().getGuildMember(memberName);

        if (guildKickedMember == null || !guild.getSettings().getGuildMemberList().contains(guildKickedMember)) {
            for (GuildMember member : guild.getSettings().getGuildMemberList()) {
                if (member.getPlayerName().equalsIgnoreCase(memberName)) {
                    guildKickedMember = member;
                    break;
                }
            }

            if (guildKickedMember == null || !guild.getSettings().getGuildMemberList().contains(guildKickedMember)) {
                TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.PlayerNotInYourGuild")
                        .replace("$player", memberName));
                return;
            }
        }

        if (guildMember.equals(guildKickedMember)){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "CantKickYourself"));
            return;
        }

        if (guildKickedMember.getGuildRank() == GuildRankType.MASTER
        || guildKickedMember.getGuildRank().getWeight() <= guildMember.getGuildRank().getWeight()){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "PlayerCannotBeKicked"));
            return;
        }

        Core.getInstance().getGuildManager().kickPlayer(guildKickedMember, guild.getId());

        Core.getInstance().getGuildManager().sendPrefixedMessageToGuildMembers(
                guild.getId(), LanguageManager.getMsg(subCommandSection + "ToGuildMessage")
                        .replace("$player", guildKickedMember.getPlayerName())
        );

        ProxiedPlayer kicked = Core.getInstance().getProxy().getPlayer(guildKickedMember.getPlayerName());

        if (kicked == null || !kicked.isConnected()){
            return;
        }

        TextHelper.sendPrefixedMessage(kicked, LanguageManager.getMsg(subCommandSection + "Kicked")
                .replace("$guild", guild.getSettings().getGuildName()));
    }
}
