package org.radium.guildsspigot.expansion;

import org.bukkit.configuration.file.YamlConfiguration;
import org.radium.guildsspigot.Core;
import org.bukkit.entity.Player;
import org.radium.guildsspigot.manager.object.User;

import java.util.Objects;

public class PlaceholderExpansion extends me.clip.placeholderapi.expansion.PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "guilds";
    }

    @Override
    public String getAuthor() {
        return "NBTC";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null) {
            return "";
        }
        User user = Core.getInstance().getUser(player.getName());
        YamlConfiguration config = Core.getInstance().getSettingsConfiguration().getConfig();
        switch (identifier) {
            case "guildname":
                if (user.getGuildName() == null || user.getGuildName().isEmpty()) {
                    return config.getString("Settings.GuildName.NotFound");
                }
                return "&" + user.getGuildColor() + user.getGuildName();

            case "guildtag":
                if (user.getGuildTag() == null || user.getGuildTag().isEmpty()) {
                    return config.getString("Settings.GuildTag.NotFound");
                }
                return Objects.requireNonNull(config.getString("Settings.GuildTag.Format"))
                        .replace("%color%", "&" + user.getGuildColor())
                        .replace("%tag%", user.getGuildTag());

            case "guildcolor":
                if (user.getGuildColor() == null || user.getGuildColor().isEmpty()) {
                    return config.getString("Settings.GuildColor.NotFound");
                }
                return "&" + user.getGuildColor();

            case "guildrank":
                if (user.getGuildRank() == null || user.getGuildRank().isEmpty()) {
                    return "&7";
                }
                return user.getGuildRank();
        }
        return null;
    }
}
