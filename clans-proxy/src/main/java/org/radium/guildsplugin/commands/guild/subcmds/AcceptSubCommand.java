package org.radium.guildsplugin.commands.guild.subcmds;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.guild.GuildInvite;
import org.radium.guildsplugin.util.TextHelper;

public class AcceptSubCommand {
    private final String subCommandSection = "Command.Guild.SubCommands.GuildAccept.";
    public AcceptSubCommand(ProxiedPlayer player, String[] args){
        if (args.length != 2){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
            return;
        }

        if (Core.getInstance().getGuildMemberManager().isInGuild(player.getName())){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.InAGuild"));
            return;
        }

        Guild guild = Core.getInstance().getGuildManager().getGuildByName(args[1]);

        if (guild == null){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "NoInvite"));
            return;
        }

        GuildInvite guildInvite = Core.getInstance().getGuildManager().getGuildInvite(guild, player);

        if (!guild.getInvites().contains(guildInvite)){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "NoInvite"));
            return;
        }

        Core.getInstance().getGuildManager().sendPrefixedMessageToGuildMembers(
                guild.getId(), LanguageManager.getMsg(subCommandSection + "ToGuildMessage")
                        .replace("$player", player.getName())
        );

        guildInvite.accept();
        guild.getInvites().remove(guildInvite);

        TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Joined")
                .replace("$guild", guild.getSettings().getGuildName()));
    }
}
