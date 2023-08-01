package org.radium.guildsspigot;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.radium.guildsspigot.events.BungeeMessageListener;
import org.radium.guildsspigot.events.FriendlyFireListener;
import org.radium.guildsspigot.events.PlayerDeathListener;
import org.radium.guildsspigot.events.PlayerJoinListener;
import org.radium.guildsspigot.expansion.PlaceholderExpansion;
import org.radium.guildsspigot.manager.object.User;
import org.radium.guildsspigot.util.SimpleConfig;

import java.util.HashMap;

public final class Core extends JavaPlugin {
    private static @Getter Core instance;
    private final HashMap<String, User> users = new HashMap<>();
    private @Getter SimpleConfig settingsConfiguration;
    @Override
    public void onEnable() {
        instance = this;
        loadConfiguration();
        loadListeners();
        loadPlaceholderExpansion();
    }

    @Override
    public void onDisable() {

    }
    private void loadConfiguration(){
        settingsConfiguration = new SimpleConfig("config.yml", getDataFolder().getPath());
    }
    private void loadListeners(){
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeMessageListener());
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new FriendlyFireListener(), this);
    }
    private void loadPlaceholderExpansion(){
        new BukkitRunnable(){
            public void run(){
                PlaceholderAPI.registerExpansion(new PlaceholderExpansion());
            }
        }.runTaskLater(this, 3L);
    }
    public User getUser(String name) {
        return (name == null) ? null : this.users.computeIfAbsent(name, k -> {
            return (new User(name));
        });
    }
}
