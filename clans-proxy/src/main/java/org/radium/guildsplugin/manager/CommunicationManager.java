package org.radium.guildsplugin.manager;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.member.GuildMember;

import java.util.Collection;

public class CommunicationManager {

    public static void sendPlayerInformation(String playerName) {

        String guildName = "";
        String guildTag = "";
        String guildColor = "";
        String guildRank = GuildRankType.MEMBER.name();
        int guildKills = 0;

        GuildMember guildMember = Core.getInstance().getGuildMemberManager().getGuildMember(playerName);

        Guild guild = null;
        if (guildMember != null) {
            guild = Core.getInstance().getGuildManager().getGuild(guildMember.getGuildId());
        }

        if (guild != null){
            guildName = guild.getSettings().getGuildName();
            guildTag = guild.getSettings().getGuildTag();
            guildColor = guild.getSettings().getGuildColor();
            guildRank = guildMember.getGuildRank().name();
            guildKills = guild.getSettings().getGuildStats().getGlobalKills();
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Guilds");
        out.writeUTF("StorePlayerGuildInformation");
        out.writeUTF(playerName);
        out.writeUTF(guildName);
        out.writeUTF(guildTag);
        out.writeUTF(guildColor);
        out.writeUTF(guildRank);
        out.writeInt(guildKills);

        sendData(out, null);
    }
    public static void sendPlayerInformation(ProxiedPlayer player) {

        String guildName = "";
        String guildTag = "";
        String guildColor = "";
        String guildRank = GuildRankType.MEMBER.name();

        GuildMember guildMember = Core.getInstance().getGuildMemberManager().getGuildMember(player.getName());

        Guild guild = null;
        if (guildMember != null) {
            guild = Core.getInstance().getGuildManager().getGuild(guildMember.getGuildId());
        }

        if (guild != null){
            guildName = guild.getSettings().getGuildName();
            guildTag = guild.getSettings().getGuildTag();
            guildColor = guild.getSettings().getGuildColor();
            guildRank = guildMember.getGuildRank().name();
        }

        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Guilds");
        out.writeUTF("StorePlayerGuildInformation");
        out.writeUTF(player.getName());
        out.writeUTF(guildName);
        out.writeUTF(guildTag);
        out.writeUTF(guildColor);
        out.writeUTF(guildRank);

        sendData(out, player);
    }

    public static void sendData(ByteArrayDataOutput out, ProxiedPlayer player) {
        Collection<ProxiedPlayer> networkPlayers = ProxyServer.getInstance().getPlayers();
        if (networkPlayers == null || networkPlayers.isEmpty())
            return;
        if (player == null){
            for (ServerInfo server : Core.getInstance().getProxy().getServers().values()){
                server.sendData("BungeeCord",out.toByteArray());
            }
            return;
        }
        player.getServer().getInfo().sendData("BungeeCord", out.toByteArray());
    }
}
