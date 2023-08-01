package org.radium.guildsspigot.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.radium.guildsspigot.Core;
import org.radium.guildsspigot.manager.LabyModManager;
import org.radium.guildsspigot.manager.object.User;

import java.util.Objects;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        boolean titlesEnabled = Core.getInstance().getSettingsConfiguration().getConfig().getBoolean("LabyMod.EnableTitles");
        boolean labyModStatusEnabled = Core.getInstance().getSettingsConfiguration().getConfig().getBoolean("LabyMod.EnableLabyModStatus");
        if (titlesEnabled) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player players : Core.getInstance().getServer().getOnlinePlayers()) {
                        User user = Core.getInstance().getUser(event.getPlayer().getName());
                        if (user.getGuildName() == null) continue;
                        String title = Core.getInstance().getSettingsConfiguration().getConfig().getString("LabyMod.Titles.Ranks." + user.getGuildRank().toUpperCase())
                                .replace("$guildUppercase", user.getGuildName()).toUpperCase()
                                .replace("$guild", user.getGuildName())
                                .replace("$guildColor", user.getGuildColor())
                                .replace("$guildTag", user.getGuildTag())
                                .replace("$guildRank", user.getGuildRank());
                        User users = Core.getInstance().getUser(players.getName());
                        if (users.getGuildName() == null) continue;
                        String playersTitle = Core.getInstance().getSettingsConfiguration().getConfig().getString("LabyMod.Titles.Ranks." + users.getGuildRank().toUpperCase())
                                .replace("$guildUppercase", users.getGuildName()).toUpperCase()
                                .replace("$guild", users.getGuildName())
                                .replace("$guildColor", users.getGuildColor())
                                .replace("$guildTag", users.getGuildTag())
                                .replace("$guildRank", users.getGuildRank());
                        if (!user.getGuildName().isEmpty()) {
                            LabyModManager.setSubtitle(players, event.getPlayer(), title);
                        } else {
                            LabyModManager.setSubtitle(players, event.getPlayer(), "");
                        }
                        if (!users.getGuildName().isEmpty()) {
                            LabyModManager.setSubtitle(event.getPlayer(), players, playersTitle);
                        } else {
                            LabyModManager.setSubtitle(event.getPlayer(), players, "");
                        }
                    }
                }
            }.runTaskLater(Core.getInstance(), 10L);
        }
        if (labyModStatusEnabled) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    User user = Core.getInstance().getUser(event.getPlayer().getName());
                    if (user.getGuildName().isEmpty()){
                        LabyModManager.sendCurrentPlayingGamemode(event.getPlayer(), ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Core.getInstance().getSettingsConfiguration().getConfig().getString("LabyMod.Status.HasNoGuild"))));
                        return;
                    }
                    LabyModManager.sendCurrentPlayingGamemode(event.getPlayer(), ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(Core.getInstance().getSettingsConfiguration().getConfig().getString("LabyMod.Status.HasGuild")))
                            .replace("%GUILDUPPERCASE%", user.getGuildName()).toUpperCase()
                            .replace("%GUILD%", user.getGuildName())
                            .replace("%GUILDCOLOR%", user.getGuildColor())
                            .replace("%GUILDTAG%", user.getGuildTag())
                            .replace("%GUILDRANK%", user.getGuildRank()));
                }
            }.runTaskLater(Core.getInstance(), 10L);
        }
    }
}
