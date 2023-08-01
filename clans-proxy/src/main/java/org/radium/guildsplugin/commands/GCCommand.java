package org.radium.guildsplugin.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.commands.guild.subcmds.ChatSubCommand;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.util.TextHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GCCommand extends Command {
    public GCCommand() {
        super("gc", "", "gchat", "guildchat");
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
        new ChatSubCommand(player, args, false);
    }
}
