package org.radium.guildsplugin.commands.guild.subcmds;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildPermissionType;
import org.radium.guildsplugin.manager.LanguageManager;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.guild.GuildInvite;
import org.radium.guildsplugin.manager.object.member.GuildMember;
import org.radium.guildsplugin.util.TextHelper;

public class InviteSubCommand {
    private final String subCommandSection = "Command.Guild.SubCommands.GuildInvite.";
    public InviteSubCommand(ProxiedPlayer player, String[] args){
        if (args.length != 2){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "Usage"));
            return;
        }

        if (!Core.getInstance().getGuildMemberManager().isInGuild(player.getName())){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.NotInGuild"));
            return;
        }

        GuildMember guildMember = Core.getInstance().getGuildMemberManager().getGuildMember(player.getName());

        if (!guildMember.getGuildRank().getGuildPermissionType().contains(GuildPermissionType.INVITE)){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.NoGuildPermission"));
            return;
        }

        ProxiedPlayer invited = Core.getInstance().getProxy().getPlayer(args[1]);

        if (invited == null || !invited.isConnected()){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.PlayerNotFound"));
            return;
        }

        if (player.equals(invited)){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "CantInviteYourself"));
            return;
        }

        if (Core.getInstance().getGuildMemberManager().isInGuild(invited.getName())){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg("Command.PlayerInAGuild"));
            return;
        }

        Guild inviter = Core.getInstance().getGuildManager().getGuild(guildMember.getGuildId());
        GuildInvite guildInvite = Core.getInstance().getGuildManager().getGuildInvite(inviter, invited);

        if (guildInvite != null && inviter.getInvites().contains(guildInvite)){
            TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "AlreadyInvited"));
            return;
        }

        Core.getInstance().getGuildManager().invitePlayer(invited, guildMember.getGuildId());
        TextComponent message = new TextComponent(TextHelper.formatClicked(LanguageManager.getMsg(subCommandSection + "PlayerReceiveInvite"))
                .replace("$guild", Core.getInstance().getGuildManager().getGuild(guildMember.getGuildId()).getSettings().getGuildName()));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guild accept " + Core.getInstance().getGuildManager().getGuild(guildMember.getGuildId()).getSettings().getGuildName()));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to accept this guild invitation")));
        invited.sendMessage(message);

        TextHelper.sendPrefixedMessage(player, LanguageManager.getMsg(subCommandSection + "PlayerInvited")
                .replace("$guild", Core.getInstance().getGuildManager().getGuild(guildMember.getGuildId()).getSettings().getGuildName())
                .replace("$player", invited.getName()));
    }
}
