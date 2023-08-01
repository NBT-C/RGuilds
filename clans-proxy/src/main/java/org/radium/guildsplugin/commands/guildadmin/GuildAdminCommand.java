package org.radium.guildsplugin.commands.guildadmin;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.commands.guildadmin.subcmds.*;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.util.TextHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuildAdminCommand extends Command {
    public GuildAdminCommand() {
        super("guildadmin", "guilds.admin", "gadmin", "guildsadmin", "clanadmin", "clansadmin", "gangadmin", "gangsadmin");
    }
    private final Map<UUID, Long> cooldownMap = new HashMap<>();
    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (!(commandSender instanceof ProxiedPlayer)) {
            TextHelper.sendPrefixedMessage(commandSender, LanguageManager.getMsg("Command.NotAPlayer"));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) commandSender;

        if (cooldownMap.containsKey(player.getUniqueId())) {
            long secondsLeft = Core.getInstance().getSettings().getConfig().getInt("CommandCooldown") - ((System.currentTimeMillis() - cooldownMap.get(player.getUniqueId())) / 1000);
            if (secondsLeft > 0) {
                TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.DontSpam")
                        .replace("$time", String.valueOf(secondsLeft)));
                return;
            }
        }

        cooldownMap.put(player.getUniqueId(), System.currentTimeMillis());
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            new AdminHelpSubCommand(player, args);
            return;
        }
        if (args[0].equalsIgnoreCase("deleteguild")){
            new AdminDeleteGuildSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("setleader")){
            new AdminSetLeaderSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("kickfromguild")){
            new AdminKickFromGuildSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("addkills")){
            new AdminAddKillsSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("addpoints")){
            new AdminAddPointsSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("adddeaths")){
            new AdminAddDeathsSubCommand(player, args);
        }
    }
}
