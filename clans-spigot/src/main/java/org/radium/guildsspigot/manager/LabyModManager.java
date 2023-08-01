package org.radium.guildsspigot.manager;

import at.julian.labymodapi.LabyModProtocol;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.radium.guildsspigot.Core;

import java.util.UUID;

public class LabyModManager {
    public static void setSubtitle(Player receiver, Player toSetSubtitle, String value) {

        JsonArray array = new JsonArray();

        JsonObject subtitle = new JsonObject();
        subtitle.addProperty( "uuid", toSetSubtitle.getUniqueId().toString() );
        subtitle.addProperty( "size", Core.getInstance().getSettingsConfiguration().getConfig().getDouble("LabyMod.Titles.Size") );

        if(value != null)
            subtitle.addProperty( "value", value );

        array.add(subtitle);

        LabyModProtocol.sendLabyModMessage( receiver, "account_subtitle", array );
    }

    public static void sendCurrentPlayingGamemode(Player player, String gamemodeName) {
        if (gamemodeName.length() > 32) {
            gamemodeName = gamemodeName.substring(0, 29) + "...";
        }

        JsonObject object = new JsonObject();
        object.addProperty("show_gamemode", true);
        object.addProperty("gamemode_name", gamemodeName);

        LabyModProtocol.sendLabyModMessage(player, "server_gamemode", object);
    }

}
