package org.radium.guildsplugin.commands.guild.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.member.GuildMember;
import org.radium.guildsplugin.util.TextHelper;

public class StatsSubCommand {
    private final String subCommandSection = "Command.Guild.SubCommands.GuildStats.";
    public StatsSubCommand(ProxiedPlayer player, String[] args){
        if (args.length > 2){
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

                TextHelper.sendMessage(player, LanguageManager.getMsg(subCommandSection + "Message")
                        .replace("$guildUppercase", guild.getSettings().getGuildName().toUpperCase())
                        .replace("$guild", guild.getSettings().getGuildName())
                        .replace("$guildtag", guild.getSettings().getGuildTag())
                        .replace("{1}", guild.getSettings().getGuildStats().getGlobalKills() + "")
                        .replace("{2}", guild.getSettings().getGuildStats().getGlobalDeaths() + "")
                        .replace("{3}", guild.getSettings().getGuildStats().getGlobalPoints() + "")
                        .replace("$color", guild.getSettings().getGuildColor()));

            } else {
                TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
            }
            return;
        }

        String guildName = args[1];
        Guild guild = Core.getInstance().getGuildManager().getGuildByName(guildName);

        if (guild == null){
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

        TextHelper.sendMessage(player, LanguageManager.getMsg(subCommandSection + "Message")
                .replace("$guildUppercase", guild.getSettings().getGuildName().toUpperCase())
                .replace("$guild", guild.getSettings().getGuildName())
                .replace("$guildtag", guild.getSettings().getGuildTag())
                .replace("{1}", guild.getSettings().getGuildStats().getGlobalKills() + "")
                .replace("{2}", guild.getSettings().getGuildStats().getGlobalDeaths() + "")
                .replace("{3}", guild.getSettings().getGuildStats().getGlobalPoints() + "")
                .replace("$color", guild.getSettings().getGuildColor()));

    }
}
