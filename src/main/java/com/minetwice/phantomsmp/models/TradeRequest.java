package com.minetwice.phantomsmp.models;

import java.util.UUID;

public class TradeRequest {
    private UUID sender;
    private UUID target;

    public TradeRequest(UUID sender, UUID target) {
        this.sender = sender;
        this.target = target;
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getTarget() {
        return target;
    }
}