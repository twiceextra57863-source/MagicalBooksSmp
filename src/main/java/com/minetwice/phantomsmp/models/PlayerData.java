package com.minetwice.phantomsmp.models;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerData {
    
    private final UUID playerUUID;
    private String playerName;
    private String bookId;
    private int bookLevel;
    private int kills;
    private boolean graceActive;
    private long lastSeen;
    private long playtime;
    private final Map<String, Long> cooldowns = new ConcurrentHashMap<>();
    private final Map<String, Object> metadata = new HashMap<>();
    
    public PlayerData(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.lastSeen = System.currentTimeMillis();
        this.playtime = 0;
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
    
    public long getLastSeen() { 
        return lastSeen; 
    }
    
    public void setLastSeen(long lastSeen) { 
        this.lastSeen = lastSeen; 
    }
    
    public long getPlaytime() { 
        return playtime; 
    }
    
    public void setPlaytime(long playtime) { 
        this.playtime = playtime; 
    }
    
    public Map<String, Long> getCooldowns() { 
        return cooldowns; 
    }
    
    public Map<String, Object> getMetadata() { 
        return metadata; 
    }
}
