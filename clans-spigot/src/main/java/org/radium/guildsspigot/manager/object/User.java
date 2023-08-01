package org.radium.guildsspigot.manager.object;

import lombok.Data;

@Data
public class User {
    private final String player;
    private String guildName, guildTag, guildColor, guildRank;
    public User(String player){
        this.player = player;
    }

}
