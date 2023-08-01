package org.radium.guildsplugin.util.config;

import net.md_5.bungee.config.Configuration;

import java.io.File;

public interface Config {
    boolean exists();

    void delete();

    Configuration getConfig();

    void save();

    File getFile();
}