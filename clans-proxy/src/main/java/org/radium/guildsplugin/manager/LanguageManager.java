package org.radium.guildsplugin.manager;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;
import org.radium.guildsplugin.Core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class LanguageManager {
    public static String getMsg(String key) {
        String language = Core.getInstance().getSettings().getConfig().getString("Language");
        File languageFile = new File(Core.getInstance().getDataFolder() + "/lang/messages_" + language + ".yml");
        if (!languageFile.exists()) {
            Core.getInstance().getLogger().warning("Language file not found: " + language);
            return "Language file not found: " + language;
        }
        Configuration languageConfig = null;
        try {
            languageConfig = YamlConfiguration.getProvider(YamlConfiguration.class).load(languageFile);
        }catch (Exception ignored){}

        if (languageConfig == null) return "ERROR";

        if (!languageConfig.contains(key)) {
            Core.getInstance().getLogger().warning("Message not found for key '" + key + "' in language '" + language + "'");
            return "Missing message: " + key +", Please delete the messages_<lang>.yml folder to get replaced!";
        }

        return languageConfig.getString(key);
    }
}