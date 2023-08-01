package org.radium.guildsplugin.commands.guildadmin.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.util.TextHelper;

public class AdminAddDeathsSubCommand {
    private final String subCommandSection = "Command.GuildAdmin.SubCommands.GuildAddDeaths.";

    public AdminAddDeathsSubCommand(ProxiedPlayer player, String[] args) {
        if (args.length != 3) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
            return;
        }

        Guild guild = Core.getInstance().getGuildManager().getGuildByName(args[1]);
        if (guild == null) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.GuildNotFound"));
            return;
        }

        if (!isInteger(args[2])){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.InvaildNumber"));
            return;
        }

        int amount = Integer.parseInt(args[2]);

        guild.getSettings().getGuildStats().addStat("deaths", amount);
        TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Added")
                .replace("{amount}", amount+"")
                .replace("$guild", guild.getSettings().getGuildName()));
    }
    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
