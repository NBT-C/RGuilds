package org.radium.guildsplugin.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum GuildRankType {
    MASTER(1, "&4&lMASTER", Arrays.asList(
            GuildPermissionType.INVITE,
            GuildPermissionType.KICK,
            GuildPermissionType.PROMOTE,
            GuildPermissionType.DEMOTE
    ), "ADMINISTRATOR", null),
    ADMINISTRATOR(2, "&4Administrator", Arrays.asList(
            GuildPermissionType.INVITE,
            GuildPermissionType.KICK,
            GuildPermissionType.PROMOTE,
            GuildPermissionType.DEMOTE
    ), "MODERATOR", null),
    MODERATOR(3, "&cModerator", Arrays.asList(
            GuildPermissionType.INVITE,
            GuildPermissionType.KICK
    ), "CHAMPION", "ADMINISTRATOR"),
    CHAMPION(4, "&eChampion", Arrays.asList(
            GuildPermissionType.INVITE
    ), "STRONG", "MODERATOR"),
    STRONG(5, "&6Strong", new ArrayList<>(),
            "MEMBER", "CHAMPION"),
    MEMBER(6, "&7Member", new ArrayList<>(),
            null, "STRONG");
    private @Getter final String displayName;
    private @Getter final List<GuildPermissionType> guildPermissionType;
    private @Getter final int weight;
    private final String previousRank;
    private final String nextRank;
    GuildRankType(int weight, String displayName, List<GuildPermissionType> guildPermissionTypeList, String previousRank, String nextRank){
        this.weight = weight;
        this.displayName = displayName;
        this.guildPermissionType = new ArrayList<>(guildPermissionTypeList);
        this.previousRank = previousRank;
        this.nextRank = nextRank;
    }
    public GuildRankType getNextRank() {
        if (nextRank == null)
            return null;
        return GuildRankType.valueOf(nextRank);
    }
    public GuildRankType getPreviousRank() {
        if (previousRank == null)
            return null;
        return GuildRankType.valueOf(previousRank);
    }
}
