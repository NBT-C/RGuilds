package org.radium.guildsplugin.util.config;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;
import org.radium.guildsplugin.Core;

import java.io.*;
import java.nio.file.Files;

public class SimpleConfig implements Config {
    public String path;
    private final File file;
    private Configuration cfg;

    public SimpleConfig(String name, String path) {
        this.path = path;
        (new File(path)).mkdir();
        this.file = new File(path, name);
        if (!this.file.exists()) {
            try {
                extractFiles(name);
                this.cfg = YamlConfiguration.getProvider(YamlConfiguration.class).load(this.file);
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }
        try {
            this.cfg = YamlConfiguration.getProvider(YamlConfiguration.class).load(this.file);
        }catch (Exception var){
            var.printStackTrace();
        }
    }

    public boolean exists() {
        return this.file.exists();
    }

    public void delete() {
        this.file.delete();
        this.cfg = null;
    }

    public Configuration getConfig() {
        if (this.cfg == null) {
            try {
                this.cfg = YamlConfiguration.getProvider(YamlConfiguration.class).load(this.file);
            }catch (Exception var){
                var.printStackTrace();
            }
        }
        return this.cfg;
    }

    public void save() {
        try {
            YamlConfiguration.getProvider(YamlConfiguration.class).save(cfg, this.file);
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }

    public File getFile() {
        return this.file;
    }
    public boolean extractFiles(String fileName) {
        InputStream inputStream = getClass().getResourceAsStream("/" + fileName);
        File destinationFile = new File(Core.getInstance().getDataFolder(), fileName);
        if (destinationFile.exists()) {
            return true;
        }
        File destinationDir = destinationFile.getParentFile();
        if (!destinationDir.exists()) {
            destinationDir.mkdirs();
        }
        try {
            OutputStream outputStream = Files.newOutputStream(destinationFile.toPath());
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
