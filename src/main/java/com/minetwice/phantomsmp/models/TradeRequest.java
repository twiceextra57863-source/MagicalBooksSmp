package com.minetwice.phantomsmp.models;

import org.bukkit.entity.Player;

public class TradeRequest {
    
    private final Player requester;
    private final Player target;
    private final long timestamp;
    
    public TradeRequest(Player requester, Player target) {
        this.requester = requester;
        this.target = target;
        this.timestamp = System.currentTimeMillis();
    }
    
    public Player getRequester() {
        return requester;
    }
    
    public Player getTarget() {
        return target;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public boolean isExpired(int timeoutSeconds) {
        return System.currentTimeMillis() - timestamp > (timeoutSeconds * 1000L);
    }
}
