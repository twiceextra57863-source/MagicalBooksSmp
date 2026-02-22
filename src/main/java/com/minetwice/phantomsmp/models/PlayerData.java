package com.minetwice.phantomsmp.models;

import java.util.UUID;

public class PlayerData {
    private UUID uuid;
    private int gems;

    public PlayerData(UUID uuid, int gems) {
        this.uuid = uuid;
        this.gems = gems;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getGems() {
        return gems;
    }
}