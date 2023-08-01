package org.radium.guildsplugin.commands.guild.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.TopType;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.util.TextHelper;

import java.util.HashMap;
import java.util.Map;

public class TopKillsSubCommand {
    private final String subCommandSection = "Command.Guild.SubCommands.GuildTopKills.";
    public TopKillsSubCommand(ProxiedPlayer player, String[] args) {
        if (args.length != 1) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
            return;
        }
        HashMap<Guild, Integer> topKills = Core.getInstance().getGuildManager().getTop(TopType.KILLS);

        TextHelper.sendMessage(player, LanguageManager.getMsg(subCommandSection + "Message.Header"));
        int rank = 1;
        int count = 0;
        for (Map.Entry<Guild, Integer> entry : topKills.entrySet()) {
            if (count >= 10) break;
            TextHelper.sendMessage(player, LanguageManager.getMsg(subCommandSection + "Message.Rest")
                    .replace("%RANK%", rank+"")
                    .replace("%GUILD%", entry.getKey().getSettings().getGuildName())
                    .replace("%COLOR%", "&"+entry.getKey().getSettings().getGuildColor())
                    .replace("%AMOUNT%", entry.getValue()+""));
            rank++;
            count++;
        }
    }
}
