package com.minetwice.phantomsmp.models;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    
    private final UUID playerUUID;
    private String playerName;
    private String bookId;
    private int bookLevel;
    private int kills;
    private boolean graceActive;
    private final Map<String, Long> cooldowns = new HashMap<>();
    
    public PlayerData(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }
    
    public UUID getPlayerUUID() {
        return playerUUID;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public String getBookId() {
        return bookId;
    }
    
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    
    public int getBookLevel() {
        return bookLevel;
    }
    
    public void setBookLevel(int bookLevel) {
        this.bookLevel = bookLevel;
    }
    
    public int getKills() {
        return kills;
    }
    
    public void setKills(int kills) {
        this.kills = kills;
    }
    
    public boolean isGraceActive() {
        return graceActive;
    }
    
    public void setGraceActive(boolean graceActive) {
        this.graceActive = graceActive;
    }
    
    public Map<String, Long> getCooldowns() {
        return cooldowns;
    }
    
    public void setCooldown(String abilityKey, long expiry) {
        cooldowns.put(abilityKey, expiry);
    }
    
    public void removeCooldown(String abilityKey) {
        cooldowns.remove(abilityKey);
    }
    
    public boolean hasCooldown(String abilityKey) {
        if (!cooldowns.containsKey(abilityKey)) return false;
        
        long expiry = cooldowns.get(abilityKey);
        if (System.currentTimeMillis() >= expiry) {
            cooldowns.remove(abilityKey);
            return false;
        }
        
        return true;
    }
    
    public long getCooldownRemaining(String abilityKey) {
        if (!cooldowns.containsKey(abilityKey)) return 0;
        
        long remaining = (cooldowns.get(abilityKey) - System.currentTimeMillis()) / 1000;
        return Math.max(0, remaining);
    }
}
