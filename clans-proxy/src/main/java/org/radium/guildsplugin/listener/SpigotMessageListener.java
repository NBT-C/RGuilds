package org.radium.guildsplugin.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.manager.object.guild.Guild;
import org.radium.guildsplugin.manager.object.member.GuildMember;

import java.util.Random;

public class SpigotMessageListener implements Listener {
    @EventHandler
    public void onReceive(PluginMessageEvent event) {
        if (!event.getTag().equalsIgnoreCase("BungeeCord"))
            return;
        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subChannel = in.readUTF();
        if (!subChannel.equals("Guilds"))
            return;
        String command = in.readUTF();
        if (command.equals("UpdateGuildStats")) {
            String killer = in.readUTF();
            String victim = in.readUTF();

            GuildMember guildKillerMember = Core.getInstance().getGuildMemberManager().getGuildMember(killer);
            Guild killerGuild;
            if (guildKillerMember != null) {
                killerGuild = Core.getInstance().getGuildManager().getGuild(guildKillerMember.getGuildId());
                killerGuild.getSettings().getGuildStats().addStat("kills", 1);
                if (Core.getInstance().getSettings().getConfig().getBoolean("Points.RandomPoints.Enabled")) {
                    int min = Core.getInstance().getSettings().getConfig().getInt("Points.RandomPoints.PerKill.Minimum");
                    int max = Core.getInstance().getSettings().getConfig().getInt("Points.RandomPoints.PerKill.Maximum");
                    int pointsToAdd = getRandomNumber(min, max);
                    killerGuild.getSettings().getGuildStats().addStat("points", pointsToAdd);
                } else {
                    killerGuild.getSettings().getGuildStats().addStat("points", Core.getInstance().getSettings().getConfig().getInt("Points.PointsPerKill"));
                }
            }
            GuildMember victimGuildMember = Core.getInstance().getGuildMemberManager().getGuildMember(victim);
            Guild victimGuild;
            if (victimGuildMember != null) {
                victimGuild = Core.getInstance().getGuildManager().getGuild(victimGuildMember.getGuildId());
                victimGuild.getSettings().getGuildStats().addStat("deaths", 1);
                if (Core.getInstance().getSettings().getConfig().getBoolean("Points.RandomPoints.Enabled")) {
                    int min = Core.getInstance().getSettings().getConfig().getInt("Points.RandomPoints.PerDeath.Minimum");
                    int max = Core.getInstance().getSettings().getConfig().getInt("Points.RandomPoints.PerDeath.Maximum");
                    int pointsToRemove = getRandomNumber(min, max);
                    victimGuild.getSettings().getGuildStats().addStat("points", pointsToRemove);
                } else {
                    victimGuild.getSettings().getGuildStats().addStat("points", Core.getInstance().getSettings().getConfig().getInt("Points.PointsPerDeath"));
                }
                if (!Core.getInstance().getSettings().getConfig().getBoolean("Points.PointsCanBeNegative")) {
                    if (victimGuild.getSettings().getGuildStats().getGlobalPoints() <= 0) {
                        victimGuild.getSettings().getGuildStats().setGlobalPoints(0);
                    }
                }
            }
        }
    }
    public static int getRandomNumber(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}
