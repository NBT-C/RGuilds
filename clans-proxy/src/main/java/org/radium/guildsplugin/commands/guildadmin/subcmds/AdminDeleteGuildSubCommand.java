package org.radium.guildsplugin.commands.guildadmin.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.util.TextHelper;

public class AdminDeleteGuildSubCommand {
    private final String subCommandSection = "Command.GuildAdmin.SubCommands.GuildDelete.";
    public AdminDeleteGuildSubCommand(ProxiedPlayer player, String[] args){
        if (args.length != 2){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
            return;
        }

        Guild guild = Core.getInstance().getGuildManager().getGuildByName(args[1]);
        if (guild == null){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.GuildNotFound"));
            return;
        }

        Core.getInstance().getGuildManager().sendPrefixedMessageToGuildMembers(
                guild.getId(), LanguageManager.getMsg(subCommandSection + "ToGuildMessage")
        );
        Core.getInstance().getGuildManager().deleteGuild(guild.getId());
        TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "GuildRemoved"));
    }
}
