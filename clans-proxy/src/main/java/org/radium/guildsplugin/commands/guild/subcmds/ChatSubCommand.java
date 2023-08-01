package org.radium.guildsplugin.commands.guild.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.member.GuildMember;
import org.radium.guildsplugin.util.TextHelper;

import java.util.Arrays;

public class ChatSubCommand {
    private final String subCommandSection = "Command.Guild.SubCommands.GuildChat.";
    public ChatSubCommand(ProxiedPlayer player, String[] args, boolean guildCommand){
        if (guildCommand){
            if (args.length < 2){
                TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
                return;
            }
        } else {
            if (args.length < 1) {
                TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
                return;
            }
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

        Guild guild = Core.getInstance().getGuildManager().getGuild(guildMember.getGuildId());
        String message = String.join(" ", Arrays.copyOfRange(args, guildCommand ? 1:0, args.length));

        Core.getInstance().getGuildManager().sendMessageToGuildMembers(
                guild.getId(), LanguageManager.getMsg(subCommandSection + "Format")
                        .replace("$player", guildMember.getPlayerName())
                        .replace("$guildUppercase", guild.getSettings().getGuildName().toUpperCase())
                        .replace("$guild", guild.getSettings().getGuildName())
                        .replace("$guildtag", guild.getSettings().getGuildTag())
                        .replace("$color", guild.getSettings().getGuildColor())
                        .replace("$message", message)
                        .replace("%prefix%", LanguageManager.getMsg("Prefix"))
                        .replace("$rank", guildMember.getGuildRank().getDisplayName()));
    }
}
