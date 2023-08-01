package org.radium.guildsplugin.commands.guild.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.util.TextHelper;

public class HelpSubCommand {
    public HelpSubCommand(ProxiedPlayer player, String[] args){
        TextHelper.sendMessage(player, LanguageManager.getMsg("Command.Guild.SubCommands.Help"));
    }
}
