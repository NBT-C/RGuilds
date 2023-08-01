package org.radium.guildsplugin.manager.object.guild;

import lombok.Data;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.enums.GuildRankType;
import org.radium.guildsplugin.manager.object.member.GuildMember;

import java.util.Objects;

@Data
public class GuildInvite {
    private final Guild inviter;
    private final ProxiedPlayer invited;
    public GuildInvite(Guild inviter, ProxiedPlayer invited){
        this.invited = invited;
        this.inviter = inviter;
    }
    public void accept(){
        Core.getInstance().getGuildMemberManager().addMember(invited.getName(), invited.getUniqueId(), inviter.getId(), GuildRankType.MEMBER);
    }
    @Override
    public int hashCode() {
        return Objects.hash(invited);
    }
}
