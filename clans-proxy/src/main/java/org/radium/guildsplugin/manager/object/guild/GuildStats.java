package org.radium.guildsplugin.manager.object.guild;

import lombok.Data;

@Data
public class GuildStats {
    private int globalKills;
    private int globalDeaths;
    private int globalPoints;
    public GuildStats(int globalKills, int globalDeaths, int globalPoints){
        this.globalKills = globalKills;
        this.globalDeaths = globalDeaths;
        this.globalPoints = globalPoints;
    }
    public void addStat(String stat, int amount){
        switch (stat){
            case "kills":
                globalKills += amount;
                break;
            case "deaths":
                globalDeaths += amount;
                break;
            case "points":
                globalPoints += amount;
        }
    }
}
