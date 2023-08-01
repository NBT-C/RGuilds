package org.radium.guildsplugin.manager.object.guild;

import lombok.Data;
import org.radium.guildsplugin.enums.GuildPermissionType;
@Data
public class GuildPermission {

    private final GuildPermissionType guildPermissionType;

    public GuildPermission(GuildPermissionType guildPermissionType) {
        this.guildPermissionType = guildPermissionType;
    }
}