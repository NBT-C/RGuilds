package org.radium.guildsplugin.commands.guild;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.commands.guild.subcmds.*;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.util.TextHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuildCommand extends Command {
    public GuildCommand() {
        super("guild", "", "g", "guilds", "clan", "clans", "gang", "gangs");
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
        if (args.length == 0 || args[0].equalsIgnoreCase("help")){
            new HelpSubCommand(player, args);
            return;
        }
        if (args[0].equalsIgnoreCase("create")){
            new CreateSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("disband")){
            new DisbandSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("invite")){
            new InviteSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("accept")){
            new AcceptSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("kick")){
            new KickSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("promote")){
            new PromoteSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("demote")){
            new DemoteSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("get")){
            new GetSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("transfer")){
            new TransferSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("chat")){
            new ChatSubCommand(player, args, true);
        }
        if (args[0].equalsIgnoreCase("stats")){
            new StatsSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("list")){
            new ListSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("rename")){
            new RenameSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("settag")){
            new SetTagSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("setcolor")){
            new SetColorSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("leave")){
            new LeaveSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("topkills")){
            new TopKillsSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("topdeaths")){
            new TopDeathsSubCommand(player, args);
        }
        if (args[0].equalsIgnoreCase("toppoints")){
            new TopPointsSubCommand(player, args);
        }
    }
}
