package org.radium.guildsplugin.commands.guildadmin.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.util.TextHelper;

public class AdminHelpSubCommand {
    public AdminHelpSubCommand(ProxiedPlayer player, String[] args){
        TextHelper.sendMessage(player, LanguageManager.getMsg("Command.GuildAdmin.SubCommands.Help"));
    }
}
