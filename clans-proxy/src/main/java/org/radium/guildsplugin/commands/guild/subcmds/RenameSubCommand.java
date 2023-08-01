package org.radium.guildsplugin.commands.guild.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.member.GuildMember;
import org.radium.guildsplugin.util.TextHelper;

public class RenameSubCommand {
    private final String subCommandSection = "Command.Guild.SubCommands.GuildRename.";
    public RenameSubCommand(ProxiedPlayer player, String[] args){
        if (args.length != 2){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
            return;
        }

        if (!Core.getInstance().getGuildMemberManager().isInGuild(player.getName())){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.NotInGuild"));
            return;
        }

        GuildMember guildMember = Core.getInstance().getGuildMemberManager().getGuildMember(player.getName());

        if (guildMember.getGuildRank() != GuildRankType.MASTER){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.NotTheGuildLeader"));
            return;
        }

        String newName = args[1];

        int maxName = Core.getInstance().getSettings().getConfig().getInt("GuildCreate.MaximumNameLength");
        int minName = Core.getInstance().getSettings().getConfig().getInt("GuildCreate.MinimumNameLength");
        if (newName.length() < minName || newName.length() > maxName || !newName.matches("[a-zA-Z0-9]+")) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.Guild.SubCommands.GuildCreation.InvaildGuildName")
                    .replace("{1}", minName+"")
                    .replace("{2}", maxName+""));
            return;
        }

        for (String blockedNames : Core.getInstance().getSettings().getConfig().getStringList("BlockedGuildNames")){
            if (newName.equalsIgnoreCase(blockedNames)){
                TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.BlockedName"));
                return;
            }
        }

        if (Core.getInstance().getGuildManager().isGuildNameTaken(newName)){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.Guild.SubCommands.GuildCreation.NameTaken"));
            return;
        }

        Core.getInstance().getGuildManager().renameGuild(guildMember.getGuildId(), newName);

        Core.getInstance().getGuildManager().sendPrefixedMessageToGuildMembers(
                guildMember.getGuildId(), LanguageManager.getMsg(subCommandSection + "ToGuildMessage")
                        .replace("$newName", newName)
        );
    }
}
