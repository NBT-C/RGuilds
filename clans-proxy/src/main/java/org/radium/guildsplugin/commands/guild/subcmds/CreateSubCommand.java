package org.radium.guildsplugin.commands.guild.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.manager.GuildManager;
import org.radium.guildsplugin.manager.IDGeneratorManager;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.util.TextHelper;

public class CreateSubCommand {
    private final String subCommandSection = "Command.Guild.SubCommands.GuildCreation.";
    public CreateSubCommand(ProxiedPlayer player, String[] args){
        if (args.length != 3){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
            return;
        }

        String guildName = args[1];
        String guildTag = args[2];

        if (Core.getInstance().getGuildMemberManager().isInGuild(player.getName())){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.InAGuild"));
            return;
        }

        if (Core.getInstance().getGuildManager().isGuildNameTaken(guildName)){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "NameTaken"));
            return;
        }

        if (Core.getInstance().getGuildManager().isGuildTagTaken(guildTag)){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "TagTaken"));
            return;
        }

        int maxName = Core.getInstance().getSettings().getConfig().getInt("GuildCreate.MaximumNameLength");
        int minName = Core.getInstance().getSettings().getConfig().getInt("GuildCreate.MinimumNameLength");
        if (guildName.length() < minName || guildName.length() > maxName || !guildTag.matches("[a-zA-Z0-9]+")) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "InvaildGuildName")
                    .replace("{1}", minName+"")
                    .replace("{2}", maxName+""));
            return;
        }

        for (String blockedNames : Core.getInstance().getSettings().getConfig().getStringList("BlockedGuildNames")){
            if (guildName.equalsIgnoreCase(blockedNames)){
                TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.BlockedName"));
                return;
            }
        }

        int maxTag = Core.getInstance().getSettings().getConfig().getInt("GuildTag.MaximumTagLength");
        int minTag = Core.getInstance().getSettings().getConfig().getInt("GuildTag.MinimumTagLength");
        if (guildTag.length() < minTag || guildTag.length() > maxTag || !guildTag.matches("[a-zA-Z0-9]+")) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "InvaildGuildTag")
                    .replace("{1}", minTag+"")
                    .replace("{2}", maxTag+""));
            return;
        }

        for (String blockedTags : Core.getInstance().getSettings().getConfig().getStringList("BlockedGuildTags")){
            if (guildName.equalsIgnoreCase(blockedTags)){
                TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.BlockedTag"));
                return;
            }
        }

        if (Core.getInstance().getSettings().getConfig().getBoolean("GuildCreate.RequirePermission")){
            if (!player.hasPermission(Core.getInstance().getSettings().getConfig().getString("GuildCreate.Permission"))){
                TextHelper.sendPrefixedMessage(player, Core.getInstance().getSettings().getConfig().getString("GuildCreate.Message"));
                return;
            }
        }

        if (GuildManager.creationBegan){
            return;
        }

        GuildManager.creationBegan = true;
        Core.getInstance().getGuildManager().createGuild(player, guildName, guildTag);
        TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "GuildCreated")
                .replace("$name", guildName)
                .replace("$tag", guildTag));
    }
}
