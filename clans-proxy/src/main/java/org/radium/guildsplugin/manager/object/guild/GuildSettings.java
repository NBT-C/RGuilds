package org.radium.guildsplugin.manager.object.guild;

import lombok.Data;
import org.radium.guildsplugin.manager.object.member.GuildMember;

import java.util.ArrayList;
import java.util.List;

@Data
public class GuildSettings {
    private List<GuildMember> guildMemberList;
    private GuildStats guildStats;
    private String guildColor;
    private String guildTag;
    private String guildName;
    private String leader;

    public GuildSettings(GuildStats guildStats, String guildColor, String guildName, String guildTag, String leader) {
        this.guildMemberList = new ArrayList<>();
        this.guildName = guildName;
        this.guildColor = guildColor;
        this.guildStats = guildStats;
        this.guildTag = guildTag;
        this.leader = leader;
    }
}
