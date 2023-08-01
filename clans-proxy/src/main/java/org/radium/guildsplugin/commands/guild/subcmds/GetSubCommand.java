package org.radium.guildsplugin.commands.guild.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.member.GuildMember;
import org.radium.guildsplugin.util.TextHelper;

public class GetSubCommand {
    private final String subCommandSection = "Command.Guild.SubCommands.GuildGet.";
    public GetSubCommand(ProxiedPlayer player, String[] args){
        if (args.length != 2){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
            return;
        }

        String memberName = args[1];
        GuildMember guildMember = Core.getInstance().getGuildMemberManager().getGuildMember(memberName);

        if (guildMember == null){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "PlayerHasNoGuild")
                    .replace("$player", memberName));
            return;
        }

        if (!Core.getInstance().getGuildMemberManager().isInGuild(guildMember.getPlayerName())){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "PlayerHasNoGuild")
                    .replace("$player", memberName));
            return;
        }

        Guild guild = Core.getInstance().getGuildManager().getGuild(guildMember.getGuildId());

        TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "PlayerInGuild")
                .replace("$player", guildMember.getPlayerName())
                .replace("$guild", guild.getSettings().getGuildName()));

    }
}
