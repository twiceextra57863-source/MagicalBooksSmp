package com.minetwice.phantomsmp.managers;

import com.minetwice.phantomsmp.PhantomSMP;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    
    private final PhantomSMP plugin;
    private final Map<String, Long> cooldowns = new HashMap<>();
    private final Map<String, BukkitRunnable> activeTasks = new HashMap<>();
    
    public CooldownManager(PhantomSMP plugin) {
        this.plugin = plugin;
    }
    
    public void setCooldown(String key, int seconds) {
        long expiry = System.currentTimeMillis() + (seconds * 1000L);
        cooldowns.put(key, expiry);
        
        // Schedule removal
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                cooldowns.remove(key);
                activeTasks.remove(key);
            }
        };
        
        task.runTaskLater(plugin, seconds * 20L);
        activeTasks.put(key, task);
    }
    
    public boolean isOnCooldown(String key) {
        if (!cooldowns.containsKey(key)) return false;
        
        long expiry = cooldowns.get(key);
        if (System.currentTimeMillis() >= expiry) {
            cooldowns.remove(key);
            if (activeTasks.containsKey(key)) {
                activeTasks.get(key).cancel();
                activeTasks.remove(key);
            }
            return false;
        }
        
        return true;
    }
    
    public long getRemainingCooldown(String key) {
        if (!cooldowns.containsKey(key)) return 0;
        
        long remaining = (cooldowns.get(key) - System.currentTimeMillis()) / 1000;
        return Math.max(0, remaining);
    }
    
    public void removeCooldown(String key) {
        cooldowns.remove(key);
        if (activeTasks.containsKey(key)) {
            activeTasks.get(key).cancel();
            activeTasks.remove(key);
        }
    }
    
    public void resetPlayerCooldowns(Player player) {
        UUID playerId = player.getUniqueId();
        
        cooldowns.entrySet().removeIf(entry -> {
            if (entry.getKey().startsWith(playerId.toString())) {
                if (activeTasks.containsKey(entry.getKey())) {
                    activeTasks.get(entry.getKey()).cancel();
                    activeTasks.remove(entry.getKey());
                }
                return true;
            }
            return false;
        });
    }
    
    public void saveCooldowns() {
        // Cooldowns are transient and don't need to be saved
        // They will reset on restart as per design
    }
    
    public void loadCooldowns() {
        // Cooldowns are transient
    }
}
