package com.minetwice.phantomsmp.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownUtils {
    
    private static final Map<String, Long> cooldowns = new HashMap<>();
    
    public static void setCooldown(Player player, String ability, int seconds) {
        String key = player.getUniqueId().toString() + "_" + ability;
        cooldowns.put(key, System.currentTimeMillis() + (seconds * 1000L));
    }
    
    public static boolean isOnCooldown(Player player, String ability) {
        String key = player.getUniqueId().toString() + "_" + ability;
        
        if (!cooldowns.containsKey(key)) return false;
        
        long expiry = cooldowns.get(key);
        if (System.currentTimeMillis() >= expiry) {
            cooldowns.remove(key);
            return false;
        }
        
        return true;
    }
    
    public static long getRemainingCooldown(Player player, String ability) {
        String key = player.getUniqueId().toString() + "_" + ability;
        
        if (!cooldowns.containsKey(key)) return 0;
        
        long remaining = (cooldowns.get(key) - System.currentTimeMillis()) / 1000;
        return Math.max(0, remaining);
    }
    
    public static void removeCooldown(Player player, String ability) {
        String key = player.getUniqueId().toString() + "_" + ability;
        cooldowns.remove(key);
    }
    
    public static void clearPlayerCooldowns(Player player) {
        cooldowns.entrySet().removeIf(entry -> entry.getKey().startsWith(player.getUniqueId().toString()));
    }
}
