package org.radium.guildsplugin;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.radium.guildsplugin.commands.GCCommand;
import org.radium.guildsplugin.commands.guild.GuildCommand;
import org.radium.guildsplugin.commands.guildadmin.GuildAdminCommand;
import org.radium.guildsplugin.listener.PlayerConnectListener;
import org.radium.guildsplugin.listener.PlayerDisconnectListener;
import org.radium.guildsplugin.listener.SpigotMessageListener;
import org.radium.guildsplugin.manager.GuildManager;
import org.radium.guildsplugin.manager.GuildMemberManager;
import org.radium.guildsplugin.manager.IDGeneratorManager;
import org.radium.guildsplugin.manager.storage.DataManager;
import org.radium.guildsplugin.util.config.SimpleConfig;

import java.util.logging.Level;

public final class Core extends Plugin {
    private static @Getter
    @Setter
    @MonotonicNonNull Core instance;
    private @Getter
    @Setter
    @MonotonicNonNull GuildMemberManager guildMemberManager;
    private @Getter
    @Setter
    @MonotonicNonNull GuildManager guildManager;
    private @Getter
    @Setter
    @MonotonicNonNull DataManager dataManager;
    private @Getter SimpleConfig langEu;
    private @Getter SimpleConfig databaseSettings, settings;

    @Override
    public void onEnable() {
        setInstance(this);
        init();
        load();
    }

    @Override
    public void onDisable() {
        save();
    }

    private void init() {
        setDataManager(new DataManager());
        setGuildMemberManager(new GuildMemberManager());
        setGuildManager(new GuildManager());
    }

    private void load() {
        loadConfiguration();
        if (!getDataManager().connect()) {
            getLogger().log(Level.SEVERE, "Disabling plugin...");
            getProxy().getPluginManager().unregisterCommands(this);
            getProxy().getPluginManager().unregisterListeners(this);
            return;
        }
        loadCommands();
        loadListeners();
        getGuildManager().load(true, true);
        IDGeneratorManager.loadHighestIdFromDatabase();
    }

    private void save() {
        getDataManager().disconnect();
    }

    private void loadConfiguration() {
        settings = new SimpleConfig("Settings.yml", getDataFolder().getPath());
        databaseSettings = new SimpleConfig("DatabaseSettings.yml", getDataFolder().getPath());
        //<--- LANGUAGES --->//
        langEu = new SimpleConfig("lang/messages_en.yml", getDataFolder().getPath());
    }

    private void loadCommands() {
        getProxy().getPluginManager().registerCommand(this, new GuildCommand());
        getProxy().getPluginManager().registerCommand(this, new GuildAdminCommand());
        getProxy().getPluginManager().registerCommand(this, new GCCommand());
    }
    private void loadListeners(){
        getProxy().getPluginManager().registerListener(this, new SpigotMessageListener());
        getProxy().getPluginManager().registerListener(this, new PlayerConnectListener());
        getProxy().getPluginManager().registerListener(this, new PlayerDisconnectListener());
    }
}
