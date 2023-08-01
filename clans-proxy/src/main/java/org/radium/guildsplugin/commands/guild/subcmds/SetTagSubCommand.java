package org.radium.guildsplugin.commands.guild.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.member.GuildMember;
import org.radium.guildsplugin.util.TextHelper;

public class SetTagSubCommand {
    private final String subCommandSection = "Command.Guild.SubCommands.GuildSetTag.";
    public SetTagSubCommand(ProxiedPlayer player, String[] args){
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

        String newTag = args[1];

        int max = Core.getInstance().getSettings().getConfig().getInt("GuildTag.MaximumTagLength");
        int min = Core.getInstance().getSettings().getConfig().getInt("GuildTag.MinimumTagLength");
        if (newTag.length() < min || newTag.length() > max || !newTag.matches("[a-zA-Z0-9]+")) {
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.Guild.SubCommands.GuildCreation.InvaildGuildTag")
                    .replace("{1}", min+"")
                    .replace("{2}", max+""));
            return;
        }

        for (String blockedTags : Core.getInstance().getSettings().getConfig().getStringList("BlockedGuildTags")){
            if (newTag.equalsIgnoreCase(blockedTags)){
                TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.BlockedTag"));
                return;
            }
        }

        if (Core.getInstance().getGuildManager().isGuildTagTaken(newTag)){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.Guild.SubCommands.GuildCreation.TagTaken"));
            return;
        }

        Core.getInstance().getGuildManager().setGuildTag(guildMember.getGuildId(), newTag);

        Core.getInstance().getGuildManager().sendPrefixedMessageToGuildMembers(
                guildMember.getGuildId(), LanguageManager.getMsg(subCommandSection + "ToGuildMessage")
                        .replace("$newTag", newTag)
        );
    }
}
