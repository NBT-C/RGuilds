//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.radium.guildsspigot.util;

import java.io.File;
import org.bukkit.configuration.file.YamlConfiguration;

public interface Config {
    boolean exists();

    void delete();

    YamlConfiguration getConfig();

    void save();

    File getFile();
}
